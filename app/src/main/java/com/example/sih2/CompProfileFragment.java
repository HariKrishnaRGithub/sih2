package com.example.sih2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CompProfileFragment extends Fragment {
    SharedPrefrencesHelper sharedPrefrencesHelper;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_comp_profile,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }
}
