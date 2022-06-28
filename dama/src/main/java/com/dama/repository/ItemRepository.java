package com.dama.repository;


import com.dama.model.entity.Item;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long>/*,CustomItemRepository*/ {
    Optional<Item> findByItemCode(String itemCode);

    /*@EntityGraph(attributePaths = {"itemName"})*/
    Optional<Item> findByItemName(String itemName);
}
