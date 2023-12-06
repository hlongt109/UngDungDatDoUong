package com.duongnd.sipdrinkadmin.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetDeliveringList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetOrderList;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentStatisticBinding;
import com.duongnd.sipdrinkadmin.fragment.statistics.TopdrinkFragment;
import com.duongnd.sipdrinkadmin.fragment.statistics.TotalRevenueByDayFragment;
import com.duongnd.sipdrinkadmin.fragment.statistics.TotalRevenueFragment;
import com.duongnd.sipdrinkadmin.model.Order;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StatisticFragment extends Fragment {
    public StatisticFragment() {
    }
    private FragmentStatisticBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater, container, false);
        getQuantity();
        totalRevenueForTheDay();
        replaceFrg(new TopdrinkFragment());
//        fakeData();
//        fakeDataDetails();
        binding.cardOrderConfirm.setOnClickListener(view -> {
            BottomSheetOrderList bottomSheetOrderList = new BottomSheetOrderList();
            bottomSheetOrderList.show(getChildFragmentManager(), "BottomSheetOrderList");
        });
        binding.cardDelivering.setOnClickListener(view -> {
            BottomSheetDeliveringList bottomSheetDeliveringList = new BottomSheetDeliveringList();
            bottomSheetDeliveringList.show(getChildFragmentManager(), "BottomSheetDeliveringList");
        });
        binding.chooseStatistics.setOnClickListener(view -> {
            openPopupMenu(view);
        });
        return binding.getRoot();
    }

    private void openPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.option_menu_revenua);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_DoanhSo) {
                binding.tvRevenue.setText("Doanh số bán hàng");
                replaceFrg(new TopdrinkFragment());
            } else if (menuItem.getItemId() == R.id.nav_TotalRevenua) {
                binding.tvRevenue.setText("Tổng doanh thu");
                replaceFrg(new TotalRevenueFragment());
            } else if (menuItem.getItemId() == R.id.nav_ChooseDay) {
                replaceFrg(new TotalRevenueByDayFragment());
                binding.tvRevenue.setText("Doanh thu theo ngày");
            }
            return true;
        });
        popupMenu.show();
    }

    private void totalRevenueForTheDay() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.orderByChild("statusOrder").equalTo("dathanhtoan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRevenua = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order2 = dataSnapshot.getValue(Order.class);
                    if ("dathanhtoan".equals(order2.getStatusOrder()) && currentDate.equals(order2.getDateOrder())) {
                        totalRevenua += order2.getTotalPrice();
                    }
                }
                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String money = numberFormat.format(totalRevenua);
                binding.tvTotalRevenue.setText(money);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getQuantity() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.orderByChild("statusOrder").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int slsp = 0;
                int sldlv = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
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
        saveFakeData(orderReference, "1", "user1", "26/11/2023", "John Doe", "123456789", "123 Main St", "choxacnhan", 1025000);
        saveFakeData(orderReference, "2", "user2", "27/11/2023", "Jane David", "987654321", "456 Main St", "choxacnhan", 84000);
        saveFakeData(orderReference, "3", "user3", "28/11/2023", "Bob Smith", "555555555", "789 Main St", "choxacnhan", 54000);
        saveFakeData(orderReference, "4", "user4", "03/12/2023", "Bob Smithy", "555555555", "789 Main St", "dathanhtoan", 1000000);
    }
    private void saveFakeData(DatabaseReference orderReference, String orderId, String idUser, String dateOrder,
                              String nameCustomer, String phoneNumber, String address, String statusOrder, double totalPrice) {
        Order order = new Order(orderId, idUser, dateOrder, nameCustomer, phoneNumber, address, statusOrder, totalPrice);
        orderReference.child(order.getOrderId()).setValue(order);
    }
    public void replaceFrg(Fragment frg) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frameLayout_Statistics, frg).commit();
    }
    public void fakeDataDetails() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("OrderDetails");
        OrderDetails orderDetails1 = new OrderDetails("1", "1", "-Nk-OrcTeK96nqltCtd1", 2, 500000, "Rượu vang Pháp", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-OrcTeK96nqltCtd1.jpg?alt=media&token=c1533dc3-658b-4a16-bd78-a78441c9bbdd");
        OrderDetails orderDetails2 = new OrderDetails("2", "1", "-Nk-PLkhgKmMU2gr1mAe", 1, 25000, "Trà sữa trân châu đen", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-PLkhgKmMU2gr1mAe.jpg?alt=media&token=4644fa07-a0f6-4671-a5b3-8eeae8351be4");
        OrderDetails orderDetails3 = new OrderDetails("3", "2", "-Nk-Pu1U9uBrbtGfSR78", 3, 18000, "Cà phê đen không đường", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-Pu1U9uBrbtGfSR78.jpg?alt=media&token=c8f34d1d-9fbd-4853-93c3-30ed7be4c281");
        OrderDetails orderDetails4 = new OrderDetails("4", "2", "-Nk-R_dl0cpuNz0QBEEg", 3, 10000, "Bia Hà Nội", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-R_dl0cpuNz0QBEEg.jpg?alt=media&token=96e2837c-8061-4936-84b9-2053e4d9ee04");
        OrderDetails orderDetails5 = new OrderDetails("5", "3", "-Nk-Pu1U9uBrbtGfSR78", 3, 18000, "Cà phê đen không đường", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-Pu1U9uBrbtGfSR78.jpg?alt=media&token=c8f34d1d-9fbd-4853-93c3-30ed7be4c281");
        OrderDetails orderDetails6 = new OrderDetails("6", "4", "-Nk-OrcTeK96nqltCtd1", 2, 500000, "Rượu vang Pháp", "https://firebasestorage.googleapis.com/v0/b/sipdrinkapplication.appspot.com/o/DoUongImages%2F-Nk-OrcTeK96nqltCtd1.jpg?alt=media&token=c1533dc3-658b-4a16-bd78-a78441c9bbdd");
//
        reference.child(orderDetails1.getIdOrderDetails()).setValue(orderDetails1);
        reference.child(orderDetails2.getIdOrderDetails()).setValue(orderDetails2);
        reference.child(orderDetails3.getIdOrderDetails()).setValue(orderDetails3);
        reference.child(orderDetails4.getIdOrderDetails()).setValue(orderDetails4);
        reference.child(orderDetails5.getIdOrderDetails()).setValue(orderDetails5);
        reference.child(orderDetails6.getIdOrderDetails()).setValue(orderDetails6);
    }
}