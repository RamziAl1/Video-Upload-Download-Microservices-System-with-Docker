package com.containorization.upload.controller;

import com.containorization.upload.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String getLoginPage(ModelMap m) {
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    protected String login(@RequestParam("username") String username, @RequestParam("password") String password, ModelMap m) throws ResourceAccessException, IOException {
        String endpoint = "http://auth-service:8082/authenticate/";

        boolean isValidLogin = loginService.login(endpoint, username, password);

        if (isValidLogin) {
            return "video-submission";
        } else {
            m.put("status", "login failure");
            return "login";
        }
    }
}
