package com.example.oneonesam.controller;

import com.example.oneonesam.model.*;
import com.example.oneonesam.repository.CartItemRepository;
import com.example.oneonesam.repository.ProductRepository;
import com.example.oneonesam.repository.ReviewRepository;
import com.example.oneonesam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    public User createUserWithCart(User user) {
        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        return userRepository.save(user);
    }
    @PostMapping("/add")
    public ResponseEntity<User> addUser(@RequestBody User newUser){
        User savedUser = createUserWithCart(newUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") Long userId){
        Optional<User> existingUser = userRepository.findById(userId);

        if(existingUser.isPresent()) {
            userRepository.delete(existingUser.get());
            return ResponseEntity.ok("deleted");
        }
        else {
            return ResponseEntity.ok("Not found");
        }
    }

    @PostMapping("/addCart/{userId}/{id}")
    public ResponseEntity<CartItem> addToCart(@PathVariable(name = "id") Long productId, @PathVariable Long userId){
        Optional<Product> existingProduct = productRepository.findById(productId);

        Optional<User> existingUser = userRepository.findById(userId);
        Cart cartAssociatedWithUser = existingUser.get().getCart();

        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cartAssociatedWithUser);
        newCartItem.setProduct(existingProduct.get());

         if(existingProduct.isPresent()){
             CartItem savedCart = cartItemRepository.save(newCartItem);
             return ResponseEntity.status(HttpStatus.CREATED).body(savedCart);
         }else {
             return ResponseEntity.status(HttpStatus.OK).body(null);
         }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product newProduct){
        Product savedProduct = productRepository.save(newProduct);

        if(savedProduct != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
        }else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @DeleteMapping("/removeProduct/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable(name = "id") Long productId){
        Optional<Product> productFound = productRepository.findById(productId);

        if(productFound.isPresent()){
            productRepository.delete(productFound.get());
            return ResponseEntity.status(HttpStatus.OK).body("product deleted successfully");
        }else{
            return ResponseEntity.status(HttpStatus.OK).body("product not found.");
        }
    }

    @PostMapping("/review/{userId}/{id}")
    public ResponseEntity<Review> addReview(@RequestBody Review newReview ,@PathVariable Long userId, @PathVariable(name = "id") Long productId){
        Optional<User> existingUser = userRepository.findById(userId);
        Optional<Product> existingProduct = productRepository.findById(productId);

        newReview.setUser(existingUser.get());
        newReview.setProduct(existingProduct.get());
        Review savedReview = reviewRepository.save(newReview);

        if(savedReview != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
        }else{
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }
}
