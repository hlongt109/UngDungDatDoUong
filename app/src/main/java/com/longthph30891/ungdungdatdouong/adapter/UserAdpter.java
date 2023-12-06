package com.longthph30891.ungdungdatdouong.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.activity.ChatActivity;
import com.longthph30891.ungdungdatdouong.activity.chatwindo;
import com.longthph30891.ungdungdatdouong.model.Admin;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.viewholder> {
    Context mainActivity;
    ArrayList<Admin> usersArrayList;
    public UserAdpter(ChatActivity mainActivity, ArrayList<Admin> usersArrayList) {
        this.mainActivity=mainActivity;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Admin admin = usersArrayList.get(position);
        holder.username.setText(admin.getFullName());
        holder.userstatus.setText(admin.getEmail());
        Glide.with(mainActivity).load(admin.getImg()).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, chatwindo.class);
                intent.putExtra("nameeee",admin.getFullName());
                intent.putExtra("reciverImg",admin.getImg());
                intent.putExtra("uid",admin.getId());
                mainActivity.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userimg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userimg = itemView.findViewById(R.id.userimg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);
        }
    }
}
