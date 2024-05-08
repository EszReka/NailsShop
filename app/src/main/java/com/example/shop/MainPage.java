package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.shop.databinding.ActivityMainBinding;
import com.example.shop.databinding.ActivityMainPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class MainPage extends AppCompatActivity {
    ActivityMainPageBinding binding;
    private FirebaseUser user;
    private FirebaseAuth uAuth;
    private RecyclerView recyclerView;
    private ArrayList<ShopItem> itemList;
    private ShopItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        replaceFragment(new ShopFragment());
        if(user != null){
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.bottomNavBar.setOnItemSelectedListener( item -> {
            if(item.getItemId() == R.id.home) {
                replaceFragment(new ShopFragment());
            }else if (item.getItemId() == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }else if(item.getItemId() == R.id.cart) {
                replaceFragment(new CartFragment());
            }else if (item.getItemId() == R.id.contact){
                replaceFragment(new ContactFragment());
            }return true;});
        } else {
            finish();
        }
    }
    private void replaceFragment(Fragment fr){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fr);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}