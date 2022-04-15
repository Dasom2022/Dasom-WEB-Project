package com.dama.controller.api;

import com.dama.model.dto.ItemResponseDto;
import com.dama.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    @PostMapping("/state")
    public ResponseEntity<ItemResponseDto> returnItemState(@RequestParam("itemCode")String itemCode){
        System.out.println("itemCode = " + itemCode);
        ItemResponseDto itemResponseDto = itemService.returnItemState(itemCode);
        return new ResponseEntity(itemResponseDto, HttpStatus.OK);
    }

}
