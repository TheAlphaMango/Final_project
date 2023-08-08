package com.example.finalproject;

public class Image {
    /**
     * Image data to save and load
     */
    private String date;
    private String copyright;
    private String description;

    /**
     * Sets image data
     */
    public Image(String date, String copyright, String description) {
        setDate(date);
        setCopyright(copyright);
        setDescription(description);
    }

    /**
     * Setters and getters
     */
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getCopyright() {
        return copyright;
    }
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
