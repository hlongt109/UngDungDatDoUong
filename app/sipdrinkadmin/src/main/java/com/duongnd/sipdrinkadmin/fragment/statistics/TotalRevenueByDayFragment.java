package com.duongnd.sipdrinkadmin.fragment.statistics;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.FragmentTotalRevenueByDayBinding;
import com.duongnd.sipdrinkadmin.model.Order;
import com.github.mikephil.charting.data.BarData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TotalRevenueByDayFragment extends Fragment {
    public TotalRevenueByDayFragment() {}
    private FragmentTotalRevenueByDayBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTotalRevenueByDayBinding.inflate(inflater,container,false);
        calculateRevenue();
        return binding.getRoot();
    }

    private void calculateRevenue() {
        binding.btnDayStart.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, i, i1, i2) ->
                    binding.tvDayStart.setText(i2+"/"+(i1 + 1) +"/"+i),year,month,day);
            datePickerDialog.show();
        });
        binding.btnDayTo.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (datePicker, i, i1, i2) ->
                    binding.tvDayTo.setText(i2+"/"+(i1 + 1)+"/"+i),year,month,day);
            datePickerDialog.show();
        });
        binding.btnXem.setOnClickListener(view -> {
            if (isValidDetails()){
                totalRevenueInRange(binding.tvDayStart.getText().toString(),binding.tvDayTo.getText().toString());
            }
        });
    }
    private Boolean isValidDetails(){
        if(binding.tvDayStart.getText().toString().trim().isEmpty()){
            binding.tilDayStart.setError("Ngày trống");
            return false;
        } else if (binding.tvDayTo.getText().toString().trim().isEmpty()) {
            binding.tilDayTo.setError("Ngày trống");
            return false;
        }else {
            binding.tilDayStart.setError(null);
            binding.tilDayTo.setError(null);
            return true;
        }
    }

    private void totalRevenueInRange(String startDate, String endDate) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Order");
        databaseReference.orderByChild("statusOrder").equalTo("dathanhtoan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double totalRevenua = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Order order2 = dataSnapshot.getValue(Order.class);
                    if ("dathanhtoan".equals(order2.getStatusOrder()) && isDateInRange(order2.getDateOrder(), startDate, endDate)) {
                        totalRevenua += order2.getTotalPrice();
                    }
                }
                Locale locale = new Locale("vi", "VN");
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
                String money = numberFormat.format(totalRevenua);
                binding.tvTotalRevenue.setText(money);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isDateInRange(String getDate, String startDate, String endDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date targetDateTime = dateFormat.parse(getDate);
            Date startDateTime = dateFormat.parse(startDate);
            Date endDateTime = dateFormat.parse(endDate);

            return targetDateTime != null && startDateTime != null && endDateTime != null &&
                    (targetDateTime.equals(startDateTime) || targetDateTime.after(startDateTime)) &&
                    (targetDateTime.equals(endDateTime) || targetDateTime.before(endDateTime));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}