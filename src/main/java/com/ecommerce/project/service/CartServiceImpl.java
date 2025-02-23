package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.util.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    private Cart createCart() {
        Cart userCart = cartRepository.findCartByEmail(authUtil.loggedInEmail());
        if(userCart != null) {
            return userCart;
        }
        Cart cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.loggedInUser());
        Cart newCart = cartRepository.save(cart);
        return newCart;
    }

    @Override
    public CartDTO adProductToCart(Long productId, Integer quantity) {
        //Find existing cart or create one
        //Retrieve Product Details
        //Perform Validations
        //Create cart Item
        //Save cart item
        //return updated cart
        Cart cart = createCart();
        Product product = productRepository.findById(productId).orElseThrow(
                ()-> new ResourceNotFoundException("Product", "productId", productId)
        );
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cart.getCartId());
        if(cartItem != null) {
            throw new APIException("Product "+product.getProductName()+" already exists in the cart.");
        }
        if(product.getQuantity() == 0) {
            throw new APIException("Product "+product.getProductName()+" is not available");
        }
        if(quantity > product.getQuantity()){
            throw new APIException("Please make an order of  "+product.getProductName()+" less than or equal to"+quantity);
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);
        newCartItem.setCart(cart);
        newCartItem.setDiscount(product.getDiscount());
        newCartItem.setProductPrice(product.getSpecialPrice());
        cartItemRepository.save(newCartItem);
        product.setQuantity(product.getQuantity());
        cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice()*quantity));
        cartRepository.save(cart);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<CartItem> cartItems = cart.getCartItems();
        Stream<ProductDTO> productStream= cartItems.stream().map(
                item ->{
                    ProductDTO map = modelMapper.map(item, ProductDTO.class);
                    map.setQuantity(item.getQuantity());
                    return map;
                }
        );
        cartDTO.setProducts(productStream.toList());
        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.size() == 0) {
            throw new APIException("No carts found.");
        }

        List<CartDTO> cartDTOs = carts.stream().map(
                cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTO> products = cart.getCartItems().stream().map(
                            p->modelMapper.map(p.getProduct(),ProductDTO.class)).collect(Collectors.toList());
                    cartDTO.setProducts(products);
                    return cartDTO;
                }).collect(Collectors.toList());
        return cartDTOs;
    }

    @Override
    public CartDTO getCart(String emailId, Long id) {

        Cart cart = cartRepository.findByEmailAndCartId(emailId,id);
        if(cart == null) {
            throw new ResourceNotFoundException("Cart","cartId",id);
        }
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        cart.getCartItems().forEach(c->c.getProduct().setQuantity(c.getQuantity()));
        List<ProductDTO> products = cart.getCartItems().stream().map(
                p->modelMapper.map(p.getProduct(), ProductDTO.class)
        ).toList();
        cartDTO.setProducts(products);
        return cartDTO;


    }

    @Transactional //Makes sure if any part of the method fails the transaction is rolled back
    @Override
    public CartDTO updateProductQuantityInCart(Long productId, int quantity) {
        String emailId = authUtil.loggedInEmail();
         Cart userCart = cartRepository.findCartByEmail(emailId);
         Long cartId = userCart.getCartId();

         Cart cart = cartRepository.findById(cartId).orElseThrow(
                 ()-> new ResourceNotFoundException("Cart","cartId",cartId));
         Product product = productRepository.findById(productId).orElseThrow(
                 ()-> new ResourceNotFoundException("Product","productId",productId));

         if(product.getQuantity() == 0) {
             throw new APIException("Product "+product.getProductName()+" is not available");
         }
         if(product.getQuantity()< quantity){
             throw new APIException("Please make an order of  "+product.getProductName()+" less than or equal to"+quantity);
         }

         CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(productId,cartId);
         if(cartItem == null) {
             throw new APIException("Product "+product.getProductName()+" not available in the cart");
         }
         cartItem.setProductPrice(product.getSpecialPrice());
         cartItem.setQuantity(product.getQuantity()+quantity);
         cartItem.setDiscount(product.getDiscount());
         cart.setTotalPrice(cart.getTotalPrice() + (cartItem.getProductPrice()* quantity));
         cartRepository.save(cart);
         CartItem updatedCartItem = cartItemRepository.save(cartItem);
         if(updatedCartItem.getQuantity() == 0){
             cartItemRepository.deleteById(updatedCartItem.getCartItemId());
         }
         CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
         List<CartItem> cartItems = cart.getCartItems();
         Stream<ProductDTO> productStream= cartItems.stream().map(
                 item->{
                     ProductDTO prd = modelMapper.map(item, ProductDTO.class);
                 prd.setQuantity(item.getQuantity());
                 return prd;
                 }
         );
         cartDTO.setProducts(productStream.toList());
         return cartDTO;
    }

    @Override
    public String deleteProductFromCart(long cartId, long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                ()-> new ResourceNotFoundException("Cart","cartId",cartId)
        );
        CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId,productId);
        if(cartItem == null) {
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        cart.setTotalPrice(cart.getTotalPrice()- cartItem.getProductPrice() * cartItem.getProduct().getQuantity());
        //Product product = cartItem.getProduct();
        //product.setQuantity(product.getQuantity()+cartItem.getProduct().getQuantity());
        cartItemRepository.deleteCartItemsByProductIdAndCartId(cartId, productId);
        return "Product"+ cartItem.getProduct().getProductName()+" deleted successfully";

    }
}
