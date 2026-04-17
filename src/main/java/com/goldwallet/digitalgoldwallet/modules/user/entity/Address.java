package com.goldwallet.digitalgoldwallet.modules.user.entity;

import jakarta.persistence.*;
import lombok.*;


import jakarta.validation.constraints.*;


@Entity
@Table(name = "addresses")
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

    public Address() {
    }

    public Address(Long addressId, String street, String city, String state, String postalCode, String country) {
        this.addressId = addressId;
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}