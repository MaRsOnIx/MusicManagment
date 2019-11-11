package me.marsonix.spotifyapitest.controllers;

import lombok.AllArgsConstructor;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.models.spotify.Search;
import me.marsonix.spotifyapitest.models.spotify.Type;
import me.marsonix.spotifyapitest.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@AllArgsConstructor
@RequestMapping("search")
class SearchController {


    private LogManager logManager;
    private SearchService searchService;

    @GetMapping
    public String search(@RequestParam String content,
                         @RequestParam  Type type,
                         @RequestParam(required = false,
                                 defaultValue = "0") Integer offset,
                         Model model) throws IOException,
            SpotifyConnectionException {

        if(content.trim().equals("")) return "redirect:/";

        if(type==Type.TRACK) {
            model.addAttribute("container", searchService.getTracks(new Search(content, type, 10, offset)));
            logManager.userInfo("has opened search page for tracks: "+content, getClass());
            return "searchTrack";
        }
        model.addAttribute("container", searchService.getArtists(new Search(content, type, 10, offset)));
        logManager.userInfo("has opened search page for artists: "+content, getClass());
        return "searchArtist";


    }








}
