package com.dama.controller.api;

import com.dama.model.dto.request.kakaopay.ApproveRequest;
import com.dama.model.dto.request.kakaopay.CompletedRequestDto;
import com.dama.model.dto.response.kakaopay.ApproveResponse;
import com.dama.model.dto.response.kakaopay.ReadyResponse;
import com.dama.service.social.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credit/KakaoPay")
@RequiredArgsConstructor
@Log4j2
public class ApiKakaoPayController {

    private final KakaoPayService kakaoPayService;

    @PostMapping("/ready")
    public ResponseEntity<?> getKakaoPayInfo(@RequestBody ApproveRequest approveRequest){
        System.out.println("approveRequest = " + approveRequest.getItem_name());
        System.out.println("approveRequest.getPartner_order_id() = " + approveRequest.getPartner_order_id());
        ReadyResponse readyResponse = kakaoPayService.payReady(approveRequest);
        System.out.println("readyResponse.getTid() = " + readyResponse.getTid());
        return new ResponseEntity<>(readyResponse, HttpStatus.OK);
    }

    @PostMapping("/completed")
    public ResponseEntity<?> KakaoPayCompleted(@RequestBody CompletedRequestDto completedRequestDto){
        System.out.println("completedRequestDto.getCid() = " + completedRequestDto.getCid());
        ApproveResponse approveResponse = kakaoPayService.payApprove(completedRequestDto);
        System.out.println("approveResponse.getItem_code() = " + approveResponse.getItem_code());
        return new ResponseEntity<>(approveResponse,HttpStatus.OK);
    }

    @PostMapping("/cancle")
    public ResponseEntity<String> KakaoPayCancle(){
        return new ResponseEntity<>("결제가 취소가 되었습니다!",HttpStatus.OK);
    }

    @PostMapping("/fail")
    public ResponseEntity<String> KakaoPayFail(){
        return new ResponseEntity<>("결제가 실패하였습니다!",HttpStatus.OK);
    }

}

