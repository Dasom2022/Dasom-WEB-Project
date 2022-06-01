package com.dama.controller.api;

import com.dama.model.entity.SonicWave;
import com.dama.repository.SonicWaveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/sensor")
public class SensorApiController {

    private SonicWaveRepository sonicWaveRepository;

    @PostMapping("/allValueList")
    public ResponseEntity<List<?>> sensorValueList(){
        List<SonicWave> all = sonicWaveRepository.findAllList();
        return (ResponseEntity<List<?>>) all;
    }
}
