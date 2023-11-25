package com.duongnd.sipdrinkadmin.BottomDiaLog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.duongnd.sipdrinkadmin.databinding.BottomSheetDeliveringListBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDeliveringList extends BottomSheetDialogFragment {
    private BottomSheetDeliveringListBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetDeliveringListBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
}
