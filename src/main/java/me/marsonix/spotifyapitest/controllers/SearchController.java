package me.marsonix.spotifyapitest.controllers;

import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.models.Search;
import me.marsonix.spotifyapitest.models.Type;
import me.marsonix.spotifyapitest.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("search")
public class SearchController {


    @Autowired
    private LogManager logManager;

    @Autowired
    private SearchService searchService;


//    @GetMapping()
//    public String test(){
//        Object details = ((UsernamePasswordAuthenticationToken) ((OAuth2Authentication) ((SecurityContextImpl) SecurityContextHolder.getContext()).getAuthentication()).getUserAuthentication()).getDetails();
//        return details.toString();
//    }



    @PostMapping
    public String search(@RequestParam String content, @RequestParam  Type type, @RequestParam(required = false, defaultValue = "0") Integer offset, Model model) throws IOException{
        if(type==Type.TRACK) {
            model.addAttribute("container", searchService.getTracks(new Search(content, type, 10, offset)));
            return "searchTrack";
        }
        model.addAttribute("container", searchService.getArtists(new Search(content, type, 10, offset)));
        return "searchArtist";


    }








}
