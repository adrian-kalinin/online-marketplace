package com.example.marketplace.model;

import javax.persistence.*;

@Entity
@Table(name = "marketplace_products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "location", nullable = false, length = 120)
    private String location;

}
