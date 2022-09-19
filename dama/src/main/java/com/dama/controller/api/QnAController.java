package com.dama.controller.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1/qna")
public class QnAController {

    @GetMapping("/first")
    public ResponseEntity<?> getFirstQnaReturnApi(){
        List<String> qnaList=new ArrayList<>();
        qnaList.add("계정 정보를 변경하고 싶어요");
        qnaList.add("지도는 어떻게 보나요?");
        qnaList.add("상품이 인식이 안돼요");
        qnaList.add("카카오 페이 외 결제는 되나요?");
        qnaList.add("카메라가 작동이 안돼요");
        qnaList.add("카트 반납은 어디에 하나요");
        return new ResponseEntity<>(qnaList, HttpStatus.OK);
    }

    @PostMapping("/second")
    public ResponseEntity<?> getSecondQnaReturnAPi(@RequestParam("secondQ") String secondQ){
        if (secondQ.equals("0")){
            List<String> changeList=new ArrayList<>();
            changeList.add("비밀번호 안바꿔줌");
            changeList.add("아이디도 안바꿔줌");
            changeList.add("이메일은 더 안바꿔줌");
            return new ResponseEntity<>(changeList,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
