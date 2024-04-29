package com.example.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder>{
    private ArrayList<ShopItem> shopItemsList;
    ShopItemAdapter(ArrayList<ShopItem> itemsList){
        this.shopItemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder( ShopItemAdapter.ViewHolder holder, int position) {
            ShopItem currentItem = shopItemsList.get(position);
            holder.productName.setText(currentItem.getProductName());
            holder.productDetails.setText(currentItem.getProductDetails());
            holder.productPrice.setText(currentItem.getProductPrice());
            holder.productImage.setImageResource(currentItem.getImageSource());
    }

    @Override
    public int getItemCount() {
        return shopItemsList.size();
    }

    public void setFilteredList(ArrayList<ShopItem> filteredList) {
        this.shopItemsList = filteredList;
        notifyDataSetChanged();
    }


    public static  class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productDetails;
        private ImageView productImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDetails = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);

            itemView.findViewById(R.id.put_to_cart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //add to cart
                }
            });
        }
    }

}

