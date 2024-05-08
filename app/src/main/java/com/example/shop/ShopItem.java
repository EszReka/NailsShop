package com.example.shop;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopItem {
    private String productName;
    private String productPrice;
    private String productDetails;
    private int ImageSource;
    private boolean onsale;

    public ShopItem(String productName, String productPrice, String productDetails, int imageSource, boolean onsale) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDetails = productDetails;
        this.ImageSource = imageSource;
        this.onsale = onsale;
    }
    public ShopItem(){}

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
    public String getProductDetails() {
        return productDetails;
    }
    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }
    public int getImageSource() {
        return ImageSource;
    }

    public void setImageSource(int imageSource) {
        ImageSource = imageSource;
    }

    public boolean isOnsale() {
        return onsale;
    }

    public void setOnsale(boolean onsale) {
        this.onsale = onsale;
    }

}
