package com.dama.repository;

import com.dama.model.dto.request.ItemSearchDto;
import com.dama.model.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomItemRepository {
    Page<Item> search(ItemSearchDto itemSearchDto, Pageable pageable);
}
