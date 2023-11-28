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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.databinding.FragmentProducDetailBinding;
import com.longthph30891.ungdungdatdouong.model.Cart;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProducDetailFragment extends Fragment {


    private FragmentProducDetailBinding binding;

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
                    Toast.makeText(getContext(), "Mua sản phẩm đi", Toast.LENGTH_SHORT).show();
                });

                binding.btnAddToCart.setOnClickListener(v -> {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference("Cart");
                    Cart cart = new Cart();
                    String idCart = databaseReference.push().getKey();
                    cart.setIdGioHang(idCart);
                    cart.setIdKhachHang("1");
                    cart.setIdDoUong(product.getIdDoUong());
                    cart.setProductName(product.getTenDoUong());
                    cart.setProductPrice(product.getGia());
                    cart.setProductImage(product.getImage());
                    cart.setSoLuong(1);
                    databaseReference.child(idCart).setValue(cart).addOnSuccessListener(command -> {
                        SweetAlertDialog dialog = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                        dialog.setTitleText("Thêm vào giỏ hàng thành công");
                        dialog.show();
//                        Toast.makeText(getContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("Cart", "Catch error:" + e.getMessage());
                    });
                });
            }
        }
    }

    private void addToCart() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) requireActivity()).showBottomNavOnBackPressed();
    }
}