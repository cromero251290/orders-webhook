package com.romertec.webook.model.bestbuy;

import java.util.ArrayList;

public class Parsed {

    private String model_number;
    private String name;
    private String order_number;
    private String product_description;
    private String product_price;
    private String sku;
    private String state_code;

    private String city;
    private String street;
    private String zip;
    private String _original_recipient_;
    private String _to_;
    private ArrayList<String> resource_url;

    public String getModel_number() {
        return model_number;
    }

    public void setModel_number(String model_number) {
        this.model_number = model_number;
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

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public String get_to_() {
        return _to_;
    }

    public void set_to_(String _to_) {
        this._to_ = _to_;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getResource_url() {
        return resource_url;
    }

    public void setResource_url(ArrayList<String> resource_url) {
        this.resource_url = resource_url;
    }
}
