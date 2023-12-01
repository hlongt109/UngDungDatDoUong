package com.duongnd.sipdrinkadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.Utilities.OnClickDrinkInterface;
import com.duongnd.sipdrinkadmin.fragment.DrinksListFragment;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DoUongAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<DoUong> list;
    private DatabaseReference databaseReference;
    OnClickDrinkInterface onClickDrinkInterface;

    public DoUongAdapter(Context context, ArrayList<DoUong> list,DatabaseReference databaseReference) {
        this.context = context;
        this.list = list;
        this.databaseReference = databaseReference;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {

        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        view = inflater.inflate(R.layout.item_drink,null);
        ProgressBar progressBar = view.findViewById(R.id.progressBarItemDrink);
        TextView tvTen = view.findViewById(R.id.tvNameDrink_item);
        TextView tvGia = view.findViewById(R.id.tvGia);
        TextView tvTrangThai = view.findViewById(R.id.tvTrangThai);
        ImageView img = view.findViewById(R.id.img_Drink_item);
        DoUong doUong = list.get(i);
        Glide.with(context).load(doUong.getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(img);
        tvTen.setText(list.get(i).getTenDoUong());
        tvGia.setText(String.valueOf(list.get(i).getGia()));
        if(list.get(i).getTrangThai().equals("DangBan")){
            tvTrangThai.setText("Còn sản phẩm");
            tvTrangThai.setTextColor(ContextCompat.getColor(context,R.color.green));
        }else if (list.get(i).getTrangThai().equals("HetHang")){
            tvTrangThai.setText("Tạm thời hết hàng");
            tvTrangThai.setTextColor(ContextCompat.getColor(context,R.color.red));
        }else if (list.get(i).getTrangThai().equals("Moi")){
            tvTrangThai.setText("Mới");
            tvTrangThai.setTextColor(ContextCompat.getColor(context,R.color.blue));
        }
        //
        view.setOnClickListener(view1 -> {
            DrinksListFragment drinksListFragment = new DrinksListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("id",doUong.getIdDoUong());
            bundle.putString("name",doUong.getTenDoUong());
            bundle.putString("idType",doUong.getMaLoai());
            bundle.putDouble("price",doUong.getGia());
            bundle.putString("status",doUong.getTrangThai());
            bundle.putString("image",doUong.getImage());
            drinksListFragment.setArguments(bundle);
            onClickDrinkInterface.onClickMenu(doUong,view1);
        });
        return view;
    }
    public void showMenu(OnClickDrinkInterface onClickDrinkInterface){
        this.onClickDrinkInterface = onClickDrinkInterface;
    }
}
