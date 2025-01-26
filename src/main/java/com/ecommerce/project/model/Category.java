package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long categoryId;
    @NotBlank
    @Size(min = 5, message = "Category name must contain atleast 5 characters.")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    List<Product> products;

}
