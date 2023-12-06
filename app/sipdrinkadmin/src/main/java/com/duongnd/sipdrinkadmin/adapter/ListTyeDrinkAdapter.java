package com.duongnd.sipdrinkadmin.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.model.LoaiDoUong;

import java.util.ArrayList;

public class ListTyeDrinkAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<LoaiDoUong> list;

    public ListTyeDrinkAdapter(Context context, ArrayList<LoaiDoUong> list) {
        this.context = context;
        this.list = list;
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
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.spinner,viewGroup,false);
        TextView tvTenLoai = view.findViewById(R.id.tv_TenLoai_spinner);
        tvTenLoai.setText(list.get(i).getTypeName());
        return view;
    }
}
