package me.marsonix.spotifyapitest.logger;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LogRepository {
    private List<Log> logList;
    LogRepository(){
        logList = new ArrayList<>();
        logList.add(new Log("TestError", Type.ERROR, LocalDate.now()));
        logList.add(new Log("Test Warning", Type.WARING, LocalDate.now()));
        logList.add(new Log("Test Info", Type.INFO, LocalDate.now()));
    }

    void save(Log log){
        logList.add(log);
    }
}
