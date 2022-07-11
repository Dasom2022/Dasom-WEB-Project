package com.dama.service.qrcode.impl;

import com.dama.model.entity.Qr;
import com.dama.principal.QrMethodUtils;
import com.dama.service.qrcode.IService;
import com.dama.service.qrcode.Resource;
import com.dama.service.qrcode.exception.ApplicationException;
import com.dama.service.qrcode.exception.QrNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/qr")
public class QrResourceImpl implements Resource<Qr> {

    private IService<Qr> qrService = new IService<Qr>() {
        @Override
        public Collection<Qr> findAll() {
            return null;
        }

        @Override
        public Optional<Qr> findById(UUID id) {
            return Optional.empty();
        }

        @Override
        public Qr saveOrUpdate(Qr qr) {
            return null;
        }

        @Override
        public List<Qr> saveAll(List<Qr> t) {
            return null;
        }

        @Override
        public String deleteById(UUID id) {
            return null;
        }
    };

    private final String imagePath = "./src/main/resources/qrcodes/QRCode.png";

    @GetMapping("/generateByteQRCode/{qrId}")
    public ResponseEntity<Qr> generateByteQRCode(@PathVariable("qrId")UUID qrId){
        log.info("QrResourceImpl - generateByteQRCode");
        Qr qrObject = null;
        Optional<Qr> qr = qrService.findById(qrId);
        if (!qr.isPresent()){
            throw new QrNotFoundException("QR not found");
        }else{
            qrObject = qr.get();
            qrObject.setBase64QRCode(QrMethodUtils.generateByteQRCode(qrObject.getCoverPhotoURL(), 250, 250));
            qrObject.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
        }

        return new ResponseEntity<>(qrObject, HttpStatus.OK);
    }

    @GetMapping("/generateImageQRCode/{qrId}")
    public ResponseEntity<Qr> generateImageQRCode(@PathVariable("qrId") UUID qrId){
        log.info("QrResourceImpl - generateImageQRCode");
        Qr qrObject = null;
        Optional<Qr> qr = qrService.findById(qrId);
        if (!qr.isPresent()){
            throw new QrNotFoundException("Qr not found");
        }else{
            qrObject = qr.get();
            QrMethodUtils.generateImageQRCode(qrObject.getCoverPhotoURL(), 250, 250, imagePath);
            qrObject.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
        }
        return new ResponseEntity<>(qrObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Collection<Qr>> findAll() {
        log.info("QrResourceImpl - findAll");
        Collection<Qr> qrs = qrService.findAll();
        List<Qr> response = new ArrayList<>();
        qrs.forEach(qr -> {
            qr.add(linkTo(methodOn(QrResourceImpl.class).findById(qr.getId())).withSelfRel());
            response.add(qr);
        });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Qr> findById(UUID id) {
        log.info("QrResourceImpl - findById");
        Qr qrObject = null;
        Optional<Qr> qr = qrService.findById(id);
        if (!qr.isPresent()) {
            throw new QrNotFoundException("Qr not found");
        } else {
            qrObject = qr.get();
            qrObject.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
        }
        return new ResponseEntity<>(qrObject, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Qr> save(Qr qr) {
        log.info("QrResourceImpl - save");
        if (qr.getId() != null) {
            throw new ApplicationException("Qr ID found, ID is not required for save the data");
        } else {
            Qr savedQr = qrService.saveOrUpdate(qr);
            savedQr.add(linkTo(methodOn(QrResourceImpl.class).findById(savedQr.getId())).withSelfRel());
            savedQr.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
            return new ResponseEntity<>(savedQr, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<Qr> update(Qr qr) {
        log.info("QrResourceImpl - update");
        if (qr.getId() == null) {
            throw new ApplicationException("Qr ID not found, ID is required for update the data");
        } else {
            Qr updatedQr = qrService.saveOrUpdate(qr);
            updatedQr.add(linkTo(methodOn(QrResourceImpl.class).findById(updatedQr.getId())).withSelfRel());
            updatedQr.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
            return new ResponseEntity<>(updatedQr, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Qr> patch(UUID id, Map<Object, Object> fields) {
        Optional<Qr> qr = qrService.findById(id);
        if (qr.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Qr.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, qr.get(), value);
            });
            Qr updatedQr = qrService.saveOrUpdate(qr.get());
            updatedQr.add(linkTo(methodOn(QrResourceImpl.class).findById(updatedQr.getId())).withSelfRel());
            updatedQr.add(linkTo(methodOn(QrResourceImpl.class).findAll()).withSelfRel());
            return new ResponseEntity<>(updatedQr, HttpStatus.OK);
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteById(UUID id) {
        log.info("QrResourceImpl - deleteById");
        Optional<Qr> qr = qrService.findById(id);
        if (!qr.isPresent()) {
            throw new QrNotFoundException("Qr not found");
        }
        return new ResponseEntity<>(qrService.deleteById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> invalid() {
        log.info("QrResourceImpl - invalid");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", "something is missing, please check everything before sending the request!!!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }
}
