package com.dama.controller.api;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.dto.request.MemberItemPocketRequestDto;
import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.dto.response.ItemSearchResponseDto;
import com.dama.model.entity.Item;
import com.dama.service.ItemService;
import com.dama.principal.UserDetailsImpl;
import com.dama.service.MemberService;
import com.dama.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemApiController {

    private final ItemService itemService;

    private final MemberService memberService;

    private final JwtService jwtService;

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
    public ResponseEntity<?> registerItem(@RequestBody ItemRequestDto itemRequestDto,@RequestParam("accessToken") String accessToken){
        System.out.println("itemRequestDto = " + itemRequestDto.getItemName());
        String findMemberRole = memberService.returnMemberRole(accessToken);
        if (findMemberRole.equals("ADMIN")){
            itemService.saveItem(itemRequestDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }else return new ResponseEntity<>("관리자가 아니면 물품을 등록할 수 없습니다",HttpStatus.BAD_REQUEST);
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
    public ResponseEntity<List<Item> returnItemList(@RequestParam("accessToken")String accessToken){
        boolean tokenValid = jwtService.isTokenValid(accessToken);
        if (tokenValid){
            List<Item> returnItemList = itemService.ReturnItemList();
            return new ResponseEntity<>(returnItemList,HttpStatus.OK);
        }else {
            return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> returnApiItemDelete(@RequestParam("id") Long id,@RequestParam("accessToken") String accessToken){
        String findMemberRole = memberService.returnMemberRole(accessToken);
        if (findMemberRole.equals("ADMIN")) {
            itemService.returnApiDeleteItem(id);
            return new ResponseEntity<>("아이템 삭제 API 성공!", HttpStatus.OK);
        }else return new ResponseEntity<>("관리자가 아니라면 물품을 삭제할 수 없습니다",HttpStatus.BAD_REQUEST);
    }

    //커밋용 주석
    @PostMapping("/UpdateItemState")
    public  ResponseEntity<String> returnApiUpdateItemState(@RequestBody ItemRequestDto itemRequestDto,@RequestParam("accessToken") String accessToken){
        System.out.println("itemRequestDto.getId() = " + itemRequestDto.getId());
        String findMemberRole = memberService.returnMemberRole(accessToken);
        if (findMemberRole.equals("ADMIN")) {
            itemService.returnApiUpdateItemState(itemRequestDto);
            return new ResponseEntity<>("아이템 수정 API 성공!", HttpStatus.OK);
        }else return new ResponseEntity<>("관리자가 아니라면 물품을 수정할 수 없습니다",HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/memberItemPocket")
    public List<Item> insertMemberItemPocket(@RequestParam("accessToken")String accessToken, @RequestBody List<MemberItemPocketRequestDto> memberItemPocketRequestDto){
        for (int i=0;i<memberItemPocketRequestDto.size();i++){
            log.info(memberItemPocketRequestDto.get(i).getItemCode());
        }
        List<Item> items = itemService.insertMemberItemPocket(accessToken, memberItemPocketRequestDto);
        return items;
    }
}
