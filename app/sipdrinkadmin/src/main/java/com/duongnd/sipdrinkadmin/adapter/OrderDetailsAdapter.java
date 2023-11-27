package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.databinding.ItemDrinkOrderBinding;
import com.duongnd.sipdrinkadmin.model.OrderDetails;
import com.google.firebase.database.DatabaseReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.viewHolder> {
    private final ArrayList<OrderDetails>list;
    private final Context context;
    DatabaseReference databaseReference;

    public OrderDetailsAdapter(ArrayList<OrderDetails> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDrinkOrderBinding binding = ItemDrinkOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        OrderDetails orderDetails = list.get(position);
        holder.setData(orderDetails);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ItemDrinkOrderBinding binding;
        viewHolder(ItemDrinkOrderBinding itemDrinkOderDetailsBinding){
            super(itemDrinkOderDetailsBinding.getRoot());
            binding = itemDrinkOderDetailsBinding;
        }
        void setData(OrderDetails orderDetails){
            binding.tvNameProduct.setText(orderDetails.getNameProduct());
            binding.tvQuantity.setText("x"+orderDetails.getQuantity());
            Glide.with(context).load(orderDetails.getImageProduct())
                    .error(R.drawable.ic_image).into(binding.imgProduct);
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String money = numberFormat.format(orderDetails.getPrice());
            binding.tvPrice.setText(money);
        }
    }
}
