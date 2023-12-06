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
//import com.longthph30891.ungdungdatdouong.activity.PayOrderActivity;
import com.longthph30891.ungdungdatdouong.adapter.ProductAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentProducDetailBinding;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProducDetailFragment extends Fragment {

    String TAG = "ProducDetailFragment";


    SessionManager sessionManager;
    private FragmentProducDetailBinding binding;
    private List<Cart> selectedItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProducDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getContext());

        binding.imgBackDetail.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            Product product = (Product) bundle.get("product");
            if (product != null) {
                Picasso.get().load(product.getImage()).into(binding.imgDetail); //Picasso
                binding.txtTitleDetail.setText(product.getTenDoUong());
                Locale locale = new Locale("vi", "VN");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);
                String price = format.format(product.getGia());
                binding.txtProductPrice.setText("Giá: " + price);
                binding.txtProductDescription.setText(product.getMoTa());

                if (product.getTrangThai().equals("HetHang")) {
                    binding.btnBuyNow.setText("Đã hết hàng");
                    binding.btnBuyNow.setEnabled(false);
                    binding.btnAddToCart.setEnabled(false);
                }

                binding.btnBuyNow.setOnClickListener(v -> {
                    Cart cart = new Cart();
                    cart.setIdDoUong(product.getIdDoUong());
                    cart.setProductName(product.getTenDoUong());
                    cart.setProductPrice(product.getGia());
                    cart.setProductImage(product.getImage());
                    cart.setSoLuong(1);
                    selectedItems.clear();
                    selectedItems.add(cart);
//                    Intent intent = new Intent(getContext(), PayOrderActivity.class);
//                    intent.putParcelableArrayListExtra("selectedItems", (ArrayList<Cart>) selectedItems);
//                    startActivity(intent);
                });

                binding.btnAddToCart.setOnClickListener(v -> {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");

                    Cart cart = new Cart();
                    String idCart = databaseReference.push().getKey();
                    String idKhachHang = sessionManager.getLoggedInCustomerId();

                    cart.setIdGioHang(idCart);
                    cart.setIdKhachHang(idKhachHang);
                    cart.setIdDoUong(product.getIdDoUong());
                    cart.setProductName(product.getTenDoUong());
                    cart.setProductPrice(product.getGia());
                    cart.setProductImage(product.getImage());
                    cart.setSoLuong(1);

                    addToCart(cart);

//                    databaseReference.child(idKhachHang).child(product.getTenDoUong()).setValue(cart).addOnSuccessListener(command -> {
//                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
//                        dialog.setTitleText("Thêm vào giỏ hàng thành công");
//                        dialog.show();
//                    }).addOnFailureListener(e -> {
//                        Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
//                        Log.e("Cart", "Catch error:" + e.getMessage());
//                    });


                });

            }
        }
    }


    private void addToCart(Cart cart) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart").child(sessionManager.getLoggedInCustomerId()).child(cart.getIdDoUong());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Cart cartInfo = snapshot.getValue(Cart.class);
                    cartInfo.setSoLuong(cartInfo.getSoLuong() + cart.getSoLuong());
                    databaseReference.setValue(cartInfo);
                } else {
                    databaseReference.setValue(cart).addOnSuccessListener(command -> {
                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("Thêm vào giỏ hàng thành công");
                        dialog.show();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "add to cart: " + error.getMessage());
            }
        });
    }

    private boolean checkDuplicateIds(String cartId, String productId) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");
        Query query = databaseReference.orderByChild("idDoUong").equalTo(productId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Cart cart = dataSnapshot.getValue(Cart.class);
                        if (cart.getIdDoUong().equals(productId)) {
                            Log.d("TAG", "onDataChange: " + cart.getIdDoUong());
                            return;
                        } else {
                            Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });

        return true;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showBottomNavOnBackPressed();
    }
}