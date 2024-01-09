package com.romertec.webook.model.walmart;

public class Parsed {

    private String name;
    private String order_date;
    private String order_number;
    private String product_description;
    private String product_price;
    private String _original_recipient_;

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

    public String get_original_recipient_() {
        return _original_recipient_;
    }

    public void set_original_recipient_(String _original_recipient_) {
        this._original_recipient_ = _original_recipient_;
    }
}
