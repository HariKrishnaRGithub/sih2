package com.example.sih2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            String empusername1 = jsonObject3.getString("empusername");
                                            String firstname1 = jsonObject3.getString("firstname");
                                            String lastname1 = jsonObject3.getString("lastname");
                                            String matchpercentage1 = jsonObject3.getString("match_percentage");

                                            empusername.add(empusername1);
                                            firstname.add(firstname1);
                                            lastname.add(lastname1);
                                            matchpercentage.add(matchpercentage1);
                                        }
                                        initEmpListRV(empusername,firstname,lastname,matchpercentage);

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
    private void initEmpListRV(ArrayList empusername, ArrayList firstname,ArrayList lastname, ArrayList matchpercentage) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        compHomeRV.setLayoutManager(lm);
        EmpListRVAdapter adapter = new EmpListRVAdapter(empusername,firstname,lastname,matchpercentage, getActivity());
        compHomeRV.setAdapter(adapter);
    }

}
class EmpListRVAdapter extends RecyclerView.Adapter<EmpListRVAdapter.ViewHolder> {
    public EmpListRVAdapter(ArrayList<String> empusername, ArrayList<String> firstname,ArrayList<String> lastname,ArrayList<String> matchpercentage,  Context mContext) {
        this.empusername=empusername;
        this.firstname=firstname;
        this.lastname=lastname;
        this.matchpercentage=matchpercentage;
        this.mContext = mContext;
    }

    private ArrayList<String> empusername = new ArrayList<>();
    private ArrayList<String> firstname = new ArrayList<>();
    private ArrayList<String> lastname = new ArrayList<>();
    private ArrayList<String> matchpercentage = new ArrayList<>();
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
        viewHolder.matchpercentageTV.setText(matchpercentage.get(i));
    }

    @Override
    public int getItemCount() {
        return empusername.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView empusernameTV,firstnameTV,lastnameTV,matchpercentageTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            empusernameTV=(TextView)itemView.findViewById(R.id.empusername); 
            firstnameTV=(TextView)itemView.findViewById(R.id.firstname);
            lastnameTV=(TextView)itemView.findViewById(R.id.lastname); 
            matchpercentageTV=itemView.findViewById(R.id.matchpercentage);
        }
    }

}
