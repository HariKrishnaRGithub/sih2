package com.example.sih2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AccountTypeActivity extends AppCompatActivity {

    private SharedPrefrencesHelper sharedPrefrencesHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_type);
        CardView btnEmployee=(CardView) findViewById(R.id.textEmployee);
        CardView btnCompany=(CardView) findViewById(R.id.textCompany);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(this);
        btnEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefrencesHelper.setAccountType("employee");
                Intent intent=new Intent(AccountTypeActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefrencesHelper.setAccountType("company");
                Intent intent=new Intent(AccountTypeActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}