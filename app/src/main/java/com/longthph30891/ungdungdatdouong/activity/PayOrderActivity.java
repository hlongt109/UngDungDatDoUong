package com.longthph30891.ungdungdatdouong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.adapter.PayOrderAdapter;
import com.longthph30891.ungdungdatdouong.databinding.ActivityPayOrderBinding;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.longthph30891.ungdungdatdouong.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class PayOrderActivity extends AppCompatActivity {

    private ActivityPayOrderBinding binding;
    private PayOrderAdapter payOrderAdapter;
    private ArrayList<Cart> selectedItems;
    private double totalPrice = 0;
    private Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH) + 1;
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Intent intent = getIntent();
        selectedItems = intent.getParcelableArrayListExtra("selectedItems");
        payOrderAdapter = new PayOrderAdapter(this, selectedItems);
        binding.recyclerViewOrder.setAdapter(payOrderAdapter);
        txtToTal();


        binding.btnThanhToanOrder.setOnClickListener(v -> {
            String name = binding.edtOrderName.getText().toString().trim();
            String phone = binding.edtOrderPhone.getText().toString().trim();
            String address = binding.edtOrderAddress.getText().toString().trim();
            showConfirmOrderDialog(name, phone, address);
        });

    }

    private void showConfirmOrderDialog(String name, String phone, String address) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_confirm_order);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView txtName = dialog.findViewById(R.id.txt_name_customer_order);
        TextView txtPhone = dialog.findViewById(R.id.txt_phone_customer_order);
        TextView txtAddress = dialog.findViewById(R.id.txt_address_customer_order);
        TextView txtTotal = dialog.findViewById(R.id.txt_total_price_order);
        Button btnConfirm = dialog.findViewById(R.id.btn_confirm_order);
        TextView txtCancel = dialog.findViewById(R.id.txt_cancel_order);

        txtName.setText("Tên khách hàng: " + name);
        txtPhone.setText("Số điện thoại: " + phone);
        txtAddress.setText("Địa chỉ: " + address);
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String price = format.format(totalPrice);
        txtTotal.setText("Tổng tiền: " + price);

        String status = "choxacnhan";
        String currentDateAndTime = String.format("%02d/%02d/%04d", day, month, year);

        btnConfirm.setOnClickListener(v -> {
            Order order = new Order();
            order.setorderId(String.valueOf(generateRandomOrderId()));
            order.setIdUser("1");
            order.setnameCustomer(name);
            order.setphoneNumber(phone);
            order.setaddress(address);
            order.setStatusOrder(status);
            order.setTotalPrice(totalPrice);
            order.setDateOrder(currentDateAndTime);
            addOrder(order);
            dialog.dismiss();
        });
        txtCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        new CountDownTimer(60000, 1000) {

            int seconds = 60;

            @Override
            public void onTick(long millisUntilFinished) {
                txtCancel.setText("Hủy (" + (seconds--) + ")");
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
        dialog.show();
    }

    private void addOrder(Order order) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Order");
        databaseReference.child(order.getorderId()).setValue(order).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Toast.makeText(this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đặt hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.d("TAG", "addOrder: " + e.getMessage());
        });
    }

    public void txtToTal() {
//        int Sl = payOrderAdapter.getTotalQuantity();
        binding.txtTotal.setText("Tổng tiền: ");

        for (Cart item : selectedItems) {
            int quantity = item.getSoLuong();
            double price = item.getProductPrice();
            totalPrice += quantity * price;
        }

        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String price = format.format(totalPrice);
        binding.txtTotal.setText(price);
    }

    private int generateRandomOrderId() {
        int minOrderId = 1000;
        int maxOrderId = 9999;
        Random random = new Random();
        return random.nextInt(maxOrderId - minOrderId + 1) + minOrderId;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedItems.clear();
    }
}