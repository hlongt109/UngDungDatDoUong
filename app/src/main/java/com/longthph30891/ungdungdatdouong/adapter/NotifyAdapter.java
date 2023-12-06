package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.longthph30891.ungdungdatdouong.databinding.ItemNotificationBinding;
import com.longthph30891.ungdungdatdouong.model.Order;

import java.util.ArrayList;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.notifiViewHolder>{
    private final ArrayList<Order>list;
    private final Context context;
    private DatabaseReference databaseReference;

    public NotifyAdapter(ArrayList<Order> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public notifiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationBinding binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new notifiViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull notifiViewHolder holder, int position) {
        Order notification = list.get(position);
        holder.setDataOnView(notification);
        holder.itemView.setOnClickListener(view -> {


        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class notifiViewHolder extends RecyclerView.ViewHolder{
        ItemNotificationBinding binding;
        notifiViewHolder(ItemNotificationBinding itemNotificationBinding){
            super(itemNotificationBinding.getRoot());
            binding = itemNotificationBinding;
        }
        void setDataOnView(Order notification){
            if(notification.getStatusOrder().equals("danggiao")){
                binding.tvNotificatonContent.setText("Shipper bảo rằng : đơn hàng "+notification.getorderId()+" đang trên đường giao đến bạn");
            } else if (notification.getStatusOrder().equals("dahuy")) {
                binding.tvNotificatonContent.setText("Đơn hàng "+notification.getorderId()+" đã hủy");
            }
            binding.tvDate.setText(notification.getDateOrder());
        }
    }
}
