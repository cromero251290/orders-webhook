package com.romertec.webook.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "menu", schema = "s75ucl6b156urww6", catalog = "")
public class MenuEntity {
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "price")
    private String price;

    @ManyToOne
    @JoinColumn(name = "restaurantId", referencedColumnName = "id")
    private RestaurantsEntity restaurants;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public RestaurantsEntity getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(RestaurantsEntity restaurants) {
        this.restaurants = restaurants;
    }


}
