package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ItemOrderBinding;
import com.duongnd.sipdrinkadmin.fragment.OrderDetailsFragment;
import com.duongnd.sipdrinkadmin.model.Order;
import com.google.firebase.database.DatabaseReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDrinkAdapter extends RecyclerView.Adapter<OrderDrinkAdapter.myViewHolder> {
    private ArrayList<Order> list;
    private Context context;
    private DatabaseReference databaseReference;

    public OrderDrinkAdapter(ArrayList<Order> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public OrderDrinkAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding itemOrderBinding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDrinkAdapter.myViewHolder(itemOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDrinkAdapter.myViewHolder holder, int position) {
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
        ItemOrderBinding binding;
        myViewHolder(ItemOrderBinding itemOrderBinding) {
            super(itemOrderBinding.getRoot());
            binding = itemOrderBinding;

        }

        void setDataOnView(Order order,Context context) {
            binding.tvIdOder.setText(order.getOrderId());
            binding.tvNgayOrder.setText(order.getDateOrder());
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String tongTien = numberFormat.format(order.getTotalPrice());
            binding.tvTotalPrice.setText(tongTien);
            if (order.getStatusOrder().equals("choxacnhan")) {
                binding.tvStatus.setText("Đang chờ xác nhận");
            } else if (order.getStatusOrder().equals("danggiao")) {
                binding.tvStatus.setText("Đang giao hàng");
                binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else if (order.getStatusOrder().equals("dathanhtoan")) {
                binding.tvStatus.setText("Giao hàng thành công");
                binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.green));
            }else if (order.getStatusOrder().equals("dahuy")) {
                binding.tvStatus.setText("Đơn hàng đã hủy");
                binding.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.red));
            }
        }
    }
    private void showOrderDetails(Order order) {
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("orderId",order.getOrderId());
        bundle.putString("idUser",order.getIdUser());
        bundle.putString("dateOrder",order.getDateOrder());
        bundle.putString("nameCustomer",order.getNameCustomer());
        bundle.putString("phoneNumber",order.getPhoneNumber());
        bundle.putString("address",order.getAddress());
        bundle.putString("statusOrder",order.getStatusOrder());
        bundle.putDouble("totalPrice",order.getTotalPrice());
        orderDetailsFragment.setArguments(bundle);
        //
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, orderDetailsFragment)
                .addToBackStack(null)
                .commit();
    }
}
