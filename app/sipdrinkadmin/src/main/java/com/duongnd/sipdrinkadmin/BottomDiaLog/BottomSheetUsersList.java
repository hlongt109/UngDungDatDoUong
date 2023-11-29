package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duongnd.sipdrinkadmin.databinding.BottomSheetUsersListBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetUsersList extends BottomSheetDialogFragment {
    private BottomSheetUsersListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetUsersListBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }
}
