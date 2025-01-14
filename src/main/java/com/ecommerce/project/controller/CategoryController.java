package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    /*
    * A constructor is being used in order to initialize the service
    * Annotation @Autowired can also be used to achieve the same behaviour
    */
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }

    @GetMapping("/public/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories,HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<String> addCategory(@Valid @RequestBody Category category) {

        categoryService.createCategory(category);
        return new ResponseEntity<>("category created",HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable int categoryId) {

            String status = categoryService.deleteCategory(categoryId);

            /*
                * ResponseEntity is used to communicate with a proper message and status code to the client.
                * ResponseEntity can be used in various ways to return status code and message.
             */
            return ResponseEntity.status(HttpStatus.OK).body(status);

    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@Valid @RequestBody Category category, @PathVariable Long categoryId) {

            Category savedCategory = categoryService.updateCategory(category,categoryId);
            return new ResponseEntity<>("category updated", HttpStatus.OK);

    }
}
