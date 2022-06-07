package com.dama.service;

import com.dama.model.dto.BeaconDto;
import com.dama.model.entity.Beacon;
import com.dama.repository.BeaconRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BeaconService {
    private final BeaconRepository beaconRepository;

    public void insertionOrUpdate(BeaconDto beaconDto){
        Optional<Beacon> findObject = beaconRepository.findByObname(beaconDto.getOb_name());
        if(findObject.isEmpty()){
            beaconRepository.save(beaconDto.toEntity());
        }else {
            beaconDto.toEntity().UpdateBeacon(beaconDto.getOb_name(), beaconDto.getBeacon());
        }
    }
}
