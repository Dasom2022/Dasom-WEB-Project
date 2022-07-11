package com.dama.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Qr extends RepresentationModel<Qr> {

    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private String password;
    private String coverPhotoURL;
    private byte[] base64QRCode;

    @JsonCreator
    public Qr(@JsonProperty("id") UUID id, @JsonProperty("username") String username,
              @JsonProperty("password") String password, @JsonProperty("coverPhotoURL") String coverPhotoURL){
        this.id = id;
        this.username = username;
        this.password = password;
        this.coverPhotoURL = coverPhotoURL;
    }

    @JsonCreator
    public Qr(@JsonProperty("username") String username,
              @JsonProperty("password") String password, @JsonProperty("coverPhotoURL") String coverPhotoURL){
        this.username = username;
        this.password = password;
        this.coverPhotoURL = coverPhotoURL;
    }
}
