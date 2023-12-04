package com.duongnd.sipdrinkadmin.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.model.Khachang;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private ArrayList<Khachang> list;
    Context context;

    public UserAdapter(ArrayList<Khachang> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        Khachang khachang = list.get(position);
        holder.txtName.setText(khachang.getFullName());
        holder.txtPhone.setText(khachang.getEmail());

        Glide.with(context).load(khachang.getImg()).error(R.drawable.profilebkg).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void seachDatalist(ArrayList<Khachang> sacrhList){
        list = sacrhList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_full_name);
            txtPhone = itemView.findViewById(R.id.txt_phone);
            imageView = itemView.findViewById(R.id.img_avata);

        }
    }
}
