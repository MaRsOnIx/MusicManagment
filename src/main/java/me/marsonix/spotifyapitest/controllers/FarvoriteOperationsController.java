package me.marsonix.spotifyapitest.controllers;


import me.marsonix.spotifyapitest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("farvoritesOperations")
@RestController
public class FarvoriteOperationsController {

    @Autowired
    UserService userService;


    @PostMapping ("{id}")
    public boolean add(@PathVariable String id){
        return userService.addNewTrack(id);
    }

    @DeleteMapping ("{id}")
    public boolean delete(@PathVariable String id){
        return userService.deleteTrack(id);
    }

}
