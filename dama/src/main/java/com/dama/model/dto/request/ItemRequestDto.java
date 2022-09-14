package com.dama.model.dto.request;

import com.dama.model.entity.Category;
import com.dama.model.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;

    private String itemName;

    private String itemCode;

    private int price;

    private double weight;

    private String locale;

    private Category category;

//    private MultipartFile image;

//    private String imgUrl;

    public Item toEntity(){
        return Item.builder()
                .id(id)
                .itemName(itemName)
                .itemCode(itemCode)
                .price(price)
                .weight(weight)
                .locale(locale)
                .category(category)
//                .imgUrl(imgUrl)
                .build();

    }
}
