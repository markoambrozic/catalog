package com.kumuluz.ee.samples.microservices.simple.Models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    private double price;

    private String ean;

    private String additionalInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEan(String ean) { this.ean = ean; }

    public String getEan() { return this.ean; }

    public void setAdditionalInfo(String additionalInfo) { this.additionalInfo = additionalInfo; }

    public String getAdditionalInfo() { return this.additionalInfo; }
}
