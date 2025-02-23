package com.ecommerce.project.repository;

import com.ecommerce.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci from CartItem ci where ci.cart.cartId=?1 AND ci.product.productId=?2")
    CartItem findCartItemByProductIdAndCartId(Long productId, Long cartId);

    @Query("DELETE from CartItem ci where ci.cart.cartId = ?1 and  ci.product.productId = ?2")
    void deleteCartItemsByProductIdAndCartId(long cartId, long productId);
}



