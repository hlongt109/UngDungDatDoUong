package com.longthph30891.ungdungdatdouong.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.longthph30891.ungdungdatdouong.R;
import com.longthph30891.ungdungdatdouong.databinding.FragmentLoginBinding;
import com.longthph30891.ungdungdatdouong.databinding.FragmentProfileBinding;
import com.longthph30891.ungdungdatdouong.model.Profile;

import org.w3c.dom.Document;

import java.io.IOException;

public class ProfileFragment extends Fragment {

    private MaterialCardView Selectphoto;
    private Uri ImageUri;
    private Bitmap bitmap;
    private FirebaseStorage storage;
    private FirebaseFirestore firebaseStorage;
    private StorageReference mStoragref;
    private String PhotoUrl;

    private FirebaseAuth auth;


    FragmentProfileBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater());

        firebaseStorage = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        mStoragref = storage.getReference();


        binding.materialCardView.setOnClickListener(view -> {

            CheckStoragaPernission();

        });
        binding.UploadInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadImage();
            }
        });


        return binding.getRoot();
    }

    private void CheckStoragaPernission() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }else {
                PickImgFromGallery();
            }
        }else {
            PickImgFromGallery();
        }
    }

    private void PickImgFromGallery() {
        Intent intent = new Intent();
        intent.setType("img/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        launcher.launch(intent);
    }
    ActivityResultLauncher<Intent> launcher
            = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
            result -> {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            if(data != null && data.getData() != null){
                                ImageUri = data.getData();

                                try {
                                    bitmap = MediaStore.Images.Media.getBitmap(
                                            getActivity().getContentResolver(),
                                            ImageUri
                                    );
                                }catch (IOException e){
                                    e.printStackTrace();
                                }

                                if(ImageUri != null){
                                    binding.carImg.setImageBitmap(bitmap);
                                }

                            }

                        }
            }
    );

    private void  UploadImage(){
        if(ImageUri != null){
            final  StorageReference myRef = mStoragref.child("photo/" + ImageUri.getLastPathSegment());
            myRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    myRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if (uri != null){
                                PhotoUrl = uri.toString();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void UploadCarInfor(){
        String name = binding.txtName.getText().toString().trim();
        String user = binding.txtUsername.getText().toString().trim();
        String date = binding.txtDate.getText().toString().trim();
        String phone = binding.txtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(user) && TextUtils.isEmpty(date) && TextUtils.isEmpty(phone)){
            Toast.makeText(getContext(), "ko dc ể trống", Toast.LENGTH_SHORT).show();
        }else {
            DocumentReference documentReference = firebaseStorage.collection("CarInfo").document();

            Profile profile = new Profile(name, user, date, phone, PhotoUrl);



        }

    }


}