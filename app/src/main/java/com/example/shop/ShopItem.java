package com.example.shop;



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
