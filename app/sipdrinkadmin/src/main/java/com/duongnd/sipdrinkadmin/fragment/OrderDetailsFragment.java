package com.duongnd.sipdrinkadmin.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.adapter.OrderDetailsAdapter;
import com.duongnd.sipdrinkadmin.databinding.FragmentWaitingConfirmationBinding;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderDetailsFragment extends Fragment {
    public OrderDetailsFragment() {
    }

    private FragmentWaitingConfirmationBinding binding;
    private DatabaseReference databaseReference;
    private OrderDetailsAdapter adapter;
    private ArrayList<OrderDetails> list = new ArrayList<>();
    private String orderId = "", idUser = "", dateOrder = "", nameCustomer = "", phoneNumber = "", address = "", statusOrder = "";
    private double totalPrice = 0.0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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
        binding = FragmentWaitingConfirmationBinding.inflate(inflater, container, false);
        binding.btnBack.setOnClickListener(view -> getActivity().onBackPressed());
        setData();
        return binding.getRoot();
    }

    private void setData() {
        if (statusOrder.equals("choxacnhan")) {
            binding.tvTrangThaiOrder.setText("Đơn hàng đang chờ xác nhận");
            binding.btnXacNhanOrder.setVisibility(View.VISIBLE);
            binding.btnHuyOrder.setVisibility(View.VISIBLE);
        } else if (statusOrder.equals("danggiao")) {
            binding.tvTrangThaiOrder.setText("Đang giao hàng");
            binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
            binding.btnDaThanhToan.setVisibility(View.VISIBLE);
        } else if (statusOrder.equals("dahuy")) {
            binding.tvTrangThaiOrder.setText("Đơn hàng đã hủy");
            binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
        } else if (statusOrder.equals("dathanhtoan")) {
            binding.tvTrangThaiOrder.setText("Giao hàng thành công");
            binding.tvTrangThaiOrder.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
        }
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
                    OrderDetails orderDetails = dataSnapshot.getValue(OrderDetails.class);

                    if (orderDetails != null && orderDetails.getOrderId().equals(orderId)) {
                        list.add(orderDetails);
                        totalQuantity += orderDetails.getQuantity();
                        totalMoney += orderDetails.getPrice() * orderDetails.getQuantity();
                    }
                }
                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String tongTien = numberFormat.format(totalMoney);
                binding.tvTotalQuantity.setText(totalQuantity + " sản phẩm");
                binding.tvTotalPrice.setText(tongTien);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        adapter = new OrderDetailsAdapter(list, getActivity(), databaseReference);
        binding.rcvDrinkOder.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcvDrinkOder.setAdapter(adapter);
        //
        binding.btnXacNhanOrder.setOnClickListener(view -> {
            updateStatus("danggiao", "Đã xác nhận đơn hàng");
            senNotificationToCustomer("Đơn hàng #"+orderId+" đang trên đường giao đến bạn");
        });
        binding.btnDaThanhToan.setOnClickListener(view -> {
            updateStatus("dathanhtoan", "Giao hàng thành công");
        });
        binding.btnHuyOrder.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setIcon(R.drawable.ic_warning);
            builder.setTitle("Thông báo !");
            builder.setMessage("Bạn có chắc muốn hủy đơn hàng này không ?");
            builder.setNegativeButton("Không", null);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                updateStatus("dahuy", "Đã hủy đơn hàng");
                dialogInterface.dismiss();
            });
            builder.create().show();
        });
    }

    private void updateStatus(String status, String tb) {
        Map<String, Object> map = new HashMap<>();
        map.put("statusOrder", status);
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference1.child(orderId).updateChildren(map)
                .addOnSuccessListener(unused -> {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setContentText(tb)
                            .show();
                })
                .addOnFailureListener(e -> {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Lỗi !")
                            .show();
                });
    }

    private void senNotificationToCustomer(String message) {
        FirebaseMessaging.getInstance().subscribeToTopic("Customer_device")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        RemoteMessage.Builder builder = new RemoteMessage.Builder("Customer_device");
                        builder.setMessageId(Integer.toString(0));
                        builder.addData("message", message);
                        FirebaseMessaging.getInstance().send(builder.build());
                    }else {

                    }
                });
    }
}