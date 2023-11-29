package com.duongnd.sipdrinkadmin.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duongnd.sipdrinkadmin.databinding.ItemQlUserBinding;

public class ListUserAdapter {
    class viewHolder extends RecyclerView.ViewHolder{
        ItemQlUserBinding binding;
        viewHolder(ItemQlUserBinding itemQlUserBinding){
            super(itemQlUserBinding.getRoot());
            binding = itemQlUserBinding;
        }

    }
}
