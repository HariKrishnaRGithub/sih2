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

public class EmpHomeFragment extends Fragment{
    employeeHomeSelected listener;
    SharedPrefrencesHelper sharedPrefrencesHelper;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_emp_home,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        ImageSlider imageSlider=view.findViewById(R.id.slider);

        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("https://news.efinancialcareers.com/binaries/content/gallery/efinancial-careers/articles/2019/05/google-san-fran.jpg","  Google"));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg","   Microsoft"));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg","   Apple"));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg","   WhatsApp"));
        slideModels.add(new SlideModel("https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg","   Infosys"));
        imageSlider.setImageList(slideModels,true);



        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof employeeHomeSelected){
            listener = (employeeHomeSelected) context;

        }else{
            throw new ClassCastException(context.toString()+" must implement listener");
        }

    }

    public interface employeeHomeSelected{
        public void btnProfileClicked();
    }
}
