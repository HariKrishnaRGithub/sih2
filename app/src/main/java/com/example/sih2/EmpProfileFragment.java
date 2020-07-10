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
import android.widget.Button;
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
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EmpProfileFragment extends Fragment{
    SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView firstname, lastname, username, email,cancel;
    Button addNewSkillButton;
    CardView skillCard;
    Spinner specializationSpinner,topicSpinner,levelSpinner;
    private RequestQueue rQueue;
    String specialization,topic,level;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_emp_profile,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        firstname = view.findViewById(R.id.emp_firstname);
        lastname = view.findViewById(R.id.emp_lastname);
        username = view.findViewById(R.id.emp_username);
        email = view.findViewById(R.id.emp_email);
        addNewSkillButton=view.findViewById(R.id.addNewSkillButton);
        skillCard=view.findViewById(R.id.skillCard);

        firstname.setText(sharedPrefrencesHelper.getFirstname());
        lastname.setText(sharedPrefrencesHelper.getLastname());
        username.setText(sharedPrefrencesHelper.getUsername());
        email.setText(sharedPrefrencesHelper.getEmail());


        addNewSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSkillButton.setVisibility(View.GONE);
                skillCard.setVisibility(View.VISIBLE);

                cancel=view.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewSkillButton.setVisibility(View.VISIBLE);
                        skillCard.setVisibility(View.GONE);
                    }
                });

                final ArrayList<String> topicsList,speciazationList,levelsList;
                topicsList=new ArrayList<String>();
                speciazationList=new ArrayList<String>();
                levelsList=new ArrayList<String>();

                specializationSpinner = view.findViewById(R.id.specializationSpinner);
                topicSpinner=view.findViewById(R.id.topicSpinner);
                levelSpinner=view.findViewById(R.id.levelSpinnner);

                // Initializing specialization spinner
                initializeSpecializationSpinner(speciazationList);
                initializeLevelsSpinner(levelsList);

                levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        level=(String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // Setting topicSpinner using SpecializationSpinner

                specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        specialization=(String) parent.getItemAtPosition(position);
                        updateTopicsSpinner(topicsList);

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

                topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        topic=(String)(String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }


        });
        
        Button addskillButton=view.findViewById(R.id.addSkillButton);
        addskillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadSkills();
            }
        });

        return view;

    }

    private void initializeLevelsSpinner(final ArrayList<String> levelsList) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getLevels.php",
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
                                    String tempvar=jsonObject3.getString("lname");
                                    levelsList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> levelsAdapter=new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(),android.R.layout.simple_spinner_dropdown_item,levelsList);
                                levelSpinner.setAdapter(levelsAdapter);
                                levelsAdapter.notifyDataSetChanged();


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
                params.put("levels","getLevels");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest2);

    }

    private void updateTopicsSpinner(final ArrayList<String> topicsList) {
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

    private void initializeSpecializationSpinner(final ArrayList<String> speciazationList) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getSpecialization.php",
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
                                    String tempvar=jsonObject3.getString("sname");
                                    speciazationList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> specializationAdapter=new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(),android.R.layout.simple_spinner_dropdown_item,speciazationList);
                                specializationSpinner.setAdapter(specializationAdapter);
                                specializationAdapter.notifyDataSetChanged();


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
                params.put("specialization","getSpecialization");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest);
    }

    private void uploadSkills() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "uploadSkill.php",
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

                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }



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
                if(specialization.equals("")||topic.equals("")||level.equals("")){
                    Toast.makeText(EmpProfileFragment.this.getActivity(), "All fields are necessary", Toast.LENGTH_SHORT).show();
                }else{
                    params.put("specialization",specialization);
                    params.put("topic",topic);
                    params.put("level",level);
                    params.put("username",sharedPrefrencesHelper.getUsername());
                }
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }


}