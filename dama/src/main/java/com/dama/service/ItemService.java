package com.dama.service;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.dto.response.ItemWebSocketResponseDTO;
import com.dama.model.entity.Item;
import com.dama.principal.SecurityUtil;
import com.dama.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {
    private final ItemRepository itemRepository;

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

    public List<Item> ReturnItemList(){
        List<Item> all = itemRepository.findAll();
        Collections.reverse(all);
        return all;
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

    public ResponseEntity<?> findItemStateByItemCodeToWebSocket(String itemCode, HashMap<String, Integer> hashMap) throws InterruptedException {


        if (itemCode == null){
            return new ResponseEntity<>("wait",HttpStatus.OK);
        }else {

            Optional<Item> findItem = itemRepository.findByItemCode(itemCode);
            ItemWebSocketResponseDTO returnDto=new ItemWebSocketResponseDTO();
            if (findItem.isPresent()) {
                if (hashMap.containsKey(itemCode)) {
                    System.out.println("count :" +hashMap.get(itemCode));
                    Integer integer = hashMap.get(itemCode);
                    integer++;
                    returnDto.setItemCode(findItem.get().getItemCode());
                    returnDto.setItemName(findItem.get().getItemName());
                    returnDto.setLocale(findItem.get().getLocale());
                    returnDto.setPrice(findItem.get().getPrice());
                    returnDto.setWeight(findItem.get().getWeight());
                    returnDto.setCount(integer);
                }
            }
            return new ResponseEntity<>(returnDto, HttpStatus.OK);
        }
    }
}
