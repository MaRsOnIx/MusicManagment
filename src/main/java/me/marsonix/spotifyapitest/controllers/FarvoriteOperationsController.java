package me.marsonix.spotifyapitest.controllers;


import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RequestMapping("farvoritesOperations")
@RestController
class FarvoriteOperationsController {

    @Autowired
    UserService userService;
    @Autowired
    LogManager logManager;

    @PostMapping ("{id}")
    public boolean add(@PathVariable String id){
        logManager.userInfo("has added "+id+" track to farvorite", getClass());
        return userService.addNewTrack(id);
    }

    @DeleteMapping ("{id}")
    public boolean delete(@PathVariable String id){
        logManager.userInfo("has removed "+id+" track to farvorite", getClass());
        return userService.deleteTrack(id);
    }

}
