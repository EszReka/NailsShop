package com.example.shop;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {
    private ArrayList<ShopItem> shopItemsList;

    CartItemAdapter(ArrayList<ShopItem> itemsList) {
        this.shopItemsList = itemsList;
    }

    public void updateDataSet(ArrayList<ShopItem> updatedList) {
        this.shopItemsList = updatedList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.incart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CartItemAdapter.ViewHolder holder, int position) {
        ShopItem currentItem = shopItemsList.get(position);
        holder.productName.setText(currentItem.getProductName());
        holder.productDetails.setText(currentItem.getProductDetails());
        holder.productPrice.setText(currentItem.getProductPrice());
        holder.productImage.setImageResource(currentItem.getImageSource());
        holder.deletefromcart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deletefromcart(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopItemsList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView productName;
        private TextView productPrice;
        private TextView productDetails;
        private ImageView productImage;
        private Button deletefromcart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productDetails = itemView.findViewById(R.id.product_description);
            productImage = itemView.findViewById(R.id.product_image);
            deletefromcart = itemView.findViewById(R.id.delete_fromcart);
        }
    }
    public void deletefromcart(ShopItem item) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userEmail = user.getEmail();
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference collection = firestore.collection("Users");
            collection.whereEqualTo("email", userEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                doc.getReference().update("inCart", FieldValue.arrayRemove(item));
                            }
                            shopItemsList.remove(item);
                            notifyDataSetChanged();
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



