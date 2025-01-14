package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "categories")
public class Category {

    @Id
    private Long categoryId;
    @NotBlank
    @Size(min = 5, message = "Category name must contain atleast 5 characters.")
    private String categoryName;

}
