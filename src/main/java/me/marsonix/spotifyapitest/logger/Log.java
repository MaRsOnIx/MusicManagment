package me.marsonix.spotifyapitest.logger;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Log {

    @Id
    @GeneratedValue
    private Long id;
    private String message;
    @Enumerated
    private Type type;
    private LocalDateTime expiredDate;

}
