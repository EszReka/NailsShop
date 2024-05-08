package com.example.shop;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class UserData {
    private String email;
    private  String username;
    private ArrayList<ShopItem> inCart;

    public UserData(){}
    public UserData(String email, String username, ArrayList<ShopItem> inCart) {
        this.email = email;
        this.username = username;
        this.inCart = inCart;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<ShopItem> getInCart() {
        return inCart;
    }

    public void setInCart(ArrayList<ShopItem> inCart) {
        this.inCart = inCart;
    }
}
