package com.containrorization.authenticationservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authenticate")
public class AuthenticationController {

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Boolean validate(@RequestParam("username") String username, @RequestParam("password") String password){
        if (username.equals("username") && password.equals("password"))
            return true;
        else
            return false;
    }
}
