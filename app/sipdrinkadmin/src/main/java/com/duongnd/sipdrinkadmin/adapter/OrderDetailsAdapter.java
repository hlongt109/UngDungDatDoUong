package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.databinding.ItemOderBinding;
import com.duongnd.sipdrinkadmin.model.ChiTietDonHang;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.viewHolder> {
    private final ArrayList<ChiTietDonHang>list;
    private final Context context;

    public OrderDetailsAdapter(ArrayList<ChiTietDonHang> list, Context context) {
        this.list = list;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOderBinding binding = ItemOderBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ItemOderBinding binding;
        viewHolder(ItemOderBinding itemDrinkOderDetailsBinding){
            super(itemDrinkOderDetailsBinding.getRoot());
            binding = itemDrinkOderDetailsBinding;
        }
        void setData(ChiTietDonHang chiTietDonHang){
            binding.tvNameProduct.setText(chiTietDonHang.getIdDoUong());
            binding.tvPrice.setText("30000");
            binding.tvQuantity.setText(chiTietDonHang.getSoLuong());
        }
    }
}
