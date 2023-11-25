package com.duongnd.sipdrinkadmin.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetDeliveringList;
import com.duongnd.sipdrinkadmin.BottomDiaLog.BottomSheetOrderList;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentStatisticBinding;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.duongnd.sipdrinkadmin.model.TestData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticFragment extends Fragment {
    public StatisticFragment() {}
    private FragmentStatisticBinding binding;
    private List<String> listTestValues;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatisticBinding.inflate(inflater,container,false);
        binding.cardOrderConfirm.setOnClickListener(view -> {
            BottomSheetOrderList bottomSheetOrderList = new BottomSheetOrderList();
            bottomSheetOrderList.show(getChildFragmentManager(),"BottomSheetOrderList");
        });
        binding.cardDelivering.setOnClickListener(view -> {
            BottomSheetDeliveringList bottomSheetDeliveringList = new BottomSheetDeliveringList();
            bottomSheetDeliveringList.show(getChildFragmentManager(),"BottomSheetDeliveringList");
        });
        binding.chooseStatistics.setOnClickListener(view -> {
            openPopupMenu(view);
        });
        TestData();
        return binding.getRoot();
    }

    private void openPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.option_menu_revenua);
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_Daily) {
                binding.tvRevenue.setText("Daily");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Weekly) {
                binding.tvRevenue.setText("Weekly");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Monthly) {
                binding.tvRevenue.setText("Monthly");

                return true;
            } else if (menuItem.getItemId() == R.id.nav_Yearly) {
                binding.tvRevenue.setText("Yearly");

                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }

    private void TestData(){
        Description description = new Description();
        description.setText("Drink type record");
        description.setPosition(150f,150f);
        binding.lineChart.setDescription(description);
        binding.lineChart.getAxisRight().setDrawLabels(false);

        listTestValues = Arrays.asList("January","February","March","April","May","June","July","August","September","October","November","December");

        XAxis xAxis = binding.lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(listTestValues));
        xAxis.setLabelCount(12);
        xAxis.setGranularity(1f);

        YAxis yAxis = binding.lineChart.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(100f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        List<Entry>entries = new ArrayList<>();
        entries.add(new Entry(0,20f));
        entries.add(new Entry(1,10f));
        entries.add(new Entry(2,25f));
        entries.add(new Entry(3 ,40f));

        List<Entry>entries2 = new ArrayList<>();
        entries2.add(new Entry(0,5f));
        entries2.add(new Entry(1,30f));
        entries2.add(new Entry(2,20f));

        LineDataSet dataSet = new LineDataSet(entries,"Coffee");
        dataSet.setColor(Color.BLUE);
        LineDataSet dataSet2 = new LineDataSet(entries2,"Tra Sua");
        dataSet2.setColor(Color.RED);

        LineData lineData = new LineData(dataSet,dataSet2);
        binding.lineChart.setData(lineData);
        binding.lineChart.invalidate();
    }
}