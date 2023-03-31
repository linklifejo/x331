package com.hanul.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hanul.myapplication.databinding.SubFragBinding;

public class FragmentSub extends Fragment {
    private SubFragBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = SubFragBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

}
