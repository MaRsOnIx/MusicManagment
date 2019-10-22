package me.marsonix.spotifyapitest.schedule;

import me.marsonix.spotifyapitest.logger.Log;
import me.marsonix.spotifyapitest.logger.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class LogScheduler {

    @Autowired
    LogRepository logRepository;

    @Scheduled(fixedRate = 60000)
    public void removing(){
        for (Log log : logRepository.findAll()) {
            if (LocalDateTime.now().isAfter(log.getExpiredDate())) {
                logRepository.delete(log);
            }
        }
    }
}
