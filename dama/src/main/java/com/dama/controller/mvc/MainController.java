package com.dama.controller.mvc;

import com.dama.model.dto.request.SignInRequestDto;
import com.dama.model.dto.response.IndexResponseUserDto;
import com.dama.model.entity.SonicWave;
import com.dama.repository.SonicWaveRepository;
import com.dama.service.ItemService;
import com.dama.service.MemberService;
import com.dama.principal.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/")
public class MainController {

    private final ItemService itemService;

    private final MemberService memberService;

    private final SonicWaveRepository sonicWaveRepository;
    @GetMapping("/main")
    public ResponseEntity<IndexResponseUserDto> index(){
        System.out.println("인덱스들어옴");
        /*UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        */IndexResponseUserDto indexResponseUserDto=new IndexResponseUserDto();
/*
        indexResponseUserDto.setUsername(userDetails.getUsername());
*/
        /*
        indexResponseUserDto.setUsername(userDetails.getUsername());
*/
        return new ResponseEntity<>(indexResponseUserDto,HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@RequestBody SignInRequestDto signInRequestDto){
        log.info("로그인 컨트롤러");
        System.out.println("signInRequestDto = " + signInRequestDto);
        boolean signin = memberService.findMemberByPasswordAndUsername(signInRequestDto.getUsername(),signInRequestDto.getPassword());
        if (signin) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/testUrl")
    public @ResponseBody ResponseEntity<String> test(){
        System.out.println("testtetst = ");
        return new ResponseEntity<>("테스트용",HttpStatus.OK);
    }


    /*@GetMapping("/chat/chat")
    public String chatForm(){

        return "/chat/chat";
    }*/

    @PostMapping("/raspi")
    public void raspi(@RequestParam("id") String id,@RequestParam("d")String d){
        SonicWave sonicWave=new SonicWave();
        sonicWave.setSensor_user(id);
        sonicWave.setDistance(d);
        sonicWaveRepository.save(sonicWave);
    }

    @GetMapping("/view")
    public String viewHtml(){
        return "view";
    }
}
