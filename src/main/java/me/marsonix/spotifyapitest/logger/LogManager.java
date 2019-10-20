package me.marsonix.spotifyapitest.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LogManager {

    private LogRepository logRepository;

    @Autowired
    public LogManager(LogRepository logRepository){
        this.logRepository=logRepository;
    }


    // I know that I could create custom logger using slf4j libraries but I prefered to use faster way

    public void save(String message, Type type, LocalDate localDate, Class cl){

        Log log = new Log(message, type, localDate);

        logRepository.save(log);
        Logger logger = LoggerFactory.getLogger(cl);
        switch (log.getType()) {
            case INFO:
                logger.info(message);
                return;
            case WARING:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
        }


    }



}
