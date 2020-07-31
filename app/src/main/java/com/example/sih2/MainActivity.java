package com.example.sih2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        String username = sharedPrefrencesHelper.getUsername();
        String accountType= sharedPrefrencesHelper.getAccountType();
        if (username == null || username.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else if(accountType.equals("employee")){
            startActivity(new Intent(this, EmployeeHome.class));
            finish();
        }else if(accountType.equals("company")){
            startActivity(new Intent(this, CompanyHome.class));
            finish();
        }

    }
}