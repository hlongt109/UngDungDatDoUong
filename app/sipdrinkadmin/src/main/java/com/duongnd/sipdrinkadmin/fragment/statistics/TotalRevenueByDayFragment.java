package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentTotalRevenueByDayBinding;

public class TotalRevenueByDayFragment extends Fragment {
    public TotalRevenueByDayFragment() {}
    private FragmentTotalRevenueByDayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTotalRevenueByDayBinding.inflate(inflater,container,false);
        binding.btnDayStart.setOnClickListener(view -> {

        });
        binding.btnDayTo.setOnClickListener(view -> {

        });
        return binding.getRoot();
    }
}