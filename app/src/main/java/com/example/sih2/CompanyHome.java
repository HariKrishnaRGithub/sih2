package com.example.sih2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CompanyHome extends AppCompatActivity {
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView firstname, lastname, usernamee, email, accType;
    Button logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this);
        firstname = findViewById(R.id.firstnameemp);
        lastname = findViewById(R.id.lastnameemp);
        usernamee = findViewById(R.id.usernameemp);
        email = findViewById(R.id.emailemp);
        accType=findViewById(R.id.acctypeemp);
        logoutBtn = findViewById(R.id.logoutBtn);
        firstname.setText(sharedPrefrencesHelper.getFirstname());
        lastname.setText(sharedPrefrencesHelper.getLastname());
        usernamee.setText(sharedPrefrencesHelper.getUsername());
        email.setText(sharedPrefrencesHelper.getEmail());
        accType.setText(sharedPrefrencesHelper.getAccountType());
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefrencesHelper.setFirstname(null);
                sharedPrefrencesHelper.setLastname(null);
                sharedPrefrencesHelper.setUsername(null);
                sharedPrefrencesHelper.setEmail(null);
                sharedPrefrencesHelper.setAccountType(null);
                startActivity(new Intent(CompanyHome.this, LoginActivity.class));
                finish();
            }
        });
    }
}