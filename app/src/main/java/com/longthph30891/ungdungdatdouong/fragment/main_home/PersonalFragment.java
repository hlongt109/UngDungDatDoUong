package com.longthph30891.ungdungdatdouong.fragment.main_home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.activity.ChatActivity;
import com.longthph30891.ungdungdatdouong.activity.LoginRegisterActivity;
import com.longthph30891.ungdungdatdouong.databinding.FragmentPersonalBinding;
import com.longthph30891.ungdungdatdouong.fragment.login_register.UserPassFragment;
import com.longthph30891.ungdungdatdouong.model.Khachang;


public class PersonalFragment extends Fragment {
    private FragmentPersonalBinding binding;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPersonalBinding.inflate(inflater,container,false);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Khachang khachang = snapshot.getValue(Khachang.class);
                binding.tvName.setText(khachang.getFullName());

                if(khachang.getImg().equals("img")){
                    binding.imgAvata.setImageResource(R.drawable.profilebkg);
                }else {
                    Glide.with(getContext()).load(khachang.getImg()).error(R.drawable.profilebkg).into(binding.imgAvata);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnQlyHoaDon.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_view_customer, new OrderHistoryFragment())
                    .addToBackStack(OrderHistoryFragment.class.getName())
                    .commit();

        });
        binding.btnChat.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), ChatActivity.class));
        });

        binding.btnTaiKoanVaBaoMat.setOnClickListener(view -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_main_view_customer, new UserPassFragment())
                    .addToBackStack(UserPassFragment.class.getName())
                    .commit();

        });
        binding.btnLogout.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Bạn có chắn muốn đăng xuất không ?");
            builder.setNegativeButton("Trở lại", null);
            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                auth.signOut();
                Intent intent = new Intent(getContext(), LoginRegisterActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
            builder.create().show();
        });
        return binding.getRoot();
    }




}