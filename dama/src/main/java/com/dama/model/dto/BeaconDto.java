package com.dama.model.dto;

import com.dama.model.entity.Beacon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BeaconDto {
    private String ob_name;
    private String beacon;

    public Beacon toEntity(){
        return Beacon.builder()
                .obname(ob_name)
                .beacon(beacon)
                .build();
    }
}
