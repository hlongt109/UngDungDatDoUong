package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.adapter.NotifyAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentNotificationBinding;
import com.longthph30891.ungdungdatdouong.model.Order;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {
    public NotificationFragment() {
    }

    private FragmentNotificationBinding binding;
    private NotifyAdapter adapter;
    private ArrayList<Order> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    SessionManager sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        sessionManager = new SessionManager(getActivity());
        String idKhachHang = sessionManager.getLoggedInCustomerId();
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    if (order != null && order.getStatusOrder().equals("danggiao") && order.getIdUser().equals(idKhachHang)) {
                        list.add(order);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new NotifyAdapter(list, getActivity(), databaseReference);
        binding.rcvThongBao.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvThongBao.setAdapter(adapter);
        return binding.getRoot();
    }
}