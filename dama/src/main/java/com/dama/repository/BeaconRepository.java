package com.dama.repository;

import com.dama.model.entity.Beacon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeaconRepository extends JpaRepository<Beacon,Long> {
    Optional<Beacon> findByObname(String obname);
}
