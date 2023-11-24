package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.longthph30891.ungdungdatdouong.activity.MainActivity;
import com.longthph30891.ungdungdatdouong.databinding.FragmentProducDetailBinding;
import com.longthph30891.ungdungdatdouong.model.Product;
import com.squareup.picasso.Picasso;

import java.util.Objects;

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
                binding.toolbar.setTitle(product.getTenDoUong());
                Picasso.get().load(product.getImage()).into(binding.imgProductDetail); //Picasso
            }
        }


    }
}