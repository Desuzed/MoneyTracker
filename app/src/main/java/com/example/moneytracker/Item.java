package com.example.moneytracker;

public class Item {
    public int id;
    public String type;
    public final String name;
    public final int price;

    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_INCOMES = "incomes";
    public static final String TYPE_EXPENSES = "expenses";
    public static final String TYPE_BALANCE = "balance";

    public Item(String name, int price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

}
