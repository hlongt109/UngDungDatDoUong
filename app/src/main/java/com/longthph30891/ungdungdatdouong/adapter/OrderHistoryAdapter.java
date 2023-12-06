package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.longthph30891.ungdungdatdouong.R;

import com.longthph30891.ungdungdatdouong.databinding.ItemOderHistoryBinding;
import com.longthph30891.ungdungdatdouong.databinding.ItemOrderBinding;
import com.longthph30891.ungdungdatdouong.fragment.main_home.HistoryDetailFragment;
import com.longthph30891.ungdungdatdouong.model.Order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.myViewHolder> {
    private ArrayList<Order> list;
    private Context context;
    private DatabaseReference databaseReference;

    public OrderHistoryAdapter(ArrayList<Order> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOderHistoryBinding itemOderHistoryBinding = ItemOderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new myViewHolder(itemOderHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Order order = list.get(position);
        holder.setDataOnView(order,context);

        holder.itemView.setOnClickListener(view -> {
            showOrderDetails(order);
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder {
        ItemOderHistoryBinding binding;
        myViewHolder(ItemOderHistoryBinding itemOderHistoryBinding) {
            super(itemOderHistoryBinding.getRoot());
            binding = itemOderHistoryBinding;

        }

        void setDataOnView(Order order,Context context) {
            binding.tvIdOder.setText(order.getorderId());
            binding.tvNgayOrder.setText(order.getDateOrder());
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String tongTien = numberFormat.format(order.getTotalPrice());
            binding.tvTotalPrice.setText(tongTien);

        }
    }
    private void showOrderDetails(Order order) {
        HistoryDetailFragment historyDetailFragment = new HistoryDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",order.getorderId());
        bundle.putString("idUser",order.getIdUser());
        bundle.putString("dateOrder",order.getDateOrder());
        bundle.putString("nameCustomer",order.getnameCustomer());
        bundle.putString("phoneNumber",order.getphoneNumber());
        bundle.putString("address",order.getaddress());
        bundle.putString("statusOrder",order.getStatusOrder());
        bundle.putDouble("totalPrice",order.getTotalPrice());
        historyDetailFragment.setArguments(bundle);
        //
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_main_view_customer, historyDetailFragment)
                .addToBackStack(null)
                .commit();
    }
}

