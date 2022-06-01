package com.dama.controller.api;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.dto.response.ItemSearchResponseDto;
import com.dama.model.entity.Item;
import com.dama.service.ItemService;
import com.dama.principal.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/register")
    public String returnUserRole(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return userDetails.returnMember().getRole().toString();
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerItem(@RequestBody ItemRequestDto itemRequestDto){
        System.out.println("itemRequestDto = " + itemRequestDto.getItemName());
        itemService.saveItem(itemRequestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<ItemSearchResponseDto> searchItem(@RequestParam("itemName")String itemName){
        Item item = itemService.searchItem(itemName);
        ItemSearchResponseDto itemSearchResponseDto=new ItemSearchResponseDto();
        itemSearchResponseDto.setItemName(itemName);
        itemSearchResponseDto.setLocale(item.getLocale());
        itemSearchResponseDto.setPrice(item.getPrice());
        itemSearchResponseDto.setWeight(item.getWeight());
        return new ResponseEntity<>(itemSearchResponseDto,HttpStatus.OK);
    }

    @GetMapping("/itemList")
    public ResponseEntity<List<Item>> returnItemList(){
        List<Item> items = itemService.ReturnItemList();
        return new ResponseEntity<>(items,HttpStatus.OK);
    }
}
