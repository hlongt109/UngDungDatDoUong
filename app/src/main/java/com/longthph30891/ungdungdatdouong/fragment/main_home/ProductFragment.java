package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.adapter.ProductAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentProductBinding;
import com.longthph30891.ungdungdatdouong.interfaces.ProductInterface;
import com.longthph30891.ungdungdatdouong.model.Category;
import com.longthph30891.ungdungdatdouong.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    ProductAdapter productAdapter;
    List<Product> productList;
    private FragmentProductBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductBinding.inflate(inflater, container, false);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getContext(), productList);
        binding.recyclerViewProduct2.setAdapter(productAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Category category = (Category) bundle.get("categoryId");
            if (category != null) {
                binding.txtTitleCategoryProduct.setText(category.getTypeName());
                getProductsByCategoryId(category.getTypeId());
            }
        }

        binding.icArrowBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        productAdapter.clickProduct(new ProductInterface() {

            @Override
            public void clickProduct(Product product) {
                changeToProductDetail(product);
            }
        });

        return binding.getRoot();
    }


    private void getProductsByCategoryId(String categoryId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DoUong");
        Query query = myRef.orderByChild("maLoai").equalTo(categoryId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void changeToProductDetail(Product product) {
        ProducDetailFragment productDetailFragment = new ProducDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        productDetailFragment.setArguments(bundle);
        ((MainActivity) requireActivity()).showProductDetail(productDetailFragment);
    }

}