package com.example.sih2;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import java.util.HashMap;
import java.util.Map;

public class CompJobsFragment extends Fragment {

    String username, level, specialization, topic;
    Button addNewJobButton;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    Spinner specializationSpinner, topicSpinner, levelSpinner;
    private RequestQueue rQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_comp_jobs, container, false);

        sharedPrefrencesHelper = new SharedPrefrencesHelper(this.getActivity());
        username = sharedPrefrencesHelper.getUsername();
        addNewJobButton = view.findViewById(R.id.addNewJobButton);
        //default method calls

        //main
        addNewJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CompJobsFragment.this.getActivity());
                final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(CompJobsFragment.this.getActivity()).inflate(R.layout.popup_add_new_job, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final EditText jobTitleET, jobDiscriptionET, jobExperienceET;
                final Button cancelJobButton, nextInJobButton, addSkillsForJob;
                final MyListView skillsLV;
                skillsLV = dialogView.findViewById(R.id.skillsLV);
                jobTitleET = dialogView.findViewById(R.id.jobTitleET);
                jobDiscriptionET = dialogView.findViewById(R.id.jobDiscriptionET);
                jobExperienceET = dialogView.findViewById(R.id.jobDiscriptionET);
                cancelJobButton = dialogView.findViewById(R.id.cancelJobButton);
                nextInJobButton = dialogView.findViewById(R.id.nextInJobButton);
                addSkillsForJob = dialogView.findViewById(R.id.addSkillsforJobButton);
                cancelJobButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                nextInJobButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewJob(jobTitleET.getText().toString(), jobDiscriptionET.getText().toString(), jobExperienceET.getText().toString());
                        final String title=jobTitleET.getText().toString();
                        final String discription=jobDiscriptionET.getText().toString();
                        jobTitleET.setEnabled(false);
                        jobDiscriptionET.setEnabled(false);
                        jobExperienceET.setEnabled(false);
                        skillsLV.setVisibility(View.VISIBLE);
                        addSkillsForJob.setVisibility(View.VISIBLE);
                        cancelJobButton.setVisibility(View.GONE);
                        nextInJobButton.setVisibility(View.GONE);


                        addSkillsForJob.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(CompJobsFragment.this.getActivity());
                                final ViewGroup viewGroup2 = view.findViewById(android.R.id.content);
                                final View dialogView2 = LayoutInflater.from(CompJobsFragment.this.getActivity()).inflate(R.layout.popup_add_skills_emp, viewGroup2, false);
                                builder2.setView(dialogView2);
                                final AlertDialog alertDialog2 = builder2.create();
                                alertDialog2.show();

                                specializationSpinner = dialogView2.findViewById(R.id.specializationSpinner);
                                topicSpinner = dialogView2.findViewById(R.id.topicSpinner);
                                levelSpinner = dialogView2.findViewById(R.id.levelSpinnner);
                                Button cancel, addSkillButton;
                                cancel = dialogView2.findViewById(R.id.cancel);
                                addSkillButton = dialogView2.findViewById(R.id.addSkillButton);

                                final ArrayList<String> topicsList, speciazationList, levelsList;
                                topicsList = new ArrayList<String>();
                                speciazationList = new ArrayList<String>();
                                levelsList = new ArrayList<String>();

                                initializeSpecializationSpinner(dialogView2, speciazationList);
                                initializeLevelsSpinner(dialogView2, levelsList);

                                levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        level = (String) parent.getItemAtPosition(position);
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
                                        //Toast.makeText(CompJobsFragment.this.getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                                        specialization = (String) parent.getItemAtPosition(position);
                                        updateTopicsSpinner(dialogView2, topicsList);
                                    }


                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                        // TODO Auto-generated method stub
                                    }
                                });

                                topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        topic = (String) (String) parent.getItemAtPosition(position);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });

                                addSkillButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        uploadSkills(title,discription);
                                        updateSkillsLV(dialogView,title,discription);
                                        alertDialog2.cancel();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog2.cancel();
                                    }
                                });
                            }
                        });


                    }
                });
            }
        });

        //end of oncreate
        return view;
    }

    private void updateSkillsLV(View dialogView, final String title, final String discription) {
        final ArrayList<String> topicsList, specializationList, levelsList;
        topicsList = new ArrayList<>();
        specializationList = new ArrayList<>();
        levelsList = new ArrayList<>();
        final MyListView skillsLV;
        skillsLV=dialogView.findViewById(R.id.skillsLV);
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getJobSkills.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String temptopic = jsonObject3.getString("topicName");
                                    String tempspecialization = jsonObject3.getString("sname");
                                    String templevel = jsonObject3.getString("lname");
                                    topicsList.add(temptopic);
                                    specializationList.add(tempspecialization);
                                    levelsList.add(templevel);

                                }

                                SkillsListAdapter skillsListAdapter = new SkillsListAdapter(CompJobsFragment.this.getActivity(), topicsList, specializationList, levelsList);
                                skillsLV.setAdapter(skillsListAdapter);
                                skillsListAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompJobsFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("error :", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                params.put("title", title);
                params.put("discription", discription);

                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest2);
    }

    private void uploadSkills(final String title, final String discription) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "uploadJobSkill.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(CompJobsFragment.this.getActivity(), "Skill upload success", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompJobsFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (specialization.equals("") || topic.equals("") || level.equals("")) {
                    Toast.makeText(CompJobsFragment.this.getActivity(), "All fields are necessary", Toast.LENGTH_SHORT).show();

                } else {
                    params.put("specialization", specialization);
                    params.put("topic", topic);
                    params.put("level", level);
                    params.put("username", sharedPrefrencesHelper.getUsername());
                    params.put("title", title);
                    params.put("discription",discription);
                }
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest3);

    }

    private void updateTopicsSpinner(View dialogView2, final ArrayList<String> topicsList) {
        topicsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getTopics.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(CompJobsFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("topicName");
                                    topicsList.add(tempvar);
                                    //Toast.makeText(CompJobsFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> topicsAdapter = new ArrayAdapter<String>(CompJobsFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, topicsList);
                                topicSpinner.setAdapter(topicsAdapter);
                                topicsAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompJobsFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("specialization", specialization);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest);
    }

    private void initializeLevelsSpinner(View dialogView2, final ArrayList<String> levelsList) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getLevels.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(CompJobsFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("lname");
                                    levelsList.add(tempvar);
                                    //Toast.makeText(CompJobsFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> levelsAdapter = new ArrayAdapter<String>(CompJobsFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, levelsList);
                                levelSpinner.setAdapter(levelsAdapter);
                                levelsAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompJobsFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("levels", "getLevels");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest2);
    }

    private void initializeSpecializationSpinner(View dialogView2, final ArrayList<String> speciazationList) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getSpecialization.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(CompJobsFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("sname");
                                    speciazationList.add(tempvar);
                                    //Toast.makeText(CompJobsFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> specializationAdapter = new ArrayAdapter<String>(CompJobsFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, speciazationList);
                                specializationSpinner.setAdapter(specializationAdapter);
                                specializationAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompJobsFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("specialization", "getSpecialization");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest);
    }

    private void addNewJob(final String title, final String discription, final String years) {

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "addNewJob.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(getActivity(), "New job added !", Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(CompJobsFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            //Toast.makeText(CompJobsFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                params.put("title", title);
                params.put("discription", discription);
                params.put("years", years);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest3);

    }

    //end of class
}
