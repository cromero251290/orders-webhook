package com.romertec.webook.entities;

import jakarta.persistence.*;

import java.awt.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "restaurants", schema = "s75ucl6b156urww6", catalog = "")
public class RestaurantsEntity {
    @Basic
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "position")
    private String position;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "score")
    private String score;
    @Basic
    @Column(name = "raitings")
    private String raitings;
    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "price_range")
    private String priceRange;
    @Basic
    @Column(name = "street")
    private String street;
    @Basic
    @Column(name = "unit")
    private String unit;
    @Basic
    @Column(name = "city")
    private String city;
    @Basic
    @Column(name = "state")
    private String state;
    @Basic
    @Column(name = "zip")
    private String zip;
    @Basic
    @Column(name = "lat")
    private String lat;
    @Basic
    @Column(name = "lng")
    private String lng;


    @OneToMany(mappedBy = "restaurants")
    private Set<MenuEntity> menus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getRaitings() {
        return raitings;
    }

    public void setRaitings(String raitings) {
        this.raitings = raitings;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Set<MenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(Set<MenuEntity> menus) {
        this.menus = menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantsEntity that = (RestaurantsEntity) o;
        return id == that.id && Objects.equals(position, that.position) && Objects.equals(name, that.name) && Objects.equals(score, that.score) && Objects.equals(raitings, that.raitings) && Objects.equals(category, that.category) && Objects.equals(priceRange, that.priceRange) && Objects.equals(street, that.street) && Objects.equals(unit, that.unit) && Objects.equals(city, that.city) && Objects.equals(state, that.state) && Objects.equals(zip, that.zip) && Objects.equals(lat, that.lat) && Objects.equals(lng, that.lng);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, position, name, score, raitings, category, priceRange, street, unit, city, state, zip, lat, lng);
    }
}
