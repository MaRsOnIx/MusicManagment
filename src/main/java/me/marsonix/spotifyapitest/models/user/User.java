package me.marsonix.spotifyapitest.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class User {

    @Id
    private String id;
    @ElementCollection(targetClass=String.class)
    private List<String> farvoriteTracks;

}
