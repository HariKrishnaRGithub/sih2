package com.example.sih2;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpHomeFragment extends Fragment{
    employeeHomeSelected listener;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    RecyclerView empHomeRV;
    private RequestQueue rQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_emp_home,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        ImageSlider imageSlider=view.findViewById(R.id.slider);
        empHomeRV=view.findViewById(R.id.empHomeRV);

        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel("https://news.efinancialcareers.com/binaries/content/gallery/efinancial-careers/articles/2019/05/google-san-fran.jpg","  Google"));
        slideModels.add(new SlideModel("https://www.ft.com/__origami/service/image/v2/images/raw/https%3A%2F%2Fd1e00ek4ebabms.cloudfront.net%2Fproduction%2Fe0ca7758-da2c-4f49-aa87-b35f4a7551a5.jpg?fit=scale-down&source=next&width=700","   Microsoft"));
        slideModels.add(new SlideModel("https://zdnet2.cbsistatic.com/hub/i/r/2017/03/13/9771951a-1439-4fab-8424-ca024674545e/resize/770xauto/735eb233abb7fad6d8bd505c8c2adf57/apple-event.jpg","   Apple"));
        slideModels.add(new SlideModel("https://cdnuploads.aa.com.tr/uploads/Contents/2019/02/06/thumbs_b_c_2f7bc0bac6400f27b8bdec0cf6d40f7d.jpg?v=181112","   WhatsApp"));
        slideModels.add(new SlideModel("https://www.infosys.com/content/dam/infosys-web/en/global-resource/media-resources/images/Bangalore-New-001.jpg","   Infosys"));
        //https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg
        imageSlider.setImageList(slideModels,true);

        updateEmpHomeRV();

        return view;
    }

    private void updateEmpHomeRV() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "updateEmpHomeRV.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                ArrayList<String> companyname = new ArrayList<>();
                                ArrayList<String> jobname = new ArrayList<>();
                                ArrayList<String> jobdiscription = new ArrayList<>();
                                ArrayList<String> matchpercentage = new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String companyname1 = jsonObject3.getString("Company_Name");
                                    String jobname1 = jsonObject3.getString("Job_Name");
                                    String jobdiscription1 = jsonObject3.getString("Job_Discription");
                                    String matchpercentage1 = jsonObject3.getString("Match_Percentage");

                                    companyname.add(companyname1);
                                    jobname.add(jobname1);
                                    jobdiscription.add(jobdiscription1);
                                    matchpercentage.add(matchpercentage1);
                                }
                                //EmpJobListRVAdapter empJobListRVAdapter=new EmpJobListRVAdapter(companyname,jobid,jobname,jobdiscription,matchpercentage,getContext());
                                initJobsRV(companyname,jobname,jobdiscription,matchpercentage);

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
                        Toast.makeText(EmpHomeFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpHomeFragment.this.getActivity());
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof employeeHomeSelected){
            listener = (employeeHomeSelected) context;

        }else{
            throw new ClassCastException(context.toString()+" must implement listener");
        }

    }

    public interface employeeHomeSelected{
        public void btnProfileClicked();
    }
    private void initJobsRV(ArrayList companyname,ArrayList jobname, ArrayList jobdiscription, ArrayList matchpercentage) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        empHomeRV.setLayoutManager(lm);
        EmpJobListRVAdapter adapter = new EmpJobListRVAdapter(companyname,jobname,jobdiscription,matchpercentage, getActivity());
        empHomeRV.setAdapter(adapter);
    }
}
class EmpJobListRVAdapter extends RecyclerView.Adapter<EmpJobListRVAdapter.ViewHolder> {
    public EmpJobListRVAdapter(ArrayList<String> companyname,ArrayList<String> jobname,ArrayList<String> jobdiscription,ArrayList<String> matchpercentage,  Context mContext) {
        this.companyname=companyname;
        this.jobname=jobname;
        this.jobdiscription=jobdiscription;
        this.matchpercentage=matchpercentage;
        this.mContext = mContext;
    }

    private ArrayList<String> companyname = new ArrayList<>();
    private ArrayList<String> jobname = new ArrayList<>();
    private ArrayList<String> jobdiscription = new ArrayList<>();
    private ArrayList<String> matchpercentage = new ArrayList<>();
    private Context mContext;

    @NonNull
    @Override
    public EmpJobListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview_emp_home_rv, viewGroup, false);
        EmpJobListRVAdapter.ViewHolder holder = new EmpJobListRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmpJobListRVAdapter.ViewHolder viewHolder, final int i) {

        viewHolder.companynameTV.setText(companyname.get(i));
        viewHolder.jobnameTV.setText(jobname.get(i));
        viewHolder.jobdiscriptionTV.setText(jobdiscription.get(i));
        viewHolder.matchpercentageTV.setText(matchpercentage.get(i));
    }

    @Override
    public int getItemCount() {
        return companyname.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView companynameTV,jobnameTV,jobdiscriptionTV,matchpercentageTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companynameTV=(TextView)itemView.findViewById(R.id.Company_Name);
            jobnameTV=itemView.findViewById(R.id.Job_Name);
            jobdiscriptionTV=itemView.findViewById(R.id.Job_Discription);
            matchpercentageTV=itemView.findViewById(R.id.Match_Percentage);
        }
    }

}
