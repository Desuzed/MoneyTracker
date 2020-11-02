package com.example.moneytracker;

public class ItemRecord {
    private final String title;
    private final int price;
    String comment;


    public ItemRecord(String title, int price) {
        this.title = title;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public String getComment() {
        return comment;
    }
}
