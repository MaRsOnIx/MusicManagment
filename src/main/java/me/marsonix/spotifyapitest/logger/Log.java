package me.marsonix.spotifyapitest.logger;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Log {

    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Type type;
    private LocalDateTime expiredDate;

    public Log() {
    }

    public Log(Long id, String message, Type type, LocalDateTime expiredDate) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.expiredDate = expiredDate;
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

    public LocalDateTime getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(LocalDateTime expiredDate) {
        this.expiredDate = expiredDate;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", expiredDate=" + expiredDate +
                '}';
    }
}
