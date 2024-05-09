package com.example.shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
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
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
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

    @Override
    protected void onDestroy() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference userCol = firestore.collection("Users");

        userCol.whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UserData userData = doc.toObject(UserData.class);
                            ArrayList<ShopItem> array = userData.getInCart();
                            if(!array.isEmpty()){
                                schecduleNotification(getNotification("A kosaradban maradt termék."),5000);
                            }
                        }
                    }
                }
            }
        });
        super.onDestroy();
    }
    private void schecduleNotification(Notification notification, int delay){
        Intent notifIntent = new Intent(this,NotificationBroadcast.class);
        notifIntent.putExtra(NotificationBroadcast.NOTIFICATIONID,1);
        notifIntent.putExtra(NotificationBroadcast.NOTIFICATION,notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,notifIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        long futureMils = SystemClock.elapsedRealtime()+delay;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMils,pendingIntent);
    }
    private Notification getNotification(String content){
        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(this, default_notification_id);

        nbuilder.setContentTitle("Maradj még!");
        nbuilder.setContentText(content);
        nbuilder.setSmallIcon(R.drawable.vector_nailpolish);
        nbuilder.setAutoCancel(true);
        nbuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return nbuilder.build();
    }
}