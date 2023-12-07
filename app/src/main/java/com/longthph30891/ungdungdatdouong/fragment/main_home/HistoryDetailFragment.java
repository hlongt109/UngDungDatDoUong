package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.adapter.HistoryDetailAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentHistoryDetailBinding;
import com.longthph30891.ungdungdatdouong.model.OrderDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
public class HistoryDetailFragment extends Fragment {

    private FragmentHistoryDetailBinding binding;
    private DatabaseReference databaseReference;
    private HistoryDetailAdapter adapter;
    private ArrayList<OrderDetail> list = new ArrayList<>();
    private String orderId = "", idUser = "",dateOrder = "",nameCustomer = "",phoneNumber = "",address = "",statusOrder ="";
    private double totalPrice = 0.0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            orderId = getArguments().getString("orderId");
            idUser = getArguments().getString("idUser");
            dateOrder = getArguments().getString("dateOrder");
            nameCustomer = getArguments().getString("nameCustomer");
            phoneNumber = getArguments().getString("phoneNumber");
            address = getArguments().getString("address");
            statusOrder = getArguments().getString("statusOrder");
            totalPrice = getArguments().getDouble("totalPrice");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryDetailBinding.inflate(inflater,container,false);
        binding.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        setData();
        return binding.getRoot();
    }
    private void setData() {
        binding.tvTime.setText(dateOrder);
        binding.tvTenKh.setText(nameCustomer);
        binding.tvSdt.setText(phoneNumber);
        binding.tvDiaChi.setText(address);

        databaseReference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                int totalQuantity = 0;
                double totalMoney = 0.0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderDetail orderDetails = dataSnapshot.getValue(OrderDetail.class);

                    if (orderDetails != null && orderDetails.getOrderId().equals(orderId)) {
                        list.add(orderDetails);
                        totalQuantity += orderDetails.getQuantity();
                        totalMoney += orderDetails.getPrice() * orderDetails.getQuantity();
                    }
                }
                Locale locale = new Locale("vi","VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String tongTien = numberFormat.format(totalMoney);
                binding.tvTotalQuantity.setText(totalQuantity +" sản phẩm");
                binding.tvTotalPrice.setText(tongTien);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new HistoryDetailAdapter(list,getActivity(),databaseReference);
        binding.rcvDrinkOder.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvDrinkOder.setAdapter(adapter);
        //
    }
}