package com.example.sih2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsFragment extends Fragment {

    SharedPrefrencesHelper sharedPrefrencesHelper;

    private Switch aSwitch;

    public static final String MyPREFERENCES="nightModePrefs";
    public static final String KEY_ISNIGHTMODE="isNightMode";
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_settings,container,false);
        sharedPrefrencesHelper =new SharedPrefrencesHelper(SettingsFragment.this.getActivity());
        super.onCreate(savedInstanceState);

        sharedPreferences=getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        aSwitch=view.findViewById(R.id.switch_darkmode);

        checkNightModeActivated();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    getActivity().recreate();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    getActivity().recreate();
                }
            }
        });

        return view;
    }
    private void saveNightModeState(boolean nightmode) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE,nightmode);
        editor.apply();
    }

    public void checkNightModeActivated(){
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE,false)){
            aSwitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            aSwitch.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}