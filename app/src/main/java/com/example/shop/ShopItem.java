package com.example.shop;



public class ShopItem {
    private String productName;
    private String productPrice;
    private String productDetails;
    private int ImageSource;

    public ShopItem(String productName, String productPrice, String productDetails, int imageSource) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDetails = productDetails;
        ImageSource = imageSource;
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
}
