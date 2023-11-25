package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.duongnd.sipdrinkadmin.adapter.OrderAdapter;
import com.duongnd.sipdrinkadmin.databinding.BottomSheetOrderListBinding;
import com.duongnd.sipdrinkadmin.model.DonHang;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BottomSheetOrderList extends BottomSheetDialogFragment {
    private BottomSheetOrderListBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<DonHang>listDonHang = new ArrayList<>();
    private OrderAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetOrderListBinding.inflate(inflater,container,false);
        databaseReference = FirebaseDatabase.getInstance().getReference("DonHang");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listDonHang.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    DonHang donHang = dataSnapshot.getValue(DonHang.class);
                    listDonHang.add(donHang);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new OrderAdapter(listDonHang,getActivity());
        binding.rcvOderList.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvOderList.setAdapter(adapter);
        return binding.getRoot();
    }
}
