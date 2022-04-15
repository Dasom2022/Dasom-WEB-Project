package com.dama.controller.mvc;


import com.dama.model.dto.ItemDto;
import com.dama.principal.UserDetailsImpl;
import com.dama.service.ItemService;
import com.dama.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/item")
public class ItemController {

    private final MemberService memberService;

    private final ItemService itemService;

    @GetMapping("/register")
    public String itemRegisterForm(@AuthenticationPrincipal UserDetailsImpl userDetails){
        if(userDetails.returnProfile().getRole()=="ROLE_ADMIN"){
            return "/item/register";
        }else {
            return "redirect:/";
        }
    }
    @PostMapping("/register")
    public String itemRegister(ItemDto itemDto){
        itemService.saveItem(itemDto);
        return "redirect:/item/register";
    }
}
