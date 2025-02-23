package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @NotBlank
    @Size(min = 5, max = 50,message = "Street name must be atleast 5 chars")
    private String street;
    @NotBlank
    @Size(min=5, message = "Building name must be atleast 5 chars")
    private String buildingName;
    @NotBlank
    @Size(min=4,message = "City name must be atleast 4 chars")
    private String city;
    @Size(min=4,message = "State  must be atleast 4 chars")
    private String state;
    @Size(min=2,message = "Country must be atleast 2 chars")
    private String country;
    @Size(min=6,message = "pincode must be atleast 6 chars")
    private String pincode;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
