package com.assetManager.server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class HelloWorldController {

    @PostMapping("/healthcheck")
    public ResponseEntity<String> heathcheck (HttpServletRequest request) {
        return ResponseEntity.ok("Good!");
    }

}
