package com.example.sih2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class CompanyHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private SharedPrefrencesHelper sharedPrefrencesHelper;

    TextView c_name,c_email;
    CircleImageView c_dp;
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
        setContentView(R.layout.activity_company_home);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this);

        toolbar =findViewById(R.id.c_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout=findViewById(R.id.company_drawer);
        navigationView=findViewById(R.id.companynavigationview);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();


        View headerView = navigationView.getHeaderView(0);
        c_name = headerView.findViewById(R.id.comp_name_dp);
        c_email= headerView.findViewById(R.id.comp_email_dp);
        c_dp=headerView.findViewById(R.id.c_dp);
        c_name.setText(sharedPrefrencesHelper.getUsername());
        c_email.setText(sharedPrefrencesHelper.getEmail());

        //toolbar.setLogo(R.drawable.employee_home);
        toolbar.setTitle("Home");

        //default fragment is home
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.company_container, new CompHomeFragment());
        fragmentTransaction.commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.comp_home_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.company_container, new CompHomeFragment());

                toolbar.setTitle("Home");
                fragmentTransaction.commit();
                break;
            case R.id.comp_profile_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.company_container, new CompProfileFragment());
                toolbar.setTitle("Profile");
                fragmentTransaction.commit();
                break;
            case R.id.comp_about_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.company_container, new AboutUsFragment());
                toolbar.setTitle("About Us");
                fragmentTransaction.commit();
                break;
            case R.id.comp_settings_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.comp_feedback_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.company_container, new FeedbackFragment());
                toolbar.setTitle("Feedback");
                fragmentTransaction.commit();
                break;
            case R.id.comp_quit_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyHome.this);
                builder.setTitle("BETTER FUTURE");
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
            case R.id.comp_logout_menu_item:
                drawerLayout.closeDrawer(GravityCompat.START);

                builder = new AlertDialog.Builder(CompanyHome.this);
                //builder.setTitle(R.string.app_name);
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
                                startActivity(new Intent(CompanyHome.this, LoginActivity.class));
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
}
