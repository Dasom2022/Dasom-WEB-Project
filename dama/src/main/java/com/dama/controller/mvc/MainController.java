package com.dama.controller.mvc;

import com.dama.model.dto.ItemDto;
import com.dama.model.dto.request.SignInRequestDto;
import com.dama.model.entity.Item;
import com.dama.service.ItemService;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    private final MemberService memberService;

    @GetMapping("/")
    public String index(){
        /*ItemDto item=new ItemDto("고구마","abc123",abc123,35.6,"신선코너");
        itemService.saveItem(item);
        */return "react Connected?";
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        log.info("로그인 컨트롤러");
        boolean signin = memberService.findMemberByPasswordAndUsername(signInRequestDto.getUsername(),signInRequestDto.getPassword());
        if (signin) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /*@GetMapping("/chat/chat")
    public String chatForm(){

        return "/chat/chat";
    }*/
}
