package com.dama.model.dto.response;

import com.dama.model.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ItemCodeByPuteResponseDTO {
    private List<Item> itemList;
    private HashMap<String,Integer> hashMap;
}
