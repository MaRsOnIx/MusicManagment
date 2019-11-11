package me.marsonix.spotifyapitest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MarsonixApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarsonixApplication.class, args);

    }

}
