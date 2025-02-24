package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;
    private String pgPaymentId;
    private String pgName;
    private String pgStatus;
    private String pgResponseMessage;
    

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;
    @NotBlank
    @Size(min = 1, message="Payment method must contain atleast 4 characters")
    private String paymentMethod;

    public Payment(String paymentMethod, String pgPaymentId, String pgName, String pgResponseMessage, String pgStatus) {
        this.paymentMethod = paymentMethod;
        this.pgPaymentId = pgPaymentId;
        this.pgName = pgName;
        this.pgResponseMessage = pgResponseMessage;
        this.pgStatus = pgStatus;
    }
}
