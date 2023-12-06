package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.duongnd.sipdrinkadmin.adapter.OrderDrinkAdapter;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetOrderListBinding;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BottomSheetBillsList extends BottomSheetDialogFragment {
    private BottomSheetOrderListBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<Order> list = new ArrayList<>();
    private OrderDrinkAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetOrderListBinding.inflate(inflater,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order1 = dataSnapshot.getValue(Order.class);
                    if (order1 != null && order1.getStatusOrder().equals("dathanhtoan")||order1.getStatusOrder().equals("dahuy")) {
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
        binding.rcvOderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvOderList.setAdapter(adapter);
        return binding.getRoot();
    }
}
