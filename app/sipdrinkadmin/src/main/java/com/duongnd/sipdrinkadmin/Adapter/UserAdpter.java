package com.duongnd.sipdrinkadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duongnd.sipdrinkadmin.R;
import com.duongnd.sipdrinkadmin.activity.ChatActivity;
import com.duongnd.sipdrinkadmin.activity.chatwindo;
import com.duongnd.sipdrinkadmin.model.Khachang;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdpter extends RecyclerView.Adapter<UserAdpter.viewholder> {
    Context context;
    ArrayList<Khachang> usersArrayList;

    public UserAdpter(ChatActivity chatActivity, ArrayList<Khachang> usersArrayList) {
        this.context = chatActivity;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        Khachang khachang = usersArrayList.get(position);
        holder.username.setText(khachang.getFullName());
        holder.userstatus.setText(khachang.getEmail());
        Glide.with(context).load(khachang.getImg()).into(holder.userimg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, chatwindo.class);
                intent.putExtra("nameeee", khachang.getFullName());
                intent.putExtra("reciverImg", khachang.getImg());
                intent.putExtra("uid", khachang.getId());
                context.startActivity(intent);
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
