package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ActivityMainBinding;
import com.duongnd.sipdrinkadmin.databinding.ItemNotificationBinding;
import com.duongnd.sipdrinkadmin.fragment.StatisticFragment;
import com.duongnd.sipdrinkadmin.model.Notification;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.notifiViewHolder>{
    private final ArrayList<Order>list;
    private final Context context;
    private DatabaseReference databaseReference;

    public NotificationAdapter(ArrayList<Order> list, Context context, DatabaseReference databaseReference) {
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
           replaceFrg(new StatisticFragment(),context);
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
            if(notification.getStatusOrder().equals("choxacnhan")){
                binding.tvNotification.setText("Hi, bạn có đơn hàng mới");
            } else if (notification.getStatusOrder().equals("dahuy")) {
                binding.tvNotification.setText("Đơn hàng "+notification.getOrderId()+" đã hủy");
            }
            binding.tvOrderId.setText(notification.getOrderId());
            binding.tvDate.setText(notification.getDateOrder());
        }
    }
    public void replaceFrg(Fragment frg,Context context) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.fragment_container, frg).commit();
    }
}
