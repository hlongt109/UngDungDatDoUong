package com.duongnd.sipdrinkadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetDeliveringList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetOrderList;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentStatisticBinding;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.duongnd.sipdrinkadmin.model.DonHang;
import com.duongnd.sipdrinkadmin.model.Order;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.duongnd.sipdrinkadmin.model.TestData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticFragment extends Fragment {
    public StatisticFragment() {}
    private FragmentStatisticBinding binding;
    private List<String> listTestValues;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater,container,false);
        getQuantity();
        binding.cardOrderConfirm.setOnClickListener(view -> {
            BottomSheetOrderList bottomSheetOrderList = new BottomSheetOrderList();
            bottomSheetOrderList.show(getChildFragmentManager(),"BottomSheetOrderList");
        });
        binding.cardDelivering.setOnClickListener(view -> {
            BottomSheetDeliveringList bottomSheetDeliveringList = new BottomSheetDeliveringList();
            bottomSheetDeliveringList.show(getChildFragmentManager(),"BottomSheetDeliveringList");
        });
        binding.chooseStatistics.setOnClickListener(view -> {
            openPopupMenu(view);
        });
        TestData();
        return binding.getRoot();
    }

    private void openPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.option_menu_revenua);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Daily) {
                binding.tvRevenue.setText("Daily");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Weekly) {
                binding.tvRevenue.setText("Weekly");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Monthly) {
                binding.tvRevenue.setText("Monthly");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Yearly) {
                binding.tvRevenue.setText("Yearly");

                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void TestData(){
        Description description = new Description();
        description.setText("Drink type record");
        description.setPosition(150f,150f);
        binding.lineChart.setDescription(description);
        binding.lineChart.getAxisRight().setDrawLabels(false);

        listTestValues = Arrays.asList("January","February","March","April","May","June","July","August","September","October","November","December");

        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(listTestValues));
        xAxis.setLabelCount(12);
        xAxis.setGranularity(1f);

        YAxis yAxis = binding.lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry>entries = new ArrayList<>();
        entries.add(new Entry(0,20f));
        entries.add(new Entry(1,10f));
        entries.add(new Entry(2,25f));
        entries.add(new Entry(3 ,40f));

        List<Entry>entries2 = new ArrayList<>();
        entries2.add(new Entry(0,5f));
        entries2.add(new Entry(1,30f));
        entries2.add(new Entry(2,20f));

        LineDataSet dataSet = new LineDataSet(entries,"Coffee");
        dataSet.setColor(Color.BLUE);
        LineDataSet dataSet2 = new LineDataSet(entries2,"Tra Sua");
        dataSet2.setColor(Color.RED);

        LineData lineData = new LineData(dataSet,dataSet2);
        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();
    }
    public void getQuantity(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.orderByChild("statusOrder").addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int slsp = 0;
                int sldlv = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    if ("choxacnhan".equals(order.getStatusOrder())) {
                        slsp++;
                    } else if ("danggiao".equals(order.getStatusOrder())) {
                        sldlv++;
                    }
                }
                binding.tvWaitForConfirm.setText(String.valueOf(slsp));
                binding.tvDelivering.setText(String.valueOf(sldlv));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


        public void fakeData() {
            DatabaseReference orderReference = FirebaseDatabase.getInstance().getReference("Order");

            // Create and save sample orders
            saveFakeData(orderReference, "1", "user1", "2023-11-26", "John Doe", "123456789", "123 Main St", "choxacnhan", 50.0);
            saveFakeData(orderReference, "2", "user2", "2023-11-27", "Jane Doe", "987654321", "456 Main St", "choxacnhan", 30.0);
            saveFakeData(orderReference, "3", "user3", "2023-11-28", "Bob Smith", "555555555", "789 Main St", "choxacnhan", 25.0);
        }

        private void saveFakeData(DatabaseReference orderReference, String orderId, String idUser, String dateOrder,
                                      String nameCustomer, String phoneNumber, String address, String statusOrder, double totalPrice) {
            Order order = new Order(orderId, idUser, dateOrder, nameCustomer, phoneNumber, address, statusOrder, totalPrice);
            orderReference.child(order.getOrderId()).setValue(order);
        }
       public void fakeDataDetails(){
           DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
           OrderDetails  orderDetails1 = new OrderDetails("1", "1", "-Nk-OrcTeK96nqltCtd1", 2, 500000, "Rượu vang Pháp", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-OrcTeK96nqltCtd1.jpg?alt=media&token=c1533dc3-658b-4a16-bd78-a78441c9bbdd");
           OrderDetails orderDetails2 = new OrderDetails("2", "1", "-Nk-PLkhgKmMU2gr1mAe", 1, 25000, "Trà sữa trân châu đen", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-PLkhgKmMU2gr1mAe.jpg?alt=media&token=4644fa07-a0f6-4671-a5b3-8eeae8351be4");
           OrderDetails orderDetails3 = new OrderDetails("3", "2", "-Nk-Pu1U9uBrbtGfSR78", 3, 18000, "Cà phê đen không đường", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-Pu1U9uBrbtGfSR78.jpg?alt=media&token=c8f34d1d-9fbd-4853-93c3-30ed7be4c281");
           OrderDetails orderDetails4 = new OrderDetails("4", "2", "-Nk-R_dl0cpuNz0QBEEg", 3, 10000, "Bia Hà Nội", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-R_dl0cpuNz0QBEEg.jpg?alt=media&token=96e2837c-8061-4936-84b9-2053e4d9ee04");
           OrderDetails orderDetails5 = new OrderDetails("5", "3", "-Nk-Pu1U9uBrbtGfSR78", 3, 18000, "Cà phê đen không đường", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-Pu1U9uBrbtGfSR78.jpg?alt=media&token=c8f34d1d-9fbd-4853-93c3-30ed7be4c281");

// Save fake data to DatabaseReference
           reference.child(orderDetails1.getIdOrderDetails()).setValue(orderDetails1);
           reference.child(orderDetails2.getIdOrderDetails()).setValue(orderDetails2);
           reference.child(orderDetails3.getIdOrderDetails()).setValue(orderDetails3);
           reference.child(orderDetails4.getIdOrderDetails()).setValue(orderDetails4);
           reference.child(orderDetails5.getIdOrderDetails()).setValue(orderDetails5);
       }
}