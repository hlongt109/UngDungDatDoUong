package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentTopdrinkBinding;
import com.duongnd.sipdrinkadmin.databinding.FragmentTotalRevenueBinding;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.Locale;

public class TotalRevenueFragment extends Fragment {
    public TotalRevenueFragment() {}
    FragmentTotalRevenueBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTotalRevenueBinding.inflate(inflater,container,false);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.orderByChild("statusOrder").equalTo("dathanhtoan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRevenue = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    if("dathanhtoan".equals(order.getStatusOrder())){
                        totalRevenue += order.getTotalPrice();
                    }
                }
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String tongTien = numberFormat.format(totalRevenue);
                binding.tvTotalRevenue.setText(tongTien);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}