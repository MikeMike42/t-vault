package ace.claims.csuiteapi.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    public String getHealthy() {
        return "Healthy!";
    }

}
