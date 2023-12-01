package com.duongnd.sipdrinkadmin.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.NotificationAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentNotificationBinding;
import com.duongnd.sipdrinkadmin.model.Notification;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    public NotificationFragment() {}
    private NotificationAdapter adapter;
    private ArrayList<Order> list = new ArrayList<>();
    private FragmentNotificationBinding binding;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    if(order != null && order.getStatusOrder().equals("choxacnhan")){
                        list.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new NotificationAdapter(list,getActivity(),databaseReference);
        binding.rcvNotification.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvNotification.setAdapter(adapter);
        return binding.getRoot();
    }
}