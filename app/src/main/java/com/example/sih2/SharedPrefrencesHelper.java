package com.example.sih2;
import android.content.Context;
import android.content.SharedPreferences;
public class SharedPrefrencesHelper {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String firstname = "firstname", lastname = "lastname", username = "username", email = "email", discription="discription";
    private String accountType="accountType";
    public SharedPrefrencesHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences("login_session",
                Context.MODE_PRIVATE);
        this.context = context;
    }
    public String getFirstname() {
        return sharedPreferences.getString(firstname, "");
    }
    public String getLastname() {
        return sharedPreferences.getString(lastname, "");
    }
    public String getUsername() {
        return sharedPreferences.getString(username, "");
    }
    public String getEmail() {
        return sharedPreferences.getString(email, "");
    }
    public String getAccountType(){
        return sharedPreferences.getString(accountType,"");
    }
    public String getDiscription(){
        return sharedPreferences.getString(discription,"");
    }
    public void setFirstname(String firstname) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.firstname, firstname);
        edit.commit();
    }
    public void setLastname(String lastname) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.lastname, lastname);
        edit.commit();
    }
    public void setUsername(String username) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.username, username);
        edit.commit();
    }
    public void setEmail(String email) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.email, email);
        edit.commit();
    }
    public void setAccountType(String accountType){
        SharedPreferences.Editor edit =sharedPreferences.edit();
        edit.putString(this.accountType,accountType);
        edit.commit();
    }
    public void setDiscription(String discription) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.discription, discription);
        edit.commit();
    }
}