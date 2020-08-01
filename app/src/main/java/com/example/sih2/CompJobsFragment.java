package com.example.sih2;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompJobsFragment extends Fragment {

    String username, level, specialization, topic;
    Button addNewJobButton;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    Spinner specializationSpinner, topicSpinner, levelSpinner;
    MyListView jobsLV;
    private RequestQueue rQueue;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_comp_jobs, container, false);


        sharedPrefrencesHelper = new SharedPrefrencesHelper(getActivity());
        username = sharedPrefrencesHelper.getUsername();
        jobsLV = view.findViewById(R.id.jobsLV);
        addNewJobButton = view.findViewById(R.id.addNewJobButton);
        recyclerView = view.findViewById(R.id.jobsRV);
        //default method calls
        updateJobsLV();
        //main

        //RV start

        //RVEnd
        jobsLV.setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {
                PopupMenu popup = new PopupMenu(CompJobsFragment.this.getActivity(), jobsLV);
                popup.getMenuInflater().inflate(R.menu.popup_edit_delete, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        View row = jobsLV.getAdapter().getView(index, view, container);
                        final TextView temptitle = row.findViewById(R.id.titleInJobs);
                        final TextView tempdiscription = row.findViewById(R.id.discriptionInJobs);

                        if (item.getTitle().toString().equals("Delete")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Delete job");
                            builder.setMessage("Do you want to delete the job " + temptitle.getText().toString() + " ?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            deleteCompJob(temptitle.getText().toString(), tempdiscription.getText().toString());
                                            updateJobsLV();
                                            dialog.cancel();
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
                        if (item.getTitle().toString().equals("Edit")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CompJobsFragment.this.getActivity());
                            final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                            final View dialogView = LayoutInflater.from(CompJobsFragment.this.getActivity()).inflate(R.layout.popup_add_new_job, viewGroup, false);
                            builder.setView(dialogView);
                            final AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                            final EditText jobTitleET, jobDiscriptionET, jobExperienceET;
                            final Button cancelJobButton, nextInJobButton, addSkillsForJob, doneButton;
                            final MyListView skillsLV;
                            skillsLV = dialogView.findViewById(R.id.skillsLV);
                            jobTitleET = dialogView.findViewById(R.id.jobTitleET);
                            jobDiscriptionET = dialogView.findViewById(R.id.jobDiscriptionET);
                            jobExperienceET = dialogView.findViewById(R.id.jobDiscriptionET);
                            cancelJobButton = dialogView.findViewById(R.id.cancelJobButton);
                            nextInJobButton = dialogView.findViewById(R.id.nextInJobButton);
                            doneButton = dialogView.findViewById(R.id.doneButton);
                            addSkillsForJob = dialogView.findViewById(R.id.addSkillsforJobButton);
                            jobTitleET.setText(temptitle.getText().toString());
                            jobExperienceET.setText(tempdiscription.getText().toString());
                            jobTitleET.setEnabled(false);
                            jobDiscriptionET.setEnabled(false);
                            jobExperienceET.setEnabled(false);
                            skillsLV.setVisibility(View.VISIBLE);
                            addSkillsForJob.setVisibility(View.VISIBLE);
                            doneButton.setVisibility(View.VISIBLE);
                            cancelJobButton.setVisibility(View.GONE);
                            nextInJobButton.setVisibility(View.GONE);
                            updateSkillsLV(dialogView, temptitle.getText().toString(), tempdiscription.getText().toString());
                            cancelJobButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.cancel();
                                }
                            });

                            addNewJob(jobTitleET.getText().toString(), jobDiscriptionET.getText().toString(), jobExperienceET.getText().toString());
                            final String title = jobTitleET.getText().toString();
                            final String discription = jobDiscriptionET.getText().toString();

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
                                            uploadSkills(title, discription);
                                            updateSkillsLV(dialogView, title, discription);
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

                            skillsLV.setOnItemLongClickListener(new OnItemLongClickListener() {
                                public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {

                                    PopupMenu popup = new PopupMenu(CompJobsFragment.this.getActivity(), skillsLV);
                                    popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                        @Override
                                        public boolean onMenuItemClick(MenuItem item) {

                                            View row = skillsLV.getAdapter().getView(index, view, container);
                                            final TextView tempspecialization = row.findViewById(R.id.specializationTV);
                                            final TextView temptopic = row.findViewById(R.id.topicTV);
                                            final TextView templevel = row.findViewById(R.id.levelTV);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setTitle("Delete Skill for the job");
                                            builder.setMessage("Do you want to delete the skill " + temptopic.getText().toString() + " under " + tempspecialization.getText().toString() + " for the jon ?")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            deleteCompJobSkill(title, discription, tempspecialization.getText().toString(), temptopic.getText().toString(), templevel.getText().toString());
                                                            updateSkillsLV(dialogView, title, discription);
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();

                                            return false;
                                        }
                                    });
                                    popup.show();//showing popup menu
                                    return false;
                                }
                            });
                            doneButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.cancel();
                                }
                            });
                        }
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });

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
                final Button cancelJobButton, nextInJobButton, addSkillsForJob, doneButton;
                final MyListView skillsLV;
                skillsLV = dialogView.findViewById(R.id.skillsLV);
                jobTitleET = dialogView.findViewById(R.id.jobTitleET);
                jobDiscriptionET = dialogView.findViewById(R.id.jobDiscriptionET);
                jobExperienceET = dialogView.findViewById(R.id.jobDiscriptionET);
                cancelJobButton = dialogView.findViewById(R.id.cancelJobButton);
                nextInJobButton = dialogView.findViewById(R.id.nextInJobButton);
                doneButton = dialogView.findViewById(R.id.doneButton);
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
                        final String title = jobTitleET.getText().toString();
                        final String discription = jobDiscriptionET.getText().toString();
                        jobTitleET.setEnabled(false);
                        jobDiscriptionET.setEnabled(false);
                        String tempexp = jobExperienceET.getText().toString();
                        jobExperienceET.setText(tempexp);
                        jobExperienceET.setEnabled(false);
                        skillsLV.setVisibility(View.VISIBLE);
                        addSkillsForJob.setVisibility(View.VISIBLE);
                        cancelJobButton.setVisibility(View.GONE);
                        nextInJobButton.setVisibility(View.GONE);
                        doneButton.setVisibility(View.VISIBLE);

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
                                        uploadSkills(title, discription);
                                        updateSkillsLV(dialogView, title, discription);
                                        alertDialog2.cancel();
                                    }
                                });

                                skillsLV.setOnItemLongClickListener(new OnItemLongClickListener() {
                                    public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {

                                        PopupMenu popup = new PopupMenu(CompJobsFragment.this.getActivity(), skillsLV);
                                        popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                            @Override
                                            public boolean onMenuItemClick(MenuItem item) {

                                                View row = skillsLV.getAdapter().getView(index, view, container);
                                                final TextView tempspecialization = row.findViewById(R.id.specializationTV);
                                                final TextView temptopic = row.findViewById(R.id.topicTV);
                                                final TextView templevel = row.findViewById(R.id.levelTV);

                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setTitle("Delete Skill for the job");
                                                builder.setMessage("Do you want to delete the skill " + temptopic.getText().toString() + " under " + tempspecialization.getText().toString() + " for the jon ?")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                deleteCompJobSkill(title, discription, tempspecialization.getText().toString(), temptopic.getText().toString(), templevel.getText().toString());
                                                                updateSkillsLV(dialogView, title, discription);
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();

                                                return false;
                                            }
                                        });
                                        popup.show();//showing popup menu
                                        return false;
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
                doneButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });

        //end of oncreate
        return view;
    }

    private void deleteCompJobSkill(final String temptitle, final String tempdiscription, final String tempspecialization, final String temptopic, final String templevel) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "deleteCompJobSkill.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(getActivity(), "Skill deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "error", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(CompJobsFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                params.put("specialization", tempspecialization);
                params.put("topic", temptopic);
                params.put("level", templevel);
                params.put("topic", temptopic);
                params.put("discription", tempdiscription);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }

    private void deleteCompJob(final String title, final String discription) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "deleteCompJob.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                                updateJobsLV();
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
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }

    private void updateJobsLV() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getCompJobs.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                final ArrayList titleList, discriptionList;
                                titleList = new ArrayList();
                                discriptionList = new ArrayList();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String temptitle = jsonObject3.getString("jname");
                                    String tempdiscription = jsonObject3.getString("discription");
                                    titleList.add(temptitle);
                                    discriptionList.add(tempdiscription);

                                }
                                JobsListAdapter jobsListAdapter = new JobsListAdapter(CompJobsFragment.this.getActivity(), titleList, discriptionList);
                                jobsLV.setAdapter(jobsListAdapter);
                                jobsListAdapter.notifyDataSetChanged();

                                initRecyclerView(titleList, discriptionList);
                            } else {
                                //Toast.makeText(CompJobsFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(CompJobsFragment.this.getActivity());
        rQueue.add(stringRequest3);

    }


    private void updateSkillsLV(final View dialogView, final String title, final String discription) {
        final ArrayList<String> topicsList, specializationList, levelsList;
        topicsList = new ArrayList<>();
        specializationList = new ArrayList<>();
        levelsList = new ArrayList<>();
        final MyListView skillsLV;
        skillsLV = dialogView.findViewById(R.id.skillsLV);
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
                                SkillsListAdapter skillsListAdapter2 = new SkillsListAdapter(CompJobsFragment.this.getActivity(), topicsList, specializationList, levelsList);
                                skillsLV.setAdapter(skillsListAdapter2);
                                skillsListAdapter2.notifyDataSetChanged();
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
                    params.put("discription", discription);
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

    private void initRecyclerView(ArrayList title, ArrayList description) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(lm);
        JobListRVAdapter adapter = new JobListRVAdapter(title, description, getActivity());
        recyclerView.setAdapter(adapter);
    }
    //end of class
}

class JobListRVAdapter extends RecyclerView.Adapter<JobListRVAdapter.ViewHolder> {
    public JobListRVAdapter(ArrayList<String> description, ArrayList<String> title, Context mContext) {
        this.description = description;
        this.title = title;
        this.mContext = mContext;
    }

    private ArrayList<String> description = new ArrayList<>();
    private ArrayList<String> title = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public JobListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview_comp_jobs, viewGroup, false);
        JobListRVAdapter.ViewHolder holder = new JobListRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final JobListRVAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.descriptionTxt.setText(description.get(i));
        viewHolder.titleTxt.setText(title.get(i));
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTxt, descriptionTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = (TextView) itemView.findViewById(R.id.titleInJobs);
            descriptionTxt = (TextView) itemView.findViewById(R.id.discriptionInJobs);
        }
    }

}

class JobsListAdapter extends ArrayAdapter {
    ArrayList<String> title, discription;

    public JobsListAdapter(Context context, ArrayList<String> title, ArrayList<String> discription) {
        super(context, R.layout.custom_listview_comp_jobs, R.id.titleInJobs, title);
        this.title = title;
        this.discription = discription;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_comp_jobs, parent, false);
        TextView titleInJobs = row.findViewById(R.id.titleInJobs);
        TextView disctiptionInJobs = row.findViewById(R.id.discriptionInJobs);
        titleInJobs.setText(title.get(position));
        disctiptionInJobs.setText(discription.get(position));
        return row;
    }
}
