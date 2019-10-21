package me.marsonix.spotifyapitest.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;

@Controller
public class IndexController {


    @GetMapping
    public String wlecome(Model model) {
        LinkedHashMap details = (LinkedHashMap) (((OAuth2Authentication) (SecurityContextHolder
                .getContext())
                .getAuthentication())
                .getUserAuthentication())
                .getDetails();

        String name = details.values().toArray()[1].toString();
        model.addAttribute("name", name);
        return "index";
    }

}
