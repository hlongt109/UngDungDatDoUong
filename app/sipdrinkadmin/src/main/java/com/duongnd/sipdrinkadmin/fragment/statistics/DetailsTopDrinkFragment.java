package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;

public class DetailsTopDrinkFragment extends Fragment {
    public DetailsTopDrinkFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_statistics_top_drink, container, false);
    }
}