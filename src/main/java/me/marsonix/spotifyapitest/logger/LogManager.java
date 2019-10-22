package me.marsonix.spotifyapitest.logger;

import me.marsonix.spotifyapitest.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LogManager {

    private LogRepository logRepository;

    @Autowired
    private UserService userService;

    @Autowired
    public LogManager(LogRepository logRepository){
        this.logRepository=logRepository;
    }


    public void userInfo(String message, Class cl){
        info("{"+userService.getUserId()+"} "+message, cl);
    }

    public void info(String message, Class cl){
        Log log = new Log();
        log.setType(Type.INFO);
        log.setMessage(message);
        log.setExpiredDate(LocalDateTime.now().plusHours(1));
        save(log, cl);
    }

    private void save(Log log, Class cl){

        logRepository.save(log);
        Logger logger = LoggerFactory.getLogger(cl);
        switch (log.getType()) {
            case INFO:
                logger.info(log.getMessage());
                return;
            case WARING:
                logger.warn(log.getMessage());
                break;
            case ERROR:
                logger.error(log.getMessage());
                break;
        }


    }



}
