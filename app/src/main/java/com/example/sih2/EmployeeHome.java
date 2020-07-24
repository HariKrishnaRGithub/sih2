package com.example.sih2;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class EmployeeHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,EmpHomeFragment.employeeHomeSelected {
    private SharedPrefrencesHelper sharedPrefrencesHelper;

    TextView e_name,e_email;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

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

    }


}