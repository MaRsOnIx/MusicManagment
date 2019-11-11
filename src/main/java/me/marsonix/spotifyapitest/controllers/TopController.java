package me.marsonix.spotifyapitest.controllers;

import lombok.AllArgsConstructor;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("top")
@AllArgsConstructor
class TopController {


    private LogManager logManager;
    private SearchService searchService;

    @GetMapping
    public String search(@RequestParam String id, Model model) throws IOException, SpotifyConnectionException {
        model.addAttribute("container", searchService.getTopTracks(id));
        logManager.userInfo("has opened top page", getClass());
        return "searchTrack";



    }








}
