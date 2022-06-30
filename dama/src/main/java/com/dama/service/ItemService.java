package com.dama.service;

import com.dama.model.dto.ItemResponseDto;
import com.dama.model.dto.request.ItemRequestDto;
import com.dama.model.entity.Item;
import com.dama.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

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
        Optional<Item> findItem = itemRepository.findByItemCode(itemRequestDto.getItemCode());
        System.out.println("findItem = " + findItem);
        findItem.get().returnApiUpdateItemState(itemRequestDto);
    }
}
