package ace.claims.csuiteapi.controllers;

import ace.claims.csuiteapi.models.Certificate;
import ace.claims.csuiteapi.models.SerializableDataContainer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class CertController {

    @GetMapping("/createCert")
    public void createCert() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("C:\\Windows\\system32\\cmd.exe")
                .directory(new File("C:\\Users\\brosh\\Documents\\tvault\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl"));
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
    public ResponseEntity<String> getAllCertificates() throws JsonProcessingException {
        String filePrefix = "gwcpnonprod.aceclublink.com-";

        File directory = new File("C:\\Users\\brosh\\Documents\\tvault\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl");

        List<Certificate> certificateList = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] matchingFiles = directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.contains(filePrefix);
                }
            });

            if (matchingFiles != null && matchingFiles.length > 0) {
                System.out.println("Files found:");
                Arrays.stream(matchingFiles).forEach(file -> {
                    System.out.println(file.getName());
                    certificateList.add(new Certificate(file.getName(), "internal"));
                });
            } else {
                System.out.println("No files found with the prefix: " + filePrefix);
            }

        } else {
            System.out.println("Invalid directory path.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ObjectMapper().writeValueAsString(certificateList));
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/certificates/download", produces = "application/json")
    public ResponseEntity<InputStreamResource> downloadCert() throws IOException {
        byte[] fileBytes = readFileToByteArray(new File("C:\\Users\\brosh\\Documents\\tvault\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl\\gwcpnonprod.aceclublink.com-privkey.key"));
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(fileBytes));
        return ResponseEntity.status(HttpStatus.OK).body(resource);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/certificate/internal", produces = "application/json")
    public ResponseEntity<Certificate> getCertDetail(@RequestParam String certName) throws IOException {
        byte[] fileBytes = readFileToByteArray(new File("C:\\Users\\brosh\\Documents\\tvault\\t-vault\\csuiteapi2\\csuiteapi\\src\\main\\resources\\openssl\\" + certName));
        return ResponseEntity.status(HttpStatus.OK).body(new Certificate(certName, "jks"));
    }


    public static byte[] readFileToByteArray(File file) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());
        return Files.readAllBytes(path);
    }

}

