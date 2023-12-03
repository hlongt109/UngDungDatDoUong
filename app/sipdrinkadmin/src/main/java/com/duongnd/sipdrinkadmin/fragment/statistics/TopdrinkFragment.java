package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.graphics.Color;
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
import com.duongnd.sipdrinkadmin.databinding.FragmentTopdrinkBinding;
import com.duongnd.sipdrinkadmin.model.DrinkDataOnBarChart;
import com.duongnd.sipdrinkadmin.model.DrinkTop;
import com.duongnd.sipdrinkadmin.model.Order;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TopdrinkFragment extends Fragment {
    public TopdrinkFragment() {
    }

    private FragmentTopdrinkBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopdrinkBinding.inflate(inflater, container, false);
        binding.tvViewDetails.setOnClickListener(view -> {
            replaceFrg(new DetailsTopDrinkFragment());
        });
        getDrinkChart();
        return binding.getRoot();
    }
    public void replaceFrg(Fragment frg) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, frg).commit();
    }private void getDrinkChart() {
        ArrayList<DrinkDataOnBarChart>list = new ArrayList<>();
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
                                int orderTotalQuantity = 0;
                                String nameProduct = "";
                                for (DataSnapshot dataSnapshotDetails : snapshotDetails.getChildren()) {
                                    OrderDetails orderDetails = dataSnapshotDetails.getValue(OrderDetails.class);

                                    if (orderDetails != null) {
                                        nameProduct = orderDetails.getNameProduct();
                                        int quantity = orderDetails.getQuantity();
                                        //
                                        orderTotalQuantity += quantity;
                                    }
                                }
                                list.add(new DrinkDataOnBarChart(nameProduct,orderTotalQuantity));
                                displayDataOnBarChart(list);
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

    }
    private void displayDataOnBarChart(ArrayList<DrinkDataOnBarChart> listDrinkData) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < listDrinkData.size(); i++) {
            DrinkDataOnBarChart ddobc = listDrinkData.get(i);
            entries.add(new BarEntry(i, ddobc.getTotalQuantity()));
        }

        BarDataSet barDataSet = new BarDataSet(entries, "Thống kê số lượng bán");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        binding.barChart.setFitBars(true);
        binding.barChart.setData(barData);
        binding.barChart.getDescription().setText("");
        binding.barChart.animateY(1000);
        binding.barChart.getDescription().setTextSize(10f);

        // Enable X-axis labels
        XAxis xAxis = binding.barChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(true); // Enable X-axis labels

        YAxis leftAxis = binding.barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);

        binding.barChart.invalidate();
    }
}