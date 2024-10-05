package com.example.demo.repository;

import com.example.demo.entity.CartItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_item WHERE cart_item_id = :cartItemId", nativeQuery = true)
    void deleteCartItemById(@Param("cartItemId") String cartItemId);
}
