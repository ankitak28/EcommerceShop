package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO adProductToCart(Long productId, Integer quantity);

    List<CartDTO> getAllCarts();

    CartDTO getCart(String emailId, Long id);

    CartDTO updateProductQuantityInCart(Long productId, int operation);

    String deleteProductFromCart(long cartId, long productId);
}
