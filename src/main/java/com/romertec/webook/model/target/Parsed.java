package com.romertec.webook.model.target;

import java.util.ArrayList;

public class Parsed {
    private String arriving;
    private String based_on;
    private String city;
    private String delivery;
    private String item_description;
    private String item_price;
    private String name;
    private String order_date;
    private String order_number;
    private String order_total;
    private String payment_four_last_digit;
    private String state;
    private String street;
    private String subtotal;
    private String taxes;
    private String total;
    private String zip;

    private String delivers_to_name;

    private String email;

    private String _original_recipient_;
    private ArrayList<String> resource_url;

    public String getArriving() {
        return arriving;
    }

    public void setArriving(String arriving) {
        this.arriving = arriving;
    }

    public String getBased_on() {
        return based_on;
    }

    public void setBased_on(String based_on) {
        this.based_on = based_on;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
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

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String get_original_recipient_() {
        return _original_recipient_;
    }

    public void set_original_recipient_(String _original_recipient_) {
        this._original_recipient_ = _original_recipient_;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getPayment_four_last_digit() {
        return payment_four_last_digit;
    }

    public void setPayment_four_last_digit(String payment_four_last_digit) {
        this.payment_four_last_digit = payment_four_last_digit;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public String getDelivers_to_name() {
        return delivers_to_name;
    }

    public void setDelivers_to_name(String delivers_to_name) {
        this.delivers_to_name = delivers_to_name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getResource_url() {
        return resource_url;
    }

    public void setResource_url(ArrayList<String> resource_url) {
        this.resource_url = resource_url;
    }
}
