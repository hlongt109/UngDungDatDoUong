package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.activity.ChatActivity;
import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.adapter.CategoryHomeAdapter;
import com.longthph30891.ungdungdatdouong.adapter.ProductAdapter;
import com.longthph30891.ungdungdatdouong.databinding.FragmentHomeBinding;
import com.longthph30891.ungdungdatdouong.interfaces.CategoryInterface;
import com.longthph30891.ungdungdatdouong.interfaces.ProductInterface;
import com.longthph30891.ungdungdatdouong.model.Category;
import com.longthph30891.ungdungdatdouong.model.Khachang;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.longthph30891.ungdungdatdouong.utilities.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final long startTime = System.currentTimeMillis();
    CategoryHomeAdapter adapter;
    ProductAdapter productAdapter;
    List<Category> categoryList;
    List<Product> productList;
    SessionManager sessionManager;
    private FragmentHomeBinding binding;
    private int timeLoadingCategory;
    private int timeLoadingProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        categoryList = new ArrayList<>();
        productList = new ArrayList<>();
        adapter = new CategoryHomeAdapter(getContext(), categoryList);
        productAdapter = new ProductAdapter(getContext(), productList);
        binding.recyclerView.setAdapter(adapter);
        sessionManager = new SessionManager(getContext());

        binding.recyclerViewProduct.setAdapter(productAdapter);

        getListCategory();
        getListProduct();
        getListImage();


        adapter.clickCategory(new CategoryInterface() {
            @Override
            public void clickCategory(Category category) {
                changeToProduct(category);
//                getProductsByCategoryId(category.getTypeId());
            }
        });

        productAdapter.clickProduct(new ProductInterface() {
            @Override
            public void clickProduct(Product product) {
                changeToProductDetail(product);
            }
        });


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                binding.shimmerViewProduct.stopShimmerAnimation();
//            }
//        }, timeLoadingProduct);


        binding.recyclerView.setVisibility(View.INVISIBLE);
        binding.shimmerViewCategory.startShimmerAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.shimmerViewCategory.stopShimmerAnimation();
                binding.shimmerViewCategory.setVisibility(View.GONE);
            }
        }, timeLoadingCategory);


        binding.imgMessage.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), ChatActivity.class));
        });

//        getNamCustomerById(sessionManager.getLoggedInCustomerId());


        binding.seach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                seachList(newText);
                return true;
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    Log.d("TAG", "onDataChange: " + product.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }


    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("DoUong");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    long endTime = System.currentTimeMillis();
                    long getTime = endTime - startTime;
                    timeLoadingProduct = (int) getTime;
                    productList.add(product);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }

    private void getListCategory() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("LoaiDoUong");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category = dataSnapshot.getValue(Category.class);
                    long endTime = System.currentTimeMillis();
                    long getTime = endTime - startTime;
                    timeLoadingCategory = (int) getTime;
                    categoryList.add(category);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }


    private void changeToProduct(Category category) {
        ProductFragment productFragment = new ProductFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("categoryId", category);
        productFragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_view_customer, productFragment)
                .addToBackStack(productFragment.getClass().getName())
                .commit();
    }


    private void changeToProductDetail(Product product) {
        ProducDetailFragment productDetailFragment = new ProducDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        productDetailFragment.setArguments(bundle);
        ((MainActivity) requireActivity()).showProductDetail(productDetailFragment);
    }

    private void getNamCustomerById(String customerId) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        Query query = myRef.orderByChild("id").equalTo(customerId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Khachang customer = dataSnapshot.getValue(Khachang.class);
//                    binding.userNameHome.setText(customer.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: " + error.getMessage());
            }
        });
    }

    public void seachList(String text) {
        ArrayList<Product> seachList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getTenDoUong().toLowerCase().contains(text.toLowerCase())) {
                seachList.add(product);
            }
        }
        if (seachList.isEmpty()) {
            getListProduct();
        } else {
            productAdapter.seachDatalist(seachList);
        }
    }


    private void getListImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        ArrayList<SlideModel> imageList = new ArrayList<>();
        storageRef.child("DoUongImages").listAll().addOnSuccessListener(listResult -> {
            for (StorageReference item : listResult.getItems()) {
                item.getDownloadUrl().addOnSuccessListener(uri -> {
                    imageList.add(new SlideModel(uri.toString(), ScaleTypes.FIT));
                    binding.imageSlider.setImageList(imageList);
                });
            }
        }).addOnFailureListener(e -> {
            Log.e("TAG", "onFailure: " + e.getMessage());
        });
    }

}