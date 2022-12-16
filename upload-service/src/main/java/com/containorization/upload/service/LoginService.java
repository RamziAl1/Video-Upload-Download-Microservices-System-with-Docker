package com.containorization.upload.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class LoginService {
    public boolean login(String endpoint, String username, String password) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("username", username);
        body.add("password", password);
        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        Boolean isValidLogin = rt
                .postForObject(endpoint, requestEntity, Boolean.class);
        return isValidLogin;
    }
}
