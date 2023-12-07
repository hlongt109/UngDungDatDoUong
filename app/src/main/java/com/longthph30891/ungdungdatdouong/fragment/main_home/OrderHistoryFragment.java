package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.adapter.OrderHistoryAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentOrderHistoryBinding;
import com.longthph30891.ungdungdatdouong.model.Order;

import java.util.ArrayList;
import java.util.Objects;


public class OrderHistoryFragment extends Fragment {

    FragmentOrderHistoryBinding binding;
    private DatabaseReference databaseReference;
    FirebaseDatabase database;
    private ArrayList<Order> list = new ArrayList<>();
    private OrderHistoryAdapter adapter;
    FirebaseAuth auth;
    String curUserId;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        curUserId = Objects.requireNonNull(auth.getCurrentUser()).getUid();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater,container,false);
//        FirebaseUser user = auth.getCurrentUser();


        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order1 = dataSnapshot.getValue(Order.class);
                    if (order1 != null && order1.getIdUser().equals(curUserId)) {
                        list.add(order1);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new OrderHistoryAdapter(list,getActivity(),databaseReference);
        binding.rcvOderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvOderList.setAdapter(adapter);
        return binding.getRoot();
    }
}