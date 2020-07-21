package com.example.sih2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AboutUsFragment extends Fragment {

    AboutUsFragment.aboutusSelected listener;
    SharedPrefrencesHelper sharedPrefrencesHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about_us,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof AboutUsFragment.aboutusSelected){
            //listener = (AboutUsFragment.aboutusSelected) context;
            listener=(AboutUsFragment.aboutusSelected) context;

        }else{
            throw new ClassCastException(context.toString()+" must implement listener");
        }

    }

    public interface aboutusSelected{
        public void btnProfileClicked();
    }
}