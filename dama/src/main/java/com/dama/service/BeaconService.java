package com.dama.service;

import com.dama.model.dto.BeaconDto;
import com.dama.model.entity.Beacon;
import com.dama.repository.BeaconRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class BeaconService {
    private final BeaconRepository beaconRepository;

    @Transactional
    public void insertionOrUpdate(BeaconDto beaconDto){
        System.out.println("beaconDto.getOb_name() = " + beaconDto.getOb_name());
        System.out.println("beaconDto = " + beaconDto.getBeacon());
        Optional<Beacon> findObject = beaconRepository.findByObname(beaconDto.getOb_name());
        if(findObject.isEmpty()){
            beaconRepository.save(beaconDto.toEntity());
        }else {
            findObject.get().UpdateBeacon(beaconDto.getOb_name(), beaconDto.getBeacon());
        }
    }
}
