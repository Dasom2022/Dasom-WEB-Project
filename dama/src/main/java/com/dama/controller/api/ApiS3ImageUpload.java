package com.dama.controller.api;

import com.dama.service.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ApiS3ImageUpload {

    private final S3Uploader s3Uploader;

    @PostMapping("/api/images/qr")
    public ResponseEntity<?> qrmake(@RequestParam("images")MultipartFile multipartFile) throws IOException{
        s3Uploader.upload(multipartFile,"static");
        return new ResponseEntity<>("qr 성공!", HttpStatus.OK);
    }
}
