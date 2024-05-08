package com.example.shop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {
    private ArrayList<ShopItem> shopItemsList;

    ShopItemAdapter(ArrayList<ShopItem> itemsList) {
        this.shopItemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShopItemAdapter.ViewHolder holder, int position) {
        ShopItem currentItem = shopItemsList.get(position);
        holder.productName.setText(currentItem.getProductName());
        holder.productDetails.setText(currentItem.getProductDetails());
        holder.productPrice.setText(currentItem.getProductPrice());
        holder.productImage.setImageResource(currentItem.getImageSource());
        holder.puttocart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addToCart(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopItemsList.size();
    }

    public void setFilteredList(ArrayList<ShopItem> filteredList) {
        this.shopItemsList = filteredList;
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productDetails;
        private ImageView productImage;
        private Button puttocart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDetails = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            puttocart= itemView.findViewById(R.id.put_to_cart);
        }
    }
    public void addToCart(ShopItem item) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            // Use userEmail for further processing
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference collection = firestore.collection("Users");
            collection.whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                UserData userData = doc.toObject(UserData.class);
                                ArrayList<ShopItem> inCart = userData.getInCart();
                                if (inCart != null) {
                                    inCart.add(item);
                                    doc.getReference().update("inCart", inCart)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Success
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure
                                                }
                                            });

                                }
                            }
                        }
                    }
                }
            });
        } else {
            // User is not signed in
            // Handle this case accordingly
        }
    }
}



