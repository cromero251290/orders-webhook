package com.romertec.webook.model;

public class AbebooksRequest {

    private String inbox_id;
    private String doc_id;
    private String event;
    private Payload payload;

    public String getInbox_id() {
        return inbox_id;
    }

    public void setInbox_id(String inbox_id) {
        this.inbox_id = inbox_id;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
