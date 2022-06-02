package com.dama.service.impl;

import com.dama.model.dto.request.ItemSearchDto;
import com.dama.model.entity.Category;
import com.dama.model.entity.Item;
import com.dama.repository.CustomItemRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.dama.model.entity.QItem.item;
import static com.dama.model.entity.QCategory.category;


@Repository
public class CustomItemRepositoryImpl implements CustomItemRepository {

    @Override
    public Page<Item> searchItem(ItemSearchDto itemSearchDto, Pageable pageable) {
        return null;
    }


    private BooleanExpression categorySR(Category category){
        return category != null ? item.category.eq(category): null;
    }
}
