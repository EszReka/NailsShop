package com.example.shop;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ShopFragment extends Fragment {
    private static final String LOG_KEY = ShopFragment.class.getName();
    private List<ShopItem> itemObjList;
    private RecyclerView homeRecyclerView;
    private ShopItemAdapter shopItemAdapter;
    private FirebaseFirestore firestore;
    private CollectionReference mItemsC;
    private SearchView searchView;

    public ShopFragment() {
        //musthave
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        itemObjList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        mItemsC = firestore.collection("Items");
        //generateShopItems();
        //generateonSaleShopItems();
        searchView = rootView.findViewById(R.id.SearchView);
        searchView.clearFocus();
        rootView.findViewById(R.id.price_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               filterFirebase("onSale");
            }
        });
        rootView.findViewById(R.id.abc_button).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filterFirebase("abc");
            }
        });
        homeRecyclerView = rootView.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        queryData();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               filterList(newText);
                return false;
            }
        });
        shopItemAdapter = new ShopItemAdapter((ArrayList<ShopItem>) itemObjList);
        homeRecyclerView.setAdapter(shopItemAdapter);
        return rootView;
    }

    private void filterList(String text) {
        ArrayList<ShopItem> filteredList = new ArrayList<>();
        for (ShopItem item : itemObjList){
            if(item.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
        }
        if (!filteredList.isEmpty()) {
            shopItemAdapter.setFilteredList(filteredList);
         }
    }

    private void queryData(){
        itemObjList.clear();
        mItemsC.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Log.d(LOG_KEY,doc.getId() + "=>" + doc.getData());
                        ShopItem shopItem = doc.toObject(ShopItem.class);
                        itemObjList.add(shopItem);
                        Log.d(LOG_KEY,shopItem.getProductName() + "=>" + shopItem.getProductDetails());
                    }
                }else {
                    Log.d(LOG_KEY, "Error getting document: ", task.getException());
                }
                shopItemAdapter.notifyDataSetChanged();
            }
        });
}

private void filterFirebase(String filterText){
    if(filterText.equals("abc")){
        itemObjList.clear();
        mItemsC.orderBy("productName").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ShopItem shopItem = doc.toObject(ShopItem.class);
                        itemObjList.add(shopItem);
                    }
                }else {
                    Log.d(LOG_KEY, "Error getting document: ", task.getException());
                }
                shopItemAdapter.notifyDataSetChanged();
            }
        });
    }else if(filterText.equals("onSale")){
        itemObjList.clear();
        mItemsC.whereEqualTo("onsale", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        ShopItem shopItem = doc.toObject(ShopItem.class);
                        itemObjList.add(shopItem);
                    }
                }else {
                    Log.d(LOG_KEY, "Error getting document: ", task.getException());
                }
                shopItemAdapter.notifyDataSetChanged();
            }
        });
    }
    }


    private void generateShopItems() {
        Log.e(LOG_KEY, "generateShopItems: generating shop items");
        String[] itemList = getResources().getStringArray(R.array.items_name);
        String[] itemsInfo = getResources().getStringArray(R.array.items_description);
        String[] itemsPrice = getResources().getStringArray(R.array.items_price);
        TypedArray itemsPictures = getResources().obtainTypedArray(R.array.items_picture);

        for (int x = 0; x < itemList.length; x++) {
            mItemsC.add(new ShopItem(
                    itemList[x],
                    itemsPrice[x],
                    itemsInfo[x],
                    itemsPictures.getResourceId(x, 0),
                    false
            ));
        }
    }
    private void generateonSaleShopItems() {
        Log.e(LOG_KEY, "generateShopItems: generating shop items");
        String[] SitemList = getResources().getStringArray(R.array.Sitems_name);
        String[] SitemsInfo = getResources().getStringArray(R.array.Sitems_description);
        String[] SitemsPrice = getResources().getStringArray(R.array.Sitems_price);
        TypedArray SitemsPictures = getResources().obtainTypedArray(R.array.Sitems_picture);

        for (int x = 0; x < SitemList.length; x++) {
            mItemsC.add(new ShopItem(
                    SitemList[x],
                    SitemsPrice[x],
                    SitemsInfo[x],
                    SitemsPictures.getResourceId(x, 0),
                    true
            ));
        }
    }
}