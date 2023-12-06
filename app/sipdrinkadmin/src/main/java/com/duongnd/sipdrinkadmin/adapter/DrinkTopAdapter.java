package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.databinding.ItemTopDrinkBinding;
import com.duongnd.sipdrinkadmin.model.DrinkTop;
import com.google.firebase.database.DatabaseReference;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DrinkTopAdapter extends RecyclerView.Adapter<DrinkTopAdapter.myViewHolder>{
    private ArrayList<DrinkTop>list;
    private Context context;
    private DatabaseReference databaseReference;

    public DrinkTopAdapter(ArrayList<DrinkTop> list, Context context, DatabaseReference databaseReference) {
        this.list = list;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopDrinkBinding itemTopDrinkBinding = ItemTopDrinkBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new myViewHolder(itemTopDrinkBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        DrinkTop drinkTop = list.get(position);
        holder.setDataOnView(drinkTop,context);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        ItemTopDrinkBinding binding;
        myViewHolder(ItemTopDrinkBinding itemTopDrinkBinding){
            super(itemTopDrinkBinding.getRoot());
            binding = itemTopDrinkBinding;
        }
        void setDataOnView(DrinkTop drinkTop,Context context){
            Glide.with(context).load(drinkTop.getImg())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(binding.imgProduct);
            binding.tvNameProduct.setText(drinkTop.getName());
            binding.tvTotalQuantity.setText(String.valueOf(drinkTop.getQuantity()));
            Locale locale = new Locale("vi","VN");
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
            String money = numberFormat.format(drinkTop.getTotalRevenue());
            binding.tvTotalPrice.setText(money);
        }
    }
}
