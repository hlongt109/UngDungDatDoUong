package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.duongnd.sipdrinkadmin.adapter.OrderDrinkAdapter;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetDeliveringListBinding;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BottomSheetDeliveringList extends BottomSheetDialogFragment {
    private BottomSheetDeliveringListBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<Order> list = new ArrayList<>();
    private OrderDrinkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDeliveringListBinding.inflate(inflater, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order1 = dataSnapshot.getValue(Order.class);
                    if (order1 != null && order1.getStatusOrder().equals("danggiao")) {
                        list.add(order1);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new OrderDrinkAdapter(list,getActivity(),databaseReference);
        binding.rcvDeliveringList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvDeliveringList.setAdapter(adapter);

        return binding.getRoot();
    }
}
