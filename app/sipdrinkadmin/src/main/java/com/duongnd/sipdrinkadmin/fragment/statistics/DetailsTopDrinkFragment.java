package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentTopdrinkBinding;

public class DetailsTopDrinkFragment extends Fragment {
    public DetailsTopDrinkFragment() {}
    private FragmentTopdrinkBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopdrinkBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}