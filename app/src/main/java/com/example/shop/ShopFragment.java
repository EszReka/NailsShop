package com.example.shop;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        queryData();
        //generateShopItems();

        homeRecyclerView = rootView.findViewById(R.id.homeRecyclerView);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        shopItemAdapter = new ShopItemAdapter((ArrayList<ShopItem>) itemObjList);
        homeRecyclerView.setAdapter(shopItemAdapter);
        return rootView;
    }
private void queryData(){
        itemObjList.clear();

        mItemsC.orderBy("name").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for ( QueryDocumentSnapshot doc : queryDocumentSnapshots){
                ShopItem item = doc.toObject(ShopItem.class);
                itemObjList.add(item);
            }
            shopItemAdapter.notifyDataSetChanged();
        });

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
                    itemsPictures.getResourceId(x, 0)
            ));
        }
    }
}