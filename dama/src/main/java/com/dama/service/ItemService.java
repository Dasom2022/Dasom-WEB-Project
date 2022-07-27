package com.dama.service;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.dto.request.MemberItemPocketRequestDto;
import com.dama.model.dto.response.ItemListResponseDto;
import com.dama.model.dto.response.ItemWebSocketResponseDTO;
import com.dama.model.entity.Item;
import com.dama.model.entity.Member;
import com.dama.principal.SecurityUtil;
import com.dama.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

    private final MemberService memberService;
    @Transactional
    public Long saveItem(ItemRequestDto item){
        return itemRepository.save(item.toEntity()).getId();
    }

    public ItemResponseDto returnItemState(String itemCode){
        Item item = itemRepository.findByItemCode(itemCode).get();
        ItemResponseDto itemResponseDto=new ItemResponseDto(item.getItemName(),item.getLocale(),item.getPrice(),item.getWeight());
        return itemResponseDto;
    }

    public Item searchItem(String itemName){
        Item item = itemRepository.findByItemName(itemName).get();
        return item;
    }

    public ArrayList<ItemListResponseDto> ReturnItemList(String accessToken){
        List<Item> all = itemRepository.findAll();
        ItemListResponseDto i = new ItemListResponseDto();
        ArrayList<ItemListResponseDto> AI=new ArrayList<>();
        Member findMember = memberService.findByAccessToken(accessToken);
        for (Item item:all){
            ItemListResponseDto itemListResponseDto = setItemListResponseDto(i, item, findMember);
            AI.add(itemListResponseDto);
        }
        Collections.reverse(AI);
        return AI;
    }

    @Transactional
    public void returnApiDeleteItem(Long id){
        Optional<Item> findItem = itemRepository.findById(id);
        itemRepository.delete(findItem.get());
    }

    @Transactional
    public void returnApiUpdateItemState(ItemRequestDto itemRequestDto) {
        Item findItem = itemRepository.findById(itemRequestDto.getId()).get();
        System.out.println("findItem = " + findItem.getItemName());
        System.out.println("findItem.getId() = " + findItem.getId());
        findItem.returnApiUpdateItemState(itemRequestDto.getItemCode(),itemRequestDto.getItemName(),itemRequestDto.getPrice(),itemRequestDto.getWeight(),itemRequestDto.getLocale());
    }

    public ResponseEntity<?> findItemStateByItemCodeToWebSocket(String itemCode,int count,int totalCount,int totalPrice) throws InterruptedException {

        if (itemCode == null){
            return new ResponseEntity<>("wait",HttpStatus.OK);
        }else {
            Optional<Item> findItem = itemRepository.findByItemCode(itemCode);
            ItemWebSocketResponseDTO returnDto=new ItemWebSocketResponseDTO();
            if (findItem.isPresent()) {
                returnDto.setItemCode(findItem.get().getItemCode());
                returnDto.setItemName(findItem.get().getItemName());
                returnDto.setLocale(findItem.get().getLocale());
                returnDto.setPrice(findItem.get().getPrice()*count);
                returnDto.setWeight(findItem.get().getWeight());
                returnDto.setCount(count);
                returnDto.setTotalCount(totalCount);
                returnDto.setTotalPrice(totalPrice);
            }
            return new ResponseEntity<>(returnDto, HttpStatus.OK);
        }
    }

    @Transactional
    public List<Item> insertMemberItemPocket(String accessToken,List<MemberItemPocketRequestDto> m) {
        System.out.println("accessTokenItemPocket = " + accessToken);
        Member findMember = memberService.findByAccessToken(accessToken);
        System.out.println("findMember = " + findMember.getId());
        List<Item> items = findMember.insertMemberItemPocket(m);
        return items;
    }

    public ItemListResponseDto setItemListResponseDto(ItemListResponseDto itemListResponseDto,Item item,Member member){
        itemListResponseDto.setItemName(item.getItemName());
        itemListResponseDto.setItemCode(item.getItemCode());
        itemListResponseDto.setLocale(item.getLocale());
        itemListResponseDto.setPrice(item.getPrice());
        itemListResponseDto.setWeight(item.getWeight());
        itemListResponseDto.setId(item.getId());
        itemListResponseDto.setMember(member);

        return itemListResponseDto;
    }
}
