package ace.claims.csuiteapi.services;
import ace.claims.csuiteapi.models.Keystore;
import ace.claims.csuiteapi.models.KeystoreDetails;
import ace.claims.csuiteapi.models.KeystorePayload;
import ace.claims.csuiteapi.models.KeystoreUpload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class KeystoreService {

    @Autowired
    private Environment env;


    public ResponseEntity<String> getAllKeystores() throws URISyntaxException, JsonProcessingException {
        String fileSuffix = ".jks";
        File directory = new File(getPath());

        List<Keystore> keystoreList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(fileSuffix);
                }
            });

            if (matchingFiles != null && matchingFiles.length > 0) {
                Arrays.stream(matchingFiles).forEach(file -> {
                    System.out.println(file.getName());
                    Date earliestCertExpiryDate;
                    String algNames;
                    try {
                        earliestCertExpiryDate = parseCertDetails(file.getAbsolutePath()).getEarliestCertExpiryDate();
                        algNames = parseCertDetails(file.getAbsolutePath()).getAlgorithmNames();
                    } catch (NoSuchElementException ex) {
                        System.out.println("No such element exception: " + ex.getMessage());
                        return;
                    }
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


    public ResponseEntity<InputStreamResource> uploadKeystore(KeystorePayload payload) throws KeyStoreException, CertificateException,
            IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        List<KeystoreUpload> uploads = payload.getUploads();
        String absolutePath = getPath();
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(null, null);
        File keyStoreFile = new File(absolutePath + "\\" + payload.getKeystoreName());

        PrivateKey privateKey = null;
        String privKeyFileName = "";
        List<java.security.cert.Certificate> chain = new ArrayList<>();
        for (KeystoreUpload upload : uploads) {
            String fileName = upload.getFileName();
            if (fileName.toLowerCase().endsWith(".pem") || fileName.toLowerCase().endsWith(".crt")) {
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(absolutePath + "\\" + fileName), StandardCharsets.ISO_8859_1))) {
                    writer.write(upload.getContent());
                    System.out.println("Successfully wrote to the file: " + absolutePath + "\\" + fileName);
                } catch (IOException e) {
                    System.err.println("Error writing to file: " + e.getMessage());
                }

                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream fis = new FileInputStream(absolutePath + "\\" + fileName);
                BufferedInputStream bis = new BufferedInputStream(fis);
                while (bis.available() > 0) {
                    java.security.cert.Certificate cert = cf.generateCertificate(bis);
                    chain.add(cert);
                    trustStore.setCertificateEntry(fileName, cert);
                }
            } else if (fileName.toLowerCase().endsWith(".key")) {
                String pkcs8Pem = upload.getContent();
                pkcs8Pem = pkcs8Pem.replace("-----BEGIN PRIVATE KEY-----", "");
                pkcs8Pem = pkcs8Pem.replace("-----END PRIVATE KEY-----", "");
                pkcs8Pem = pkcs8Pem.replaceAll("\\s+","");

                Base64.Decoder decoder = Base64.getDecoder();
                byte [] pkcs8EncodedBytes = decoder.decode(pkcs8Pem);

                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
                KeyFactory kf = KeyFactory.getInstance("RSA");
                privateKey = kf.generatePrivate(keySpec);
                privKeyFileName = fileName;
            }
        }

        if (privateKey != null) {
            trustStore.setKeyEntry(privKeyFileName, privateKey, getPassword(), chain.toArray(new Certificate[0]));
        }

        try (FileOutputStream fos = new FileOutputStream(keyStoreFile)) {
            trustStore.store(fos, getPassword());
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }


    public ResponseEntity<InputStreamResource> downloadKeystore(String certName) throws IOException {
        byte[] fileBytes = readFileToByteArray(new File(getPath() + "\\" + certName));
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }


    public ResponseEntity<Keystore> getKeystoreDetail(String keystoreName) {
        File file = new File(getPath() + "\\" + keystoreName);
        Date earliestCertExpiryDate = parseCertDetails(file.getAbsolutePath()).getEarliestCertExpiryDate();
        String algNames = "";
        try {
            algNames = parseCertDetails(file.getAbsolutePath()).getAlgorithmNames();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new Keystore(
                keystoreName, new String[] {}, new SimpleDateFormat("MM/dd/yyyy").format(earliestCertExpiryDate),
                new SimpleDateFormat("MM/dd/yyyy").format(new Date()), "ClaimCenter", "admin@gmail.com",
                "Claims Product Non-Prod", "JKS", algNames));
    }


    public static byte[] readFileToByteArray(File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }


    private KeystoreDetails parseCertDetails(String path) throws NoSuchElementException {
        List<Date> expiryDates = new ArrayList<>();
        List<String> algorithmNames = new ArrayList<>();
        try {
            File file = new File(path);
            InputStream is = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, getPassword());
            Enumeration<String> enumeration = keystore.aliases();
            while(enumeration.hasMoreElements()) {
                String alias = enumeration.nextElement();
                X509Certificate certificate = (X509Certificate) keystore.getCertificate(alias);
                algorithmNames.add(certificate.getSigAlgName().replaceAll("with", "-"));
                expiryDates.add(certificate.getNotAfter());
            }

        } catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }

        algorithmNames = algorithmNames
                .stream()
                .distinct()
                .collect(Collectors.toList());

        return new KeystoreDetails(
                Collections.min(expiryDates),
                String.join(", ", algorithmNames));
    }


    private String getPath() {
        return env.getProperty("keystore.folder.path");
    }

    private char[] getPassword() {
        byte[] decodedBytes = Base64.getDecoder()
                .decode(env.getProperty("encoded.pw"));
        return (new String(decodedBytes, StandardCharsets.UTF_8))
                .toCharArray();
    }

}
