package com.example.sih2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutUsFragment extends Fragment {

    SharedPrefrencesHelper sharedPrefrencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about_us,container,false);
        sharedPrefrencesHelper =new SharedPrefrencesHelper(AboutUsFragment.this.getActivity());
        // write the code for about here

        return view;
    }
}