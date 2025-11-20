package ace.claims.csuiteapi.controllers;

import ace.claims.csuiteapi.models.Keystore;
import ace.claims.csuiteapi.models.KeystoreDetails;
import ace.claims.csuiteapi.models.KeystoreUpload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.x500.X500Principal;
import java.io.*;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CertController {

    @GetMapping("/createCert")
    public void createCert() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\cmd.exe")
                .directory(new File("C:\\Users\\brosh\\OneDrive\\Desktop\\CSuite\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl"));
        pb.command().add("/c");
        pb.command().add("openssl pkcs12 -export -out gwcpnonprod.aceclublink.com_2024-01-06.p12 " +
                "-inkey gwcpnonprod.aceclublink.com-privkey.key -in gwcpnonprodaceclublinkcom_without_chain.pem");

        Process process = pb.start();

        int exitCode = process.waitFor();
        System.out.println("Command exited with code: " + exitCode);

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }
        String result = builder.toString();
        System.out.println(result);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/sslcert/allcertificates", produces = "application/json")
    public ResponseEntity<String> getAllCertificates() throws JsonProcessingException, URISyntaxException {
        String fileSuffix = ".jks";

        URL resourceUrl = CertController.class.getClassLoader().getResource("openssl/");
        assert resourceUrl != null;
        File directory = Paths.get(resourceUrl.toURI()).toFile();

//        File directory = new File("C:\\Users\\brosh\\OneDrive\\Desktop\\CSuite\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl");

        List<Keystore> keystoreList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(fileSuffix);
                }
            });

            if (matchingFiles != null && matchingFiles.length > 0) {
                System.out.println("Files found:");
                Arrays.stream(matchingFiles).forEach(file -> {
                    System.out.println(file.getName());
                    Date earliestCertExpiryDate = null;
                    String algNames = null;
                    try {
                        earliestCertExpiryDate = parseCertDetails(file.getAbsolutePath()).getEarliestCertExpiryDate();
                        algNames = parseCertDetails(file.getAbsolutePath()).getAlgorithmNames();
                    } catch (KeyStoreException e) {
                        System.out.println("failed to parse");
//                        throw new RuntimeException(e);
                    }
                    System.out.println("Earliest: " + earliestCertExpiryDate.toString());
                    keystoreList.add(new Keystore(file.getName(), new String[] {}, new SimpleDateFormat("MM/dd/yyyy").format(earliestCertExpiryDate),
                            new SimpleDateFormat("MM/dd/yyyy").format(new Date()),
                            "ClaimCenter", "admin@gmail.com", "Claims Product Non-Prod", "JKS", algNames));
                });
            } else {
                System.out.println("No files found with the suffix: " + fileSuffix);
            }

        } else {
            System.out.println("Invalid directory path.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ObjectMapper().writeValueAsString(keystoreList));
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/certificates/upload", produces = "application/json")
    public ResponseEntity<InputStreamResource> uploadKeystore(@RequestBody KeystoreUpload upload) throws IOException, URISyntaxException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
//        InputStream inputStream = new ByteArrayInputStream(upload.getContent());
//
        URL resourceUrl = CertController.class.getClassLoader().getResource("openssl/");
        assert resourceUrl != null;
        File resourceFolder = Paths.get(resourceUrl.toURI()).toFile();
        String absolutePath = resourceFolder.getAbsolutePath();
//
//        File file = new File(absolutePath + "\\" + upload.getFileName());
//
//        try(OutputStream outputStream = new FileOutputStream(file)) {
//            IOUtils.copy(inputStream, outputStream);
//        } catch (FileNotFoundException e) {
//            // handle exception here
//        } catch (IOException e) {
//            // handle exception here
//        }

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(absolutePath + "\\" + upload.getFileName()), StandardCharsets.ISO_8859_1))) {
            writer.write(upload.getContent());
            System.out.println("Successfully wrote to the file: " + absolutePath + "\\" + upload.getFileName());
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/certificates/download", produces = "application/json")
    public ResponseEntity<InputStreamResource> downloadCert(@RequestParam String certName) throws IOException {
        byte[] fileBytes = readFileToByteArray(new File("C:\\Users\\brosh\\OneDrive\\Desktop\\CSuite\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl\\" + certName));
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/sslcert/certificate/internal", produces = "application/json")
    public ResponseEntity<Keystore> getCertDetail(@RequestParam String certName) throws IOException, KeyStoreException {
        File file = new File("C:\\Users\\brosh\\OneDrive\\Desktop\\CSuite\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl\\" + certName);
        byte[] fileBytes = readFileToByteArray(file);
        Date earliestCertExpiryDate = parseCertDetails(file.getAbsolutePath()).getEarliestCertExpiryDate();
        String algNames = parseCertDetails(file.getAbsolutePath()).getAlgorithmNames();
        return ResponseEntity.status(HttpStatus.OK).body(new Keystore(certName, new String[] {}, new SimpleDateFormat("MM/dd/yyyy").format(earliestCertExpiryDate),
                new SimpleDateFormat("MM/dd/yyyy").format(new Date()),
                "ClaimCenter", "admin@gmail.com", "Claims Product Non-Prod", "JKS", algNames));
    }


    public static byte[] readFileToByteArray(File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }


    private static KeystoreDetails parseCertDetails(String path) throws KeyStoreException {
        List<Date> expiryDates = new ArrayList<>();
        List<String> algorithmNames = new ArrayList<>();
        try {

            File file = new File(path);
            InputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "autoclub1".toCharArray());
            Enumeration<String> enumeration = keystore.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
                algorithmNames.add(certificate.getSigAlgName().replaceAll("with", "-"));
                expiryDates.add(certificate.getNotAfter());
            }

        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            System.out.println("sucks to suck");
            //e.printStackTrace();
        }

        algorithmNames = algorithmNames
                .stream()
                .distinct()
                .collect(Collectors.toList());

        return new KeystoreDetails(Collections.min(expiryDates), String.join(", ", algorithmNames));
    }

}

