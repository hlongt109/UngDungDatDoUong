package com.duongnd.sipdrinkadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentStatisticBinding;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {
    public StatisticFragment() {}
    private FragmentStatisticBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater,container,false);
        ArrayList<BarEntry> vistors = new ArrayList<>();
        vistors.add(new BarEntry(2018,420));
        vistors.add(new BarEntry(2019,455));
        vistors.add(new BarEntry(2020,505));
        vistors.add(new BarEntry(2021,670));
        vistors.add(new BarEntry(2022,420));
        vistors.add(new BarEntry(2023,888));

        BarDataSet barDataSet = new BarDataSet(vistors,"Visistors");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);
        binding.barChart.setFitBars(true);
        binding.barChart.setData(barData);

        return binding.getRoot();
    }
}