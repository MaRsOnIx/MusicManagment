package me.marsonix.spotifyapitest.controllers;

import lombok.AllArgsConstructor;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.models.dtos.ContainerTrackDTO;
import me.marsonix.spotifyapitest.models.spotify.Track;
import me.marsonix.spotifyapitest.services.SearchService;
import me.marsonix.spotifyapitest.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("farvorites")
@AllArgsConstructor
class FarvoritesController {


    private LogManager logManager;
    private UserService userService;
    private SearchService searchService;

    @GetMapping
    public String farvorites(Model model){
        LinkedHashMap details = (LinkedHashMap) (((OAuth2Authentication) (SecurityContextHolder
                .getContext())
                .getAuthentication())
                .getUserAuthentication())
                .getDetails();
        String id =details.values().toArray()[7].toString();
        List<Track> trackList = userService.getFarvoritesTracks(id);
        ContainerTrackDTO containerTrackDTO =
                ContainerTrackDTO.builder()
                        .content("Ulubione").items(trackList.stream()
                        .map(v -> searchService.trackToTransfer(v)).collect(Collectors.toList()))
                        .nextUrl("null").previousUrl("null").total(trackList.size()).build();
        logManager.userInfo("has opened farvorite page", getClass());
        model.addAttribute("container", containerTrackDTO);

        return "searchTrack";
    }




}
