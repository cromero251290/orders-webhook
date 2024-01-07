package com.romertec.webook.model.csv;

import java.util.ArrayList;

public class Parsed {
    private String city;
    private String estimated_delivery;
    private String item_description;
    private String item_price;
    private String name;
    private String order_number;
    private String state_code;
    private String street;
    private String zip;
    private String _original_recipient_;
    private ArrayList<String> resource_url;
    private String email;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEstimated_delivery() {
        return estimated_delivery;
    }

    public void setEstimated_delivery(String estimated_delivery) {
        this.estimated_delivery = estimated_delivery;
    }

    public String getItem_description() {
        return item_description;
    }

    public void setItem_description(String item_description) {
        this.item_description = item_description;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String get_original_recipient_() {
        return _original_recipient_;
    }

    public void set_original_recipient_(String _original_recipient_) {
        this._original_recipient_ = _original_recipient_;
    }

    public ArrayList<String> getResource_url() {
        return resource_url;
    }

    public void setResource_url(ArrayList<String> resource_url) {
        this.resource_url = resource_url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
