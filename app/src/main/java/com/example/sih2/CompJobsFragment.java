package com.example.sih2;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CompJobsFragment extends Fragment {

    String username;
    Button addNewJobButton;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    private RequestQueue rQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.fragment_comp_jobs,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        username=sharedPrefrencesHelper.getUsername();
        addNewJobButton=view.findViewById(R.id.addNewJobButton);
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

                final EditText jobTitleET,jobDiscriptionET,jobExperienceET;
                Button cancelJobButton, nextInJobButton;
                jobTitleET=dialogView.findViewById(R.id.jobTitleET);
                jobDiscriptionET=dialogView.findViewById(R.id.jobDiscriptionET);
                jobExperienceET=dialogView.findViewById(R.id.jobDiscriptionET);
                cancelJobButton=dialogView.findViewById(R.id.cancelJobButton);
                nextInJobButton=dialogView.findViewById(R.id.nextInJobButton);
                cancelJobButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
                nextInJobButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addNewJob(jobTitleET.getText().toString(),jobDiscriptionET.getText().toString(),jobExperienceET.getText().toString());
                        alertDialog.cancel();
                    }
                });
            }
        });

        //end of oncreate
        return view;
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
