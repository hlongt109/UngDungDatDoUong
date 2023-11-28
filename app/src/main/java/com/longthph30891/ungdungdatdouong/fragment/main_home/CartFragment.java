package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.adapter.CartAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentCartBinding;
import com.longthph30891.ungdungdatdouong.interfaces.CartInterface;
import com.longthph30891.ungdungdatdouong.model.Cart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;

    private List<Cart> cartList;

    Cart cart;
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

        cartList = new ArrayList<>();


        cartAdapter = new CartAdapter(getContext(), cartList);
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
            public void checkItem(boolean isChecked, int position) {
                isCheckItem(isChecked, position);
            }
        });

        binding.btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
            }
        });

        binding.muasp.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Mua sản phẩm đi", Toast.LENGTH_SHORT).show();
        });

        binding.imgBackCart.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        getDataCart();
    }

    private void getDataCart() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartList.clear();
                for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                    Cart cart = cartSnapshot.getValue(Cart.class);
                    cartList.add(cart);
                }
                if (cartList.isEmpty()) {
                    binding.layoutCartNull.setVisibility(View.VISIBLE);
                    binding.layoutSum.setVisibility(View.GONE);
                    binding.btnDatHang.setVisibility(View.GONE);
                } else {
                    binding.layoutCartNull.setVisibility(View.GONE);
                    binding.layoutSum.setVisibility(View.VISIBLE);
                    binding.btnDatHang.setVisibility(View.VISIBLE);
                }
                binding.recyclerViewCart.setAdapter(cartAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Cart", "onCancelled: " + error.getMessage());
            }
        });

        binding.recyclerViewCart.setAdapter(cartAdapter);

        if (cartList.isEmpty()) {
            binding.layoutCartNull.setVisibility(View.VISIBLE);
            binding.layoutSum.setVisibility(View.GONE);
            binding.btnDatHang.setVisibility(View.GONE);
        } else {
            binding.layoutCartNull.setVisibility(View.GONE);
            binding.layoutSum.setVisibility(View.VISIBLE);
            binding.btnDatHang.setVisibility(View.VISIBLE);
        }
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        for (Cart cart : cartList) {
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
        cart = cartList.get(position);
        int quantity = cart.getSoLuong();
        quantity++;
        cart.setSoLuong(quantity);
        updateTotalPrice();
        cartAdapter.notifyDataSetChanged();
    }

    private void isDecreaseClick(int position) {
        cart = cartList.get(position);
        int quantity = cart.getSoLuong();
        if (quantity > 1) {
            quantity--;
            cart.setSoLuong(quantity);
            updateTotalPrice();
            cartAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
        }
    }

    private void isCheckItem(boolean isChecked, int position) {
        cart = cartList.get(position);
        cart.setChecked(isChecked);
        if (isChecked) {
            cartList.add(cart);
            Log.d("TAG", "isCheckItem: " + cartList.size());
        } else {
            cartList.remove(cart);
            Log.d("TAG", "isCheckItem: " + cartList.size());
        }
        updateTotalPrice();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showBottomNavOnBackPressed();
    }
}