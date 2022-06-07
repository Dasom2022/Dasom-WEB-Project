package com.dama.model.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Beacon extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "beacon_object_name")
    private String obname;

    @Column(name = "beacon_distance")
    private String beacon;

    @Column(name = "beacon_count")
    private int count;

    public void UpdateBeacon(String obname,String beacon){
        this.obname=obname;
        this.beacon=beacon;
    }

    @Builder
    public Beacon(Long id, String obname, String beacon) {
        this.id = id;
        this.obname = obname;
        this.beacon = beacon;
    }
}


