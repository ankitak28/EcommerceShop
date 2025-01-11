package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service //this annotation tells to manage this component as a bean and inject it in the controller
public interface CategoryService {

    //Service is required to achieve loose coupling

    List<Category> getAllCategories();

    void createCategory(Category category);

    String deleteCategory(long id);

    Category updateCategory(Category category, Long id);
}
