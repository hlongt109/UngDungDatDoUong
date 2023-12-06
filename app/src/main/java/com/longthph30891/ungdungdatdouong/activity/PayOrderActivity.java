package com.longthph30891.ungdungdatdouong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.adapter.PayOrderAdapter;
import com.longthph30891.ungdungdatdouong.databinding.ActivityPayOrderBinding;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.longthph30891.ungdungdatdouong.model.Khachang;
import com.longthph30891.ungdungdatdouong.model.Order;
import com.longthph30891.ungdungdatdouong.model.OrderDetail;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;
import com.longthph30891.ungdungdatdouong.utilities.api.CreateOrder;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class PayOrderActivity extends AppCompatActivity {


    String TAG = "PayOrderActivity";

    SessionManager sessionManager;
    String methodPay = "";
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

//         zalo pay
        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);


        sessionManager = new SessionManager(this);

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
            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                new SweetAlertDialog(PayOrderActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Vui lòng điền đầy đủ thông tin")
                        .setConfirmText("Đóng")
                        .show();
            } else if (!phone.matches("0\\d{9}")) {
                new SweetAlertDialog(PayOrderActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Số điện thoại không đúng định dạng")
                        .setConfirmText("Đóng")
                        .show();
            } else if (methodPay.isEmpty()) {
                new SweetAlertDialog(PayOrderActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Vui lòng chọn phương thức thanh toán")
                        .setConfirmText("Đóng")
                        .show();
            } else {
                showConfirmOrderDialog(name, phone, address, methodPay);
            }
            Log.d(TAG, "payMethod: " + methodPay);
        });

        binding.layoutMethodPay.setOnClickListener(v -> {
            AlertDialog builder = new AlertDialog.Builder(this).create();
            View view = getLayoutInflater().inflate(R.layout.dialog_method_pay, null);
            builder.setTitle("Chọn phương thức thanh toán");
            builder.setView(view);

            TextView txtZaloPay = view.findViewById(R.id.txt_payment_ZaloPay);
            TextView txtMoMo = view.findViewById(R.id.txt_payment_MoMo);
            TextView txtPaypal = view.findViewById(R.id.txt_payment_Paypal);
            TextView txtOffline = view.findViewById(R.id.txt_payment_offline);

            txtZaloPay.setOnClickListener(v1 -> {
                binding.txtMethodPay.setText(txtZaloPay.getText().toString());
                methodPay = txtZaloPay.getText().toString();
                builder.dismiss();
            });

            txtMoMo.setOnClickListener(v1 -> {
                binding.txtMethodPay.setText(txtMoMo.getText().toString());
                methodPay = txtMoMo.getText().toString();
                builder.dismiss();
            });

            txtPaypal.setOnClickListener(v1 -> {
                binding.txtMethodPay.setText(txtPaypal.getText().toString());
                methodPay = txtPaypal.getText().toString();
                builder.dismiss();
            });

            txtOffline.setOnClickListener(v1 -> {
                binding.txtMethodPay.setText(txtOffline.getText().toString());
                methodPay = txtOffline.getText().toString();
                builder.dismiss();
            });
            builder.show();
        });

        getUserInfo();

    }


    private void showConfirmOrderDialog(String name, String phone, String address, String methodPay) {
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
        TextView txtMethodPayment = dialog.findViewById(R.id.txt_method_payment);

        txtName.setText("Tên khách hàng: " + name);
        txtPhone.setText("Số điện thoại: " + phone);
        txtAddress.setText("Địa chỉ: " + address);
        txtMethodPayment.setText(methodPay);
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String price = format.format(totalPrice);
        txtTotal.setText("Tổng thanh toán: " + price);

        String status = "choxacnhan";
        String currentDateAndTime = String.format("%02d/%02d/%04d", day, month, year);
        String idUser = sessionManager.getLoggedInCustomerId();

        btnConfirm.setOnClickListener(v -> {
            Order order = new Order();
            order.setorderId(String.valueOf(generateRandomOrderId()));
            order.setIdUser(idUser);
            order.setnameCustomer(name);
            order.setphoneNumber(phone);
            order.setaddress(address);
            order.setStatusOrder(status);
            order.setTotalPrice(totalPrice);
            order.setDateOrder(currentDateAndTime);

            if (methodPay.isEmpty()) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Vui lòng chọn phương thức thanh toán!")
                        .show();
            } else if (methodPay.equals("Thanh toán khi nhận hàng")) {
                addOrder(order);
            } else if (methodPay.equals("Thanh toán bằng ZaloPay")) {
                paymentWithZaloPay(order);
            } else if (methodPay.equals("Thanh toán bằng MoMo")) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Chức năng thanh toán bằng MoMo đang phát triển, vui lòng chọn phương thức khác!")
                        .show();
            } else if (methodPay.equals("Thanh toán bằng Paypal")) {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Chức năng thanh toán bằng Paypal đang phát triển, vui lòng chọn phương thức khác!")
                        .show();
            }
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
                for (Cart item : selectedItems) {
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setIdOrderDetail(String.valueOf(generateRandomOrderId()));
                    orderDetail.setOrderId(order.getorderId());
                    orderDetail.setIdProduct(item.getIdDoUong());
                    orderDetail.setQuantity(item.getSoLuong());
                    orderDetail.setNameProduct(item.getProductName());
                    orderDetail.setPrice(item.getProductPrice());
                    orderDetail.setImageProduct(item.getProductImage());
                    addOrderDetail(orderDetail);
                }
            }
        }).addOnFailureListener(e -> {
            Log.d(TAG, "addOrder: " + e.getMessage());
        });
    }

    private void addOrderDetail(OrderDetail orderDetail) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("OrderDetails");
        String id = databaseReference.getKey().toString();
        SweetAlertDialog dialog = new SweetAlertDialog(this);
        databaseReference.child(orderDetail.getIdOrderDetail()).setValue(orderDetail).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("Đặt hàng thành công");
                dialog.setConfirmText("Xác nhận");
                dialog.setConfirmClickListener(v -> {
                    dialog.dismiss();
                    for (Cart item : selectedItems) {
                        DatabaseReference cartRef = firebaseDatabase.getReference("Cart").child(sessionManager.getLoggedInCustomerId()).child(item.getIdDoUong());
                        cartRef.removeValue().addOnSuccessListener(command -> {
                            Log.d(TAG, "addOrderDetail: " + command);
                        }).addOnFailureListener(e -> {
                            Log.d(TAG, "addOrderDetail: " + e.getMessage());
                        });
                    }
                    Intent intent = new Intent(PayOrderActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
                dialog.show();
            } else {
                dialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
                dialog.setTitleText("Đặt hàng thất bại");
                dialog.show();
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "addOrderDetail: " + e.getMessage());
        });
    }

    public void txtToTal() {
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


    private void getUserInfo() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users").child(sessionManager.getLoggedInCustomerId());
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Khachang khachang = snapshot.getValue(Khachang.class);
                    binding.edtOrderName.setText(khachang.getFullName());
                    binding.edtOrderPhone.setText(khachang.getPhone());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });

    }

    private int generateRandomOrderId() {
        int minOrderId = 1000;
        int maxOrderId = 9999;
        Random random = new Random();
        return random.nextInt(maxOrderId - minOrderId + 1) + minOrderId;
    }


    private void paymentWithZaloPay(Order order) {
        CreateOrder orderApi = new CreateOrder();

        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        String price = decimalFormat.format(order.getTotalPrice());
        Log.d(TAG, "paymentWithZaloPay: " + price);

        try {
            JSONObject data = orderApi.createOrder(price);
            String code = data.getString("return_code");
            Log.d(TAG, "requestZaloPay: " + code);
            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                Log.d(TAG, "requestZaloPay token: " + token);
                ZaloPaySDK.getInstance().payOrder(PayOrderActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(String s, String s1, String s2) {
                        addOrder(order);
                    }

                    @Override
                    public void onPaymentCanceled(String s, String s1) {

                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {

                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paymentWithOffline(Order order) {
        addOrder(order);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        selectedItems.clear();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}