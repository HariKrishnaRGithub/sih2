package com.example.sih2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class EmpProfileFragment extends Fragment{
    SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView firstname, lastname, username, email;
    private RequestQueue rQueue;
    String specialization;
    ArrayList<String> topicsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_emp_profile,container,false);

        topicsList=new ArrayList<String>();
        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        firstname = view.findViewById(R.id.emp_firstname);
        lastname = view.findViewById(R.id.emp_lastname);
        username = view.findViewById(R.id.emp_username);
        email = view.findViewById(R.id.emp_email);
        firstname.setText(sharedPrefrencesHelper.getFirstname());
        lastname.setText(sharedPrefrencesHelper.getLastname());
        username.setText(sharedPrefrencesHelper.getUsername());
        email.setText(sharedPrefrencesHelper.getEmail());

        Spinner specializationSpinner = view.findViewById(R.id.specializationSpinner);
        final Spinner topicSpinner=view.findViewById(R.id.topicSpinner);
        String[] items = new String[]{"Computer Science", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        specializationSpinner.setAdapter(adapter);
        specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(EmpProfileFragment.this.getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                specialization=(String) parent.getItemAtPosition(position);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getTopics.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                        JSONArray jsonArray=jsonObject.getJSONArray("details");
                                        for(int i=0; i<jsonArray.length();i++){
                                            JSONObject jsonObject3=jsonArray.getJSONObject(i);
                                            String tempvar=jsonObject3.getString("topicName");
                                            topicsList.add(tempvar);
                                            //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                        }
                                        ArrayAdapter<String> topicsAdapter=new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(),android.R.layout.simple_spinner_dropdown_item,topicsList);
                                        topicSpinner.setAdapter(topicsAdapter);
                                        topicsAdapter.notifyDataSetChanged();


                                    } else {
                                        Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("specialization",specialization);
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
                rQueue.add(stringRequest);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        return view;
    }
}