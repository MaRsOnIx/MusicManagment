package me.marsonix.spotifyapitest.controllers;

import me.marsonix.spotifyapitest.logger.Log;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.logger.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;

@Controller
public class IndexController {

    @Autowired
    private LogManager logManager;

    @GetMapping
    public String wlecome(Model model) {
        LinkedHashMap details = (LinkedHashMap) (((OAuth2Authentication) (SecurityContextHolder
                .getContext())
                .getAuthentication())
                .getUserAuthentication())
                .getDetails();
        logManager.info("Testuje sobie", getClass());
        String name = details.values().toArray()[1].toString();
        model.addAttribute("name", name);
        return "index";
    }

}
