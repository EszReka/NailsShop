package com.example.shop;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CartFragment extends Fragment {
    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";
    FirebaseFirestore firestore;
    CollectionReference userCol;
    ArrayList<ShopItem> inCart;
    private RecyclerView cartRecyclerView;
    private CartItemAdapter cartItemAdapter;
    private Button pay_button;
    public CartFragment() {    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        userCol = firestore.collection("Users");

        userCol.whereEqualTo("email",user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UserData userData = doc.toObject(UserData.class);
                            ArrayList<ShopItem> array = userData.getInCart();
                            if(array != null){
                                inCart = array;
                                cartRecyclerView = rootView.findViewById(R.id.cartRecycleView);
                                cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                cartItemAdapter = new CartItemAdapter((ArrayList<ShopItem>) inCart);
                                cartRecyclerView.setAdapter(cartItemAdapter);
                            } else {
                                inCart = new ArrayList<ShopItem>();
                            }
                        }
                    }
                }
            }
        });
        pay_button = rootView.findViewById(R.id.payBTN);
        pay_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
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
                                        UserData userData = doc.toObject(UserData.class);
                                        userData.setInCart(new ArrayList<>());
                                            inCart = new ArrayList<>();
                                            doc.getReference().update("inCart", inCart);
                                            cartRecyclerView = rootView.findViewById(R.id.cartRecycleView);
                                            cartRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                            cartItemAdapter = new CartItemAdapter((ArrayList<ShopItem>) inCart);
                                            cartRecyclerView.setAdapter(cartItemAdapter);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    // User is not signed in
                }
                if(!inCart.isEmpty()) {
                    schecduleNotification(getNotification("Köszönjük, hogy nálunk vásárolt!"), 5000);
                }
            }
        });


        return rootView;
    }

    private void schecduleNotification(Notification notification, int delay){
        Intent notifIntent = new Intent(getContext(),NotificationBroadcast.class);
        notifIntent.putExtra(NotificationBroadcast.NOTIFICATIONID,1);
        notifIntent.putExtra(NotificationBroadcast.NOTIFICATION,notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,notifIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        long futureMils = SystemClock.elapsedRealtime()+delay;
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMils,pendingIntent);
    }
    private Notification getNotification(String content){
        NotificationCompat.Builder nbuilder = new NotificationCompat.Builder(getActivity(), default_notification_id);

        nbuilder.setContentTitle("Sikeres fizetés");
        nbuilder.setContentText(content);
        nbuilder.setSmallIcon(R.drawable.vector_nailpolish);
        nbuilder.setAutoCancel(true);
        nbuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return nbuilder.build();
    }
}