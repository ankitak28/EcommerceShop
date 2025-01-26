package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //this annotation tells to manage this component as a bean and inject it in the controller
public interface CategoryService {

    //Service is required to achieve loose coupling

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(long id);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long id);
}
