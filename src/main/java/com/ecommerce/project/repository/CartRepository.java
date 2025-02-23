package com.ecommerce.project.repository;

import com.ecommerce.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c from Cart c where c.user.email = ?1")
    Cart findCartByEmail(String email);

    @Query("SELECT c from Cart c where c.user.email = ?1 AND c.cartId=?2")
    Cart findByEmailAndCartId(String email, Long cartId);
}
