package com.dama.service.qrcode;

import com.dama.model.entity.Qr;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface Resource<T> {
    @GetMapping
    ResponseEntity<Collection<T>> findAll();

    @GetMapping("{id}")
    ResponseEntity<T> findById(@PathVariable UUID id);

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<T> save(@RequestBody T t);

    ResponseEntity<Qr> update(Qr qr);

    @PatchMapping("{id}")
    ResponseEntity<T> patch(@PathVariable UUID id, @RequestBody Map<Object, Object> fields);

    @DeleteMapping("{id}")
    ResponseEntity<String> deleteById(@PathVariable UUID id);

    @GetMapping("/invalid")
    ResponseEntity<String> invalid();
}
