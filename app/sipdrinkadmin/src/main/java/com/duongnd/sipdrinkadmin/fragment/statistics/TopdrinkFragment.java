package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.duongnd.sipdrinkadmin.databinding.FragmentTopdrinkBinding;
import com.duongnd.sipdrinkadmin.model.DrinkDataOnBarChart;
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
        DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("Order");
        orderReference.orderByChild("statusOrder").equalTo("dathanhtoan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DrinkDataOnBarChart> orderDrinkDataList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order = dataSnapshot.getValue(Order.class);
                    String orderId = order.getOrderId();
                    //truy vấn OrderDetails
                    DatabaseReference orderDetailsReference = FirebaseDatabase.getInstance().getReference("OrderDetails");
                    orderDetailsReference.orderByChild("orderId").equalTo(orderId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                OrderDetails orderDetails = dataSnapshot1.getValue(OrderDetails.class);
                                if (orderDetails != null) {
                                    for (DrinkDataOnBarChart drinkDataOnBarChart : orderDrinkDataList) {
                                        if (drinkDataOnBarChart.getName().equals(orderDetails.getNameProduct())) {
                                            drinkDataOnBarChart.addQuantity(orderDetails.getQuantity());

                                            break;
                                        }
                                    }
                                }else {
                                    orderDrinkDataList.add(new DrinkDataOnBarChart(orderDetails.getNameProduct(), orderDetails.getQuantity()));
                                }
                            }
                            displayDataOnBarChart(orderDrinkDataList);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }

    private void displayDataOnBarChart(ArrayList<DrinkDataOnBarChart> listDrinkData) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0,10));
        entries.add(new BarEntry(1,20));
        entries.add(new BarEntry(2,12));
        entries.add(new BarEntry(3,9));

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