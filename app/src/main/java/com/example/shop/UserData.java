package com.example.shop;

import java.util.ArrayList;

public class UserData {
    private String email;
    private  String username;
    private ArrayList<ShopItem> inCartList;
    private int ProfilePictureSource;

    public UserData(String email, String username, ArrayList<ShopItem> inCartList, int profilePictureSource) {
        this.email = email;
        this.username = username;
        this.inCartList = inCartList;
        ProfilePictureSource = profilePictureSource;
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

    public ArrayList<ShopItem> getInCartList() {
        return inCartList;
    }

    public void setInCartList(ArrayList<ShopItem> inCartList) {
        this.inCartList = inCartList;
    }

    public int getProfilePictureSource() {
        return ProfilePictureSource;
    }

    public void setProfilePictureSource(int profilePictureSource) {
        ProfilePictureSource = profilePictureSource;
    }
}
