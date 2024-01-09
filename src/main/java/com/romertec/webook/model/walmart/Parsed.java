package com.romertec.webook.model.walmart;

public class Parsed {

    private String name;
    private String order_date;
    private String order_number;
    private String item_canceled_description;
    private String temporary_hold_ammount;
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

    public String getItem_canceled_description() {
        return item_canceled_description;
    }

    public void setItem_canceled_description(String item_canceled_description) {
        this.item_canceled_description = item_canceled_description;
    }

    public String getTemporary_hold_ammount() {
        return temporary_hold_ammount;
    }

    public void setTemporary_hold_ammount(String temporary_hold_ammount) {
        this.temporary_hold_ammount = temporary_hold_ammount;
    }

    public String get_original_recipient_() {
        return _original_recipient_;
    }

    public void set_original_recipient_(String _original_recipient_) {
        this._original_recipient_ = _original_recipient_;
    }
}
