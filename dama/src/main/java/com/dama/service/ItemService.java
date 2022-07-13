package com.dama.service;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.dto.response.ItemWebSocketResponseDTO;
import com.dama.model.entity.Item;
import com.dama.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public ResponseEntity<?> findItemStateByItemCodeToWebSocket(String itemCode) throws InterruptedException {
        long time = 1000;
        System.out.println("long = "+time);
        if (itemCode == null){
            Thread.sleep(time);
            return new ResponseEntity<>("잠시만 기달려주세요",HttpStatus.OK);
        }else {
            Optional<Item> findItem = itemRepository.findByItemCode(itemCode);
            ItemWebSocketResponseDTO returnDto=new ItemWebSocketResponseDTO();
            if (findItem.isPresent()) {
                returnDto.setItemCode(findItem.get().getItemCode());
                returnDto.setItemName(findItem.get().getItemName());
                returnDto.setLocale(findItem.get().getLocale());
                returnDto.setPrice(findItem.get().getPrice());
                returnDto.setWeight(findItem.get().getWeight());
            }
            return new ResponseEntity<>(returnDto, HttpStatus.OK);
        }
    }
}
