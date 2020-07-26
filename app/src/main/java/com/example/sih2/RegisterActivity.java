package com.example.sih2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    TextView loginTV,firstNameTV;
    EditText fisrtName, lastName, username, email, password, password1;
    Button registerBtn;
    CardView lastnameCardView;
    private RequestQueue rQueue;
    private SharedPrefrencesHelper sharedPrefrencesHelper;

    private CheckBox checkbox,checkbox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        sharedPrefrencesHelper=new SharedPrefrencesHelper(this);
        String acc=sharedPrefrencesHelper.getAccountType();



        loginTV = findViewById(R.id.loginTV);
        fisrtName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        username = findViewById(R.id.usernameemp);
        email = findViewById(R.id.emailemp);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);
        registerBtn = findViewById(R.id.registerBtn);
        lastnameCardView=findViewById(R.id.lastnameCardView);
        firstNameTV=findViewById(R.id.firstNameTV);

        if(acc.equals("employee")){

        }
        if(acc.equals("company")){
                lastnameCardView.setVisibility(View.GONE);
                firstNameTV.setText("Company Name");
                firstNameTV.setHint("Company Name");
        }

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), LoginActivity.class));
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                if (mail.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return;
                }
                Random random =new Random();
                final String randomNumber = String.format("%04d", random.nextInt(10000));
                sendOTP(randomNumber,mail);

                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.email_verification, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button actualRegisterButton=dialogView.findViewById(R.id.actualRegisterButton);
                actualRegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText otpNumber=dialogView.findViewById(R.id.otpNumber);
                        if(otpNumber.getText().toString().equals(randomNumber)){
                            registerAction();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        //show/hide password
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        //show/hide confirm password
        checkbox1 = (CheckBox) findViewById(R.id.checkbox1);
        checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    // show password
                    password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // hide password
                    password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    private void sendOTP(final String randomNumber, final String mail) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "sendOTPforRegister.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(RegisterActivity.this, "OTP sent successfully, check your mail", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", mail);
                params.put("otp",randomNumber);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(stringRequest3);
    }

    private void registerAction() {
        final String accType=sharedPrefrencesHelper.getAccountType();
        final String fname = fisrtName.getText().toString();
        String lname = lastName.getText().toString();
        final String uname = username.getText().toString();
        final String mail = email.getText().toString();
        final String pswd = password.getText().toString();
        final String pswd1 = password1.getText().toString();
        lname=" ";
        if (fname.isEmpty()) {
            fisrtName.setError("First name is required");
            fisrtName.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            if(accType=="1"){
                lastName.setError("Last name is required");
                lastName.requestFocus();
            }
            return;
        }
        if (uname.isEmpty()) {
            username.setError("Username is required");
            username.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if (pswd.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if (!pswd.equals(pswd1)) {
            password1.setError("Password mismatch");
            password1.requestFocus();
            return;
        }

        final String finalLname = lname;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "register.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(RegisterActivity.this, "Registered Successfully! Now Login", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "In catch :"+e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("firstname", fname);
                params.put("lastname", finalLname);
                params.put("username", uname);
                params.put("email", mail);
                params.put("password", pswd);
                params.put("password1", pswd1);
                params.put("accountType",accType);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(RegisterActivity.this);
        rQueue.add(stringRequest);
    }
}