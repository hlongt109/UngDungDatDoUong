package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.ItemOrderDetailBinding;
import com.longthph30891.ungdungdatdouong.model.OrderDetail;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class HistoryDetailAdapter extends RecyclerView.Adapter<HistoryDetailAdapter.viewHolder> {
    private final ArrayList<OrderDetail> list;
    private final Context context;
    DatabaseReference databaseReference;

    public HistoryDetailAdapter(ArrayList<OrderDetail> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        OrderDetail orderDetails = list.get(position);
        holder.setData(orderDetails);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ItemOrderDetailBinding binding;
        viewHolder(ItemOrderDetailBinding itemOrderDetailBinding){
            super(itemOrderDetailBinding.getRoot());
            binding = itemOrderDetailBinding;
        }
        void setData(OrderDetail orderDetails){
            binding.tvNameProduct.setText(orderDetails.getNameProduct());
            binding.tvQuantity.setText("x"+orderDetails.getQuantity());
            Glide.with(context).load(orderDetails.getImageProduct())
                    .error(R.drawable.profilebkg).into(binding.imgProduct);
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String money = numberFormat.format(orderDetails.getPrice());
            binding.tvPrice.setText(money);
        }
    }
}
