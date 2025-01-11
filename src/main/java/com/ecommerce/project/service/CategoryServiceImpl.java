package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categories.add(category);
    }

    @Override
    public String deleteCategory(long categoryId) {
        Category category = categories.stream().filter(s ->
                s.getCategoryId().equals(categoryId)).findFirst().orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(category == null)
            return "Category does not exist";
        categories.remove(category);
        return "Category deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        Optional<Category> optionalCategory = categories.stream().
                filter(s ->s.getCategoryId().equals(id)).findFirst();

        if (optionalCategory.isPresent()) {
            Category existingCategory = optionalCategory.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }
}
