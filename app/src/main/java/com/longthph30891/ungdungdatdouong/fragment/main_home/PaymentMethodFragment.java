package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.FragmentPaymentMethodBinding;


public class PaymentMethodFragment extends Fragment {

    private FragmentPaymentMethodBinding binding;
    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(inflater, container, false);

        binding.txtPaymentOffline.setOnClickListener(v -> {
            binding.txtPaymentOffline.setTextCursorDrawable(R.drawable.ic_check);
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}