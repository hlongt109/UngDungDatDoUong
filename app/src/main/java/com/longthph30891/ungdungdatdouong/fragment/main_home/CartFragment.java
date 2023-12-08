package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.activity.PayOrderActivity;
import com.longthph30891.ungdungdatdouong.adapter.CartAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentCartBinding;
import com.longthph30891.ungdungdatdouong.interfaces.CartInterface;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartFragment extends Fragment {

    Cart cart;
    SessionManager sessionManager;
    private FragmentCartBinding binding;
    private ArrayList<Cart> cartArrayList;
    private List<Cart> selectedItems = new ArrayList<>();
    private CartAdapter cartAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());
        cartArrayList = new ArrayList<>();
        cartAdapter = new CartAdapter(getContext(), cartArrayList);

        binding.recyclerViewCart.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerViewCart.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        getDataCartByIdKhachHang();

        cartAdapter.cartClick(new CartInterface() {
            @Override
            public void onIncreaseClick(int position) {
                isIncreaseClick(position);
            }

            @Override
            public void onDecreaseClick(int position) {
                isDecreaseClick(position);
            }

            @Override
            public void checkItem(int position, boolean isChecked) {
                isCheckItem(isChecked, position);
            }

        });

        binding.btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), PayOrderActivity.class);
                    intent.putParcelableArrayListExtra("selectedItems", (ArrayList<Cart>) selectedItems);
                    startActivity(intent);
                }
            }
        });

        binding.muasp.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        binding.imgBackCart.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        binding.imgDeleteCart.setOnClickListener(v -> {
            if (selectedItems.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng chọn sản phẩm", Toast.LENGTH_SHORT).show();
            } else {
                (selectedItems).forEach(this::deleteCart);
            }
        });

    }

    private void deleteCart(Cart cart) {
        String idKhachHang = sessionManager.getLoggedInCustomerId();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(idKhachHang).child(cart.getIdDoUong());
        SweetAlertDialog sDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
        sDialog.setTitleText("Bạn muốn xóa sản phẩm?");
        sDialog.setConfirmText("Có");
        sDialog.setConfirmClickListener(sDialog1 -> {
            sDialog1.dismissWithAnimation();
            databaseReference.removeValue().addOnSuccessListener(command -> {
                binding.imgDeleteCart.setVisibility(View.GONE);
                binding.txtTotalPrice.setText("0đ");
            }).addOnFailureListener(e -> {
                Log.e("Cart", "onFailure: " + e.getMessage());
            });
        });
        sDialog.setCancelText("Không");
        sDialog.setCancelClickListener(sDialog1 -> {
            sDialog1.dismissWithAnimation();
            sDialog.dismiss();
        });
        sDialog.show();

    }

    private void getDataCartByIdKhachHang() {
        String idKhachHang = sessionManager.getLoggedInCustomerId();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(idKhachHang);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartArrayList.clear();
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    Cart cart = cartSnapshot.getValue(Cart.class);
                    cartArrayList.add(cart);
                }
                if (cartArrayList.isEmpty()) {
                    binding.layoutCartNull.setVisibility(View.VISIBLE);
                    binding.cardTotalPrice.setVisibility(View.GONE);
                    binding.btnDatHang.setVisibility(View.GONE);
                } else {
                    binding.layoutCartNull.setVisibility(View.GONE);
                    binding.cardTotalPrice.setVisibility(View.VISIBLE);
                    binding.btnDatHang.setVisibility(View.VISIBLE);
                }
                binding.recyclerViewCart.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cart", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Cart cart : selectedItems) {
            int quantity = cart.getSoLuong();
            double price = cart.getProductPrice();
            totalPrice += quantity * price;
        }
        Locale locale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        String price = format.format(totalPrice);
        binding.txtTotalPrice.setText(price);
    }

    private void isIncreaseClick(int position) {
        cart = cartArrayList.get(position);
        int quantity = cart.getSoLuong();
        quantity++;
        cart.setSoLuong(quantity);
        updateQuantityCartByIdCustomer(cart);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void isDecreaseClick(int position) {
        cart = cartArrayList.get(position);
        int quantity = cart.getSoLuong();
        if (quantity > 1) {
            quantity--;
            cart.setSoLuong(quantity);
            updateQuantityCartByIdCustomer(cart);
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        } else {
            Toast.makeText(getContext(), "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
        }
    }

    private void isCheckItem(boolean isChecked, int position) {
        cart = cartArrayList.get(position);
        cart.setChecked(isChecked);


        String idKhachHang = sessionManager.getLoggedInCustomerId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child(idKhachHang).child(cart.getIdGioHang());
        if (isChecked) {
            selectedItems.add(cart);
            Log.d("TAG", "isCheckItem: " + cart.isChecked());
        } else {
            selectedItems.remove(cart);
            Log.d("TAG", "isCheckItem: " + cart.isChecked());
        }
        if (selectedItems.isEmpty()) {
            binding.imgDeleteCart.setVisibility(View.GONE);
        } else {
            binding.imgDeleteCart.setVisibility(View.VISIBLE);
        }
        updateTotalPrice();
    }

    private void updateQuantityCartByIdCustomer(Cart cart) {
        String idKhachHang = sessionManager.getLoggedInCustomerId();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(idKhachHang).child(cart.getIdDoUong()).child("soLuong");
        databaseReference.setValue(cart.getSoLuong());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showBottomNavOnBackPressed();
    }

}