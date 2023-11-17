package com.duongnd.sipdrinkadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.model.DoUong;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DoUongAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<DoUong> list;
    private DatabaseReference databaseReference;

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
        TextView tvTen = view.findViewById(R.id.tvNameDrink_item);
        TextView tvGia = view.findViewById(R.id.tvGia);
//        TextView tvTrangThai = view.findViewById(R.id.tvTrangThai);
        ImageView img = view.findViewById(R.id.img_Drink_item);
        ImageView icon = view.findViewById(R.id.imgFavorite);
        Glide.with(context).load(list.get(i).getImage())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(img);
        tvTen.setText(list.get(i).getTenDoUong());
        tvGia.setText(list.get(i).getGia());
//        if(list.get(i).getTrangThai().equals("DangBan")){
//            tvTrangThai.setText("");
//        }else if (list.get(i).getTrangThai().equals("HetHang")){
//            tvTrangThai.setText("Tạm thời hết hàng");
//        }
        return view;
    }
}
