package com.dama.controller.mvc;

import com.dama.model.entity.Item;
import com.dama.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping("/")
    public String index(){
        Item item=new Item("고구마","abc123",20000,35.6,"신선코너");
        itemService.saveItem(item);
        return "index";
    }

    /*@GetMapping("/chat/chat")
    public String chatForm(){

        return "/chat/chat";
    }*/
}