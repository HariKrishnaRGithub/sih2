package com.example.sih2;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

public class CompHomeFragment extends Fragment {
    SharedPrefrencesHelper sharedPrefrencesHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_comp_home,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }
}
