package me.marsonix.spotifyapitest.logger;

import lombok.Data;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Data
public class Log {

    private Long id;
    private String message;
    private Type type;
    private LocalDate localDate;

    private static AtomicLong al = new AtomicLong();

    public Log(String message, Type type, LocalDate localDate) {
        this.id=al.getAndIncrement();
        this.message = message;
        this.type = type;
        this.localDate = localDate;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
