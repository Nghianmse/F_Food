package com.example.f_food.Entity;

public class CartItem {
    private Food food;
    private int quantity;

    public CartItem(Food food, int quantity) {
        this.food = food;
        this.quantity = quantity;
    }

    public Food getProduct() { return food; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
