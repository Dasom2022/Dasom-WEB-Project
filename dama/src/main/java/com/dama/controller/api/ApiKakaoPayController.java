package com.dama.controller.api;

import com.dama.model.dto.request.kakaopay.ApproveRequest;
import com.dama.service.social.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credit")
@RequiredArgsConstructor
@Log4j2
public class ApiKakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/KakaoPay")
    public ResponseEntity<?> getKakaoPayInfo(@RequestBody ApproveRequest approveRequest){
        kakaoPayService.payReady(approveRequest);
        System.out.println("approveRequest = " + approveRequest.getItem_name());
        return new ResponseEntity<>(1, HttpStatus.OK);
    }
}

