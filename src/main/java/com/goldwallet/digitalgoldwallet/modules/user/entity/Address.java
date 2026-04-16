package com.goldwallet.digitalgoldwallet.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotBlank(message = "Street cannot be empty")
    @Size(max = 255, message = "Street must be less than 255 characters")
    @Column(nullable = false)
    private String street;

    @NotBlank(message = "City cannot be empty")
    @Size(max = 100, message = "City must be less than 100 characters")
    @Column(nullable = false, length = 100)
    private String city;

    @NotBlank(message = "State cannot be empty")
    @Size(max = 100, message = "State must be less than 100 characters")
    @Column(nullable = false, length = 100)
    private String state;

    @Pattern(regexp = "^[a-zA-Z0-9\\- ]{4,20}$", message = "Invalid postal code")
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @NotBlank(message = "Country cannot be empty")
    @Size(max = 100, message = "Country must be less than 100 characters")
    @Column(nullable = false, length = 100)
    private String country;
}