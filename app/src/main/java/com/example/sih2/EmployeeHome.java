package com.example.sih2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,EmpHomeFragment.employeeHomeSelected {
    private SharedPrefrencesHelper sharedPrefrencesHelper;

    TextView e_name,e_email;
    CircleImageView e_dp;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView name,email;
    private RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        isOnline();

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this);

        toolbar =findViewById(R.id.e_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.employee_drawer);
        navigationView=findViewById(R.id.employeenavigationview);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        e_name = headerView.findViewById(R.id.emp_name_dp);
        e_email= headerView.findViewById(R.id.emp_email_dp);
        e_dp=headerView.findViewById(R.id.e_dp);
        updateDisplayProfile();
        e_name.setText(sharedPrefrencesHelper.getUsername());
        e_email.setText(sharedPrefrencesHelper.getEmail());
        
        //toolbar.setLogo(R.drawable.employee_home);
        toolbar.setTitle("Home");

        //default fragment is home
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.employee_container, new EmpHomeFragment());
        fragmentTransaction.commit();


    }

    private void updateDisplayProfile() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getDisplayProfile.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                String imageEncoded;
                                //Toast.makeText(getActivity(), "Image upload success", Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                                imageEncoded=jsonObject1.getString("imagelocation");
                                byte[] decodedByte = Base64.decode(imageEncoded, 0);
                                Bitmap svdimg = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                                e_dp.setImageBitmap(svdimg);
                            } else {
                                Toast.makeText(EmployeeHome.this, "failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EmployeeHome.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmployeeHome.this);
        rQueue.add(stringRequest3);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){


        switch (menuItem.getItemId()){

            case R.id.emp_home_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.employee_container, new EmpHomeFragment());
                //toolbar.setLogo(R.drawable.employee_home);
                toolbar.setTitle("Home");
                fragmentTransaction.commit();
                break;
            case R.id.emp_profile_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.employee_container, new EmpProfileFragment());
                toolbar.setTitle("Profile");
                fragmentTransaction.commit();
                break;
            case R.id.emp_about_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.employee_container, new AboutUsFragment());
                toolbar.setTitle("About Us");
                fragmentTransaction.commit();
                break;
            case R.id.emp_settings_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.employee_container, new SettingsFragment());
                toolbar.setTitle("Settings");
                fragmentTransaction.commit();
                break;
            case R.id.emp_feedback_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.employee_container, new FeedbackFragment());
                toolbar.setTitle("Feedback");
                fragmentTransaction.commit();
                break;
            case R.id.emp_quit_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHome.this);
                builder.setTitle("Better Future");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Do you want to exit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.emp_logout_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);

                builder = new AlertDialog.Builder(EmployeeHome.this);
                builder.setTitle(R.string.app_name);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage("Do you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                sharedPrefrencesHelper.setFirstname(null);
                                sharedPrefrencesHelper.setLastname(null);
                                sharedPrefrencesHelper.setUsername(null);
                                sharedPrefrencesHelper.setEmail(null);
                                sharedPrefrencesHelper.setAccountType(null);
                                startActivity(new Intent(EmployeeHome.this, LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert = builder.create();
                alert.show();
                break;
        }


        return true;
    }

    @Override
    public void btnProfileClicked() {
      // run any actions which cannot be executed in the emp home fragment
    }


    @Override
    public void onBackPressed() { // exit dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHome.this);
        builder.setTitle("Better Future");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        else
        {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No internet");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage("You are not connected to internet.Try again")
                    .setCancelable(false)
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("open settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return false;
    }
}