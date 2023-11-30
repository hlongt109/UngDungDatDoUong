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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

public class CartFragment extends Fragment {

    private FragmentCartBinding binding;

    private ArrayList<Cart> cartArrayList;
    private List<Cart> selectedItems = new ArrayList<>();

    Cart cart;
    private CartAdapter cartAdapter;

    SessionManager sessionManager;


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
        updateQuantityCart(cart);
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void isDecreaseClick(int position) {
        cart = cartArrayList.get(position);
        int quantity = cart.getSoLuong();
        if (quantity > 1) {
            quantity--;
            cart.setSoLuong(quantity);
            updateQuantityCart(cart);
            cartAdapter.notifyDataSetChanged();
            updateTotalPrice();
        } else {
            Toast.makeText(getContext(), "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
        }
    }

    private void isCheckItem(boolean isChecked, int position) {
        cart = cartArrayList.get(position);
        cart.setChecked(isChecked);

        if (isChecked) {
            selectedItems.add(cart);
            Log.d("TAG", "isCheckItem: " + cartArrayList.size());
        } else {
            selectedItems.remove(cart);
            Log.d("TAG", "isCheckItem: " + cartArrayList.size());
        }
        updateTotalPrice();
    }


    private void updateQuantityCart(Cart cart) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");
        databaseReference.child(cart.getIdGioHang()).setValue(cart).addOnSuccessListener(command -> {
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("Cart", "Catch error:" + e.getMessage());
        });
    }

    private void updateCheckedCart(Cart cart) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");
        databaseReference.child(cart.getIdGioHang()).setValue(cart).addOnSuccessListener(command -> {
            Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Log.e("Cart", "Catch error:" + e.getMessage());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showBottomNavOnBackPressed();
    }

}