package com.romertec.webook.model.bestbuy;

import java.util.ArrayList;

public class Payload {
    private String filename;
    private Parsed parsed;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Parsed getParsed() {
        return parsed;
    }

    public void setParsed(Parsed parsed) {
        this.parsed = parsed;
    }
}
