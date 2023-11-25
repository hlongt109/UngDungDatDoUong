package com.duongnd.sipdrinkadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;

import com.duongnd.sipdrinkadmin.Utilities.Category_interface;
import com.duongnd.sipdrinkadmin.fragment.DrinksListFragment;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;


public class LoaiDoUongAdapter extends RecyclerView.Adapter<LoaiDoUongAdapter.viewHolder> {
    private final Context context;
    private final ArrayList<LoaiDoUong> list;
    private DatabaseReference databaseReference;
    Category_interface categoryInterface;

    public LoaiDoUongAdapter(Context context, ArrayList<LoaiDoUong> list, DatabaseReference database) {
        this.context = context;
        this.list = list;
        this.databaseReference = database;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_loai_do_uong, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        LoaiDoUong loaiDoUong = list.get(position);
        holder.tvTenLoai.setText(list.get(position).getTypeName());
        Glide.with(context).load(loaiDoUong.getTypeImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.ic_image)
                .into(holder.imgLoai).getRequest();
        holder.itemView.setOnClickListener(view -> {
            showListDoUong(loaiDoUong);
        });
        holder.btnTienIch.setOnClickListener(view -> {
            categoryInterface.onClickMenu(loaiDoUong,view);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imgLoai, btnTienIch;
        TextView tvTenLoai;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imgLoai = itemView.findViewById(R.id.image_Loai_item);
            tvTenLoai = itemView.findViewById(R.id.tvTenLoai_item);
            btnTienIch = itemView.findViewById(R.id.imv_tien_ich);
        }
    }

    private void showListDoUong(LoaiDoUong loaiDoUong) {
        DrinksListFragment drinksListFragment = new DrinksListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("maLoai", loaiDoUong.getTypeId());
        bundle.putString("tenLoai",loaiDoUong.getTypeName());
        drinksListFragment.setArguments(bundle);
        //
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, drinksListFragment)
                .addToBackStack(null)
                .commit();
    }
    public void showMenu(Category_interface categoryInterface){
        this.categoryInterface = categoryInterface;
    }
}
