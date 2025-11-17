package org.example;

public class StorageItem {
    private int itemId;
    private String type;
    private int amount;
    private double price;

    public StorageItem(int itemId, String type, int amount, double price) {
        this.itemId = itemId;
        this.type = type;
        this.amount = amount;
        this.price = price;
    }
    public StorageItem() {

    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
