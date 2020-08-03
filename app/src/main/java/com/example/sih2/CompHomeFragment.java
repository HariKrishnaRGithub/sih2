package com.example.sih2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arasthel.asyncjob.AsyncJob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;

public class CompHomeFragment extends Fragment {
    RecyclerView compHomeRV;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    private RequestQueue rQueue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_comp_home,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        compHomeRV=view.findViewById(R.id.compHomeRV);
        updateCompHomeLV();

        return view;
    }

    private void updateCompHomeLV() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "updateCompHomeRV.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                        ArrayList<String> empusername = new ArrayList<>();
                                        ArrayList<String> firstname = new ArrayList<>();
                                        ArrayList<String> lastname = new ArrayList<>();
                                        ArrayList<String> matchpercentage = new ArrayList<>();
                                        ArrayList<String> jobid = new ArrayList<>();
                                        ArrayList<String> jobname = new ArrayList<>();
                                        ArrayList<String> empdiscription = new ArrayList<>();
                                        ArrayList<String> empemail = new ArrayList<>();
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            String empusername1 = jsonObject3.getString("empusername");
                                            String firstname1 = jsonObject3.getString("firstname");
                                            String lastname1 = jsonObject3.getString("lastname");
                                            String matchpercentage1 = jsonObject3.getString("match_percentage");
                                            String jobid1=jsonObject3.getString("jobid");
                                            String jobname1=jsonObject3.getString("jobname");
                                            String empdiscription1=jsonObject3.getString("empdiscription");
                                            String empemail1=jsonObject3.getString("empemail");

                                            empusername.add(empusername1);
                                            firstname.add(firstname1);
                                            lastname.add(lastname1);
                                            matchpercentage.add(matchpercentage1);
                                            jobid.add(jobid1);
                                            jobname.add(jobname1);
                                            empdiscription.add(empdiscription1);
                                            empemail.add(empemail1);

                                        }
                                        initEmpListRV(empusername,firstname,lastname,matchpercentage,jobid,jobname,empdiscription,empemail);

                                        //Toast.makeText(getActivity(), "Skill deleted", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(CompHomeFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", sharedPrefrencesHelper.getUsername());
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(CompHomeFragment.this.getActivity());
                rQueue.add(stringRequest3);
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        // Toast.makeText(getActivity(), "Result was: ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    private void initEmpListRV(ArrayList empusername, ArrayList firstname,ArrayList lastname, ArrayList matchpercentage,ArrayList jobid, ArrayList jobname, ArrayList empdiscription,ArrayList empemail) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        compHomeRV.setLayoutManager(lm);
        EmpListRVAdapter adapter = new EmpListRVAdapter(empusername,firstname,lastname,matchpercentage,jobid,jobname,empdiscription,empemail, getActivity());
        compHomeRV.setAdapter(adapter);
    }

}
class EmpListRVAdapter extends RecyclerView.Adapter<EmpListRVAdapter.ViewHolder> {
    RequestQueue rQueue;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    public EmpListRVAdapter(ArrayList<String> empusername, ArrayList<String> firstname,ArrayList<String> lastname,ArrayList<String> matchpercentage,ArrayList<String> jobid,ArrayList<String> jobnamne,ArrayList<String> empdiscription,ArrayList<String> empemail,  Context mContext) {
        this.empusername=empusername;
        this.firstname=firstname;
        this.lastname=lastname;
        this.matchpercentage=matchpercentage;
        this.jobid=jobid;
        this.jobnamne=jobnamne;
        this.empdiscription=empdiscription;
        this.empemail=empemail;
        this.mContext = mContext;
    }

    private ArrayList<String> empusername = new ArrayList<>();
    private ArrayList<String> firstname = new ArrayList<>();
    private ArrayList<String> lastname = new ArrayList<>();
    private ArrayList<String> matchpercentage = new ArrayList<>();
    private ArrayList<String> jobid = new ArrayList<>();
    private ArrayList<String> jobnamne = new ArrayList<>();
    private ArrayList<String> empdiscription = new ArrayList<>();
    private ArrayList<String> empemail = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public EmpListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview_comp_home, viewGroup, false);
        EmpListRVAdapter.ViewHolder holder = new EmpListRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmpListRVAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.empusernameTV.setText(empusername.get(i));
        viewHolder.firstnameTV.setText(firstname.get(i));
        viewHolder.lastnameTV.setText(lastname.get(i));
        viewHolder.jobidTV.setText(jobid.get(i));
        viewHolder.jobnameTV.setText(jobnamne.get(i));
        viewHolder.empdiscriptionTV.setText(empdiscription.get(i));
        String s=matchpercentage.get(i);
        Float f=Float.parseFloat(s);
        if(f>100){
            viewHolder.matchpercentageTV.setText("100% qualified & The candidate pocesses more skill levels than required for this job");
        }else{
            int ff=(int)f.intValue();
            viewHolder.matchpercentageTV.setText(ff+"% of skills are matching with skills of the candidate");
        }
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.employee_details, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                // COPYPROFILESTART
                TextView firstname2 = dialogView.findViewById(R.id.emp_firstname);
                TextView lastname2 = dialogView.findViewById(R.id.emp_lastname);
                TextView empemail2=dialogView.findViewById(R.id.emp_email);
                TextView empdisc=dialogView.findViewById(R.id.empDiscriptionTV);
                firstname2.setText(firstname.get(i));
                lastname2.setText(lastname.get(i));
                empdisc.setText(empdiscription.get(i));
                empemail2.setText(empemail.get(i));
                
                final MyListView skillsLV,experienceLV;
                final TagContainerLayout degreesLV;
                
                skillsLV=dialogView.findViewById(R.id.skillsLV);
                degreesLV=dialogView.findViewById(R.id.degreeLV);
                experienceLV=dialogView.findViewById(R.id.experienceLV);
                final ArrayList<String> topicsList, specializationList, levelsList;
                topicsList = new ArrayList<>();
                specializationList = new ArrayList<>();
                levelsList = new ArrayList<>();
                StringRequest stringRequest7 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "getEmpSkills.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
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

                                        SkillsListAdapter skillsListAdapter = new SkillsListAdapter(mContext, topicsList, specializationList, levelsList);
                                        skillsLV.setAdapter(skillsListAdapter);
                                        skillsListAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(mContext, "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                                Log.i("error :", error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", empusername.get(i));
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(mContext);
                rQueue.add(stringRequest7);

                final ArrayList<String> degreesList;
                degreesList = new ArrayList<>();
                StringRequest stringRequest8 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "getEmpDegrees.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    degreesLV.removeAllTags();
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            String temptopic = jsonObject3.getString("dname");
                                            degreesList.add(temptopic);

                                        }
                                        //educationTags = getView().findViewById(R.id.degreeLV);
                                        //educationTags.setTags(degreesList);
                                        degreesLV.setTags(degreesList);

                                    } else {
                                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(mContext, "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username",empusername.get(i) );
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(mContext);
                rQueue.add(stringRequest8);

                StringRequest stringRequest4 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "getEmpExperience.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        final ArrayList experienceList, yearsList;
                                        experienceList = new ArrayList();
                                        yearsList = new ArrayList();
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            String tempdiscription = jsonObject3.getString("discription");
                                            String tempyears = jsonObject3.getString("experience");
                                            experienceList.add(tempdiscription);
                                            yearsList.add(tempyears);

                                        }
                                        ExperienceListAdapter experienceListAdapter = new ExperienceListAdapter(mContext, experienceList, yearsList);
                                        experienceLV.setAdapter(experienceListAdapter);
                                        experienceListAdapter.notifyDataSetChanged();
                                    } else {
                                        //Toast.makeText(EmpProfileFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", empusername.get(i));
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(mContext);
                rQueue.add(stringRequest4);

                Button requestbutton=dialogView.findViewById(R.id.requestbutton);
                requestbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "Requested", Toast.LENGTH_LONG).show();
                        String to=empemail.get(i);
                        String subject="Resume request";
                        String message="";
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);
                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        mContext.startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        StringRequest stringRequest5 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "notifyEmployee.php",
                                new Response.Listener<String>() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onResponse(String response) {
                                        rQueue.getCache().clear();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                                        Log.i("error :", error.toString());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                sharedPrefrencesHelper=new SharedPrefrencesHelper(mContext);
                                params.put("From", sharedPrefrencesHelper.getFirstname());
                                params.put("To",empemail.get(i));
                                return params;
                            }
                        };
                        rQueue = Volley.newRequestQueue(mContext);
                        rQueue.add(stringRequest5);
                    }
                });
                //COPYPROFILEEND
            }
        });

    }

    @Override
    public int getItemCount() {
        return empusername.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayoutCompat parentLayout;
        private TextView empusernameTV,firstnameTV,lastnameTV,matchpercentageTV,jobidTV,empdiscriptionTV,jobnameTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentLayout=(LinearLayoutCompat)itemView.findViewById(R.id.comp_home_RV_layout);
            empusernameTV=(TextView)itemView.findViewById(R.id.empusername); 
            firstnameTV=(TextView)itemView.findViewById(R.id.firstname);
            lastnameTV=(TextView)itemView.findViewById(R.id.lastname); 
            matchpercentageTV=itemView.findViewById(R.id.matchpercentage);
            jobidTV=itemView.findViewById(R.id.jobidJ);
            empdiscriptionTV=itemView.findViewById(R.id.discriptionJ);
            jobnameTV=itemView.findViewById(R.id.jobNamE);

        }
    }
}
