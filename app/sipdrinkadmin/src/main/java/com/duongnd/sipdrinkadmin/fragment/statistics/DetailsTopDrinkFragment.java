package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.DrinkTopAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentDrinksListBinding;
import com.duongnd.sipdrinkadmin.databinding.FragmentStatisticsTopDrinkBinding;
import com.duongnd.sipdrinkadmin.fragment.StatisticFragment;
import com.duongnd.sipdrinkadmin.model.DrinkTop;
import com.duongnd.sipdrinkadmin.model.Order;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.s;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DetailsTopDrinkFragment extends Fragment {
    public DetailsTopDrinkFragment() {
    }

    private FragmentStatisticsTopDrinkBinding binding;
    private DatabaseReference databaseReference;
    private ArrayList<DrinkTop> list = new ArrayList<>();
    private DrinkTopAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticsTopDrinkBinding.inflate(inflater, container, false);
        getDrinkRevenue();
        binding.btnBack.setOnClickListener(view -> {
            replaceFrg(new StatisticFragment());
        });
        return binding.getRoot();
    }

    private void getDrinkRevenue() {
        DatabaseReference referenceOrder = FirebaseDatabase.getInstance().getReference("Order");
        DatabaseReference referenceDetails = FirebaseDatabase.getInstance().getReference("OrderDetails");

        referenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    String orderId;
                    if (order.getStatusOrder().equals("dathanhtoan")) {
                        orderId = order.getOrderId();
                        referenceDetails.orderByChild("orderId").equalTo(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshotDetails) {
                                for (DataSnapshot dataSnapshotDetails : snapshotDetails.getChildren()) {
                                    OrderDetails orderDetails = dataSnapshotDetails.getValue(OrderDetails.class);

                                    if (orderDetails != null) {
                                        String productName = orderDetails.getNameProduct();
                                        int quantity = orderDetails.getQuantity();
                                        double price = orderDetails.getPrice();

                                        // Kiểm tra xem sản phẩm đã tồn tại trong danh sách chưa
                                        boolean productExists = false;
                                        for (DrinkTop existingProduct : list) {
                                            if (existingProduct.getName().equals(productName)) {
                                                // Nếu đã tồn tại, cập nhật thông tin sản phẩm
                                                existingProduct.setQuantity(existingProduct.getQuantity() + quantity);
                                                existingProduct.setTotalRevenue(existingProduct.getTotalRevenue() + quantity * price);
                                                productExists = true;
                                                break;
                                            }
                                        }

                                        // Nếu sản phẩm chưa tồn tại, thêm sản phẩm mới vào danh sách
                                        if (!productExists) {
                                            String imageProduct = orderDetails.getImageProduct();
                                            DrinkTop newProduct = new DrinkTop(productName, quantity, quantity * price, imageProduct);
                                            list.add(newProduct);
                                        }
                                    }
                                }
                                Collections.sort(list, new Comparator<DrinkTop>() {
                                    @Override
                                    public int compare(DrinkTop o1, DrinkTop o2) {
                                        return Integer.compare(o2.getQuantity(), o1.getQuantity());
                                    }
                                });
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        adapter = new DrinkTopAdapter(list, getActivity(), databaseReference);
        binding.rcvTopDrink.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvTopDrink.setAdapter(adapter);
    }
    public void replaceFrg(Fragment frg) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, frg).commit();
    }
}