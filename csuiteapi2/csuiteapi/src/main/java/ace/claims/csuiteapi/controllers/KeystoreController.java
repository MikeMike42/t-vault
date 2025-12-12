package ace.claims.csuiteapi.controllers;

import ace.claims.csuiteapi.models.Keystore;
import ace.claims.csuiteapi.models.KeystorePayload;
import ace.claims.csuiteapi.services.KeystoreService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class KeystoreController {

    @Autowired
    KeystoreService service;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/sslcert/allkeystores", produces = "application/json")
    public ResponseEntity<String> getAllKeystores() throws JsonProcessingException, URISyntaxException {
        return service.getAllKeystores();
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/keystores/upload", produces = "application/json")
    public ResponseEntity<InputStreamResource> uploadKeystore(@RequestBody KeystorePayload payload) throws IOException,
            KeyStoreException, CertificateException, NoSuchAlgorithmException, InvalidKeySpecException {
        return service.uploadKeystore(payload);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/sslcert/keystores/download", produces = "application/json")
    public ResponseEntity<InputStreamResource> downloadKeystore(@RequestParam String keystoreName) throws IOException {
        return service.downloadKeystore(keystoreName);
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/sslcert/keystores/internal", produces = "application/json")
    public ResponseEntity<Keystore> getKeystoreDetail(@RequestParam String keystoreName) {
        return service.getKeystoreDetail(keystoreName);
    }

}

