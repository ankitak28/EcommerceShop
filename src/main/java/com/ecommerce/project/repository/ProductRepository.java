package com.ecommerce.project.repository;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //JPA creates a function that return products of the same category in ascending order of the price
    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageDetails);

    // JPA creates a function that takes a keyword and return products which has the keyword ignoring the case of letters
    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}