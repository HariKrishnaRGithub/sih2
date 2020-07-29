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
        slideModels.add(new SlideModel("https://www.ft.com/__origami/service/image/v2/images/raw/https%3A%2F%2Fd1e00ek4ebabms.cloudfront.net%2Fproduction%2Fe0ca7758-da2c-4f49-aa87-b35f4a7551a5.jpg?fit=scale-down&source=next&width=700","   Microsoft"));
        slideModels.add(new SlideModel("https://zdnet2.cbsistatic.com/hub/i/r/2017/03/13/9771951a-1439-4fab-8424-ca024674545e/resize/770xauto/735eb233abb7fad6d8bd505c8c2adf57/apple-event.jpg","   Apple"));
        slideModels.add(new SlideModel("https://cdnuploads.aa.com.tr/uploads/Contents/2019/02/06/thumbs_b_c_2f7bc0bac6400f27b8bdec0cf6d40f7d.jpg?v=181112","   WhatsApp"));
        slideModels.add(new SlideModel("https://www.infosys.com/content/dam/infosys-web/en/global-resource/media-resources/images/Bangalore-New-001.jpg","   Infosys"));
        //https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg
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
