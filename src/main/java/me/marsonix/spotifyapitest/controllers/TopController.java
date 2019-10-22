package me.marsonix.spotifyapitest.controllers;

import me.marsonix.spotifyapitest.exceptions.MissingPropertyException;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.models.spotify.Search;
import me.marsonix.spotifyapitest.models.spotify.Type;
import me.marsonix.spotifyapitest.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@RequestMapping("top")
public class TopController {


    @Autowired
    private LogManager logManager;

    @Autowired
    private SearchService searchService;

    @GetMapping
    public String search(@RequestParam String id, Model model) throws IOException {
        model.addAttribute("container", searchService.getTopTracks(id));
        return "searchTrack";



    }








}
