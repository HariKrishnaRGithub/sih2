package com.example.sih2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    TextView loginTV;
    EditText fisrtName, lastName, username, email, password, password1;
    Button registerBtn;
    private RequestQueue rQueue;
    private SharedPrefrencesHelper sharedPrefrencesHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginTV = findViewById(R.id.loginTV);
        fisrtName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        username = findViewById(R.id.usernameemp);
        email = findViewById(R.id.emailemp);
        password = findViewById(R.id.password);
        password1 = findViewById(R.id.password1);
        registerBtn = findViewById(R.id.registerBtn);
        sharedPrefrencesHelper=new SharedPrefrencesHelper(this);
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
        final String lname = lastName.getText().toString();
        final String uname = username.getText().toString();
        final String mail = email.getText().toString();
        final String pswd = password.getText().toString();
        final String pswd1 = password1.getText().toString();
        if (fname.isEmpty()) {
            fisrtName.setError("First name is required");
            fisrtName.requestFocus();
            return;
        }
        if (lname.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
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
                params.put("lastname", lname);
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