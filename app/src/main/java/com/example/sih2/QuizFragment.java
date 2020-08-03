package com.example.sih2;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Map;

public class QuizFragment<view> extends Fragment {

    private RequestQueue rQueue;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    ListView listView;
    Button quizbutton;
    TextView resultText;
    ArrayAdapter adapter;
    ArrayList<String> topics;
    ArrayList<String> specialization;
    ArrayList question1;
    ArrayList<String> question2;
    ArrayList<String> answer1;
    ArrayList<String> answer2;
    ArrayList finalList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(QuizFragment.this.getActivity());
        resultText=view.findViewById(R.id.resultText);
        listView = view.findViewById(R.id.quizLV);
        quizbutton=view.findViewById(R.id.quizbutton);
        quizbutton.setText("start");

        topics=new ArrayList<>();
        specialization=new ArrayList<>();
        question1=new ArrayList();
        question2=new ArrayList<String>();
        answer1=new ArrayList<>();
        answer2=new ArrayList<>();
        finalList=new ArrayList();

        initializeQuiz();
        quizbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quizbutton.getText().toString().equals("start")){
                    listView.setVisibility(View.VISIBLE);
                    quizbutton.setText("next level");
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_multiple_choice,question1);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else{
                    quizbutton.setText("done");
                    int len = listView.getCount();
                    SparseBooleanArray checked = listView.getCheckedItemPositions();
                    for (int i = 0; i < len; i++)
                        if (checked.get(i)) {
                            answer1.set(i,"1");
                            //Toast.makeText(getContext(), specialization.get(i)+" "+topics.get(i), Toast.LENGTH_SHORT).show();
                            /* do whatever you want with the checked item */
                        }
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_multiple_choice,question2);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    quizbutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(quizbutton.getText().toString().equals("done")){
                                int len = listView.getCount();
                                SparseBooleanArray checked = listView.getCheckedItemPositions();
                                for (int i = 0; i < len; i++)
                                    if (checked.get(i)) {
                                        answer2.set(i,"1");
                                        //Toast.makeText(getContext(), specialization.get(i)+" "+topics.get(i), Toast.LENGTH_SHORT).show();
                                    }
                                    quizbutton.setText("start");
                                int len2 = listView.getCount();
                                for (int i = 0; i < len; i++){
                                    if((answer1.get(i)==answer2.get(i)) && answer1.get(i).equals("1")){
                                        finalList.add(topics.get(i)+" ("+specialization.get(i)+")");
                                    }
                                    }
                                resultText.setVisibility(View.VISIBLE);
                                adapter=new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1,finalList);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                quizbutton.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        });
        return view;
    }

    private void initializeQuiz() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "quiz.php",
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
                                    String sname = jsonObject3.getString("sname");
                                    String topicName = jsonObject3.getString("topicName");
                                    String question11= jsonObject3.getString("question1");
                                    String question22=jsonObject3.getString("question2");
                                    String answer11=jsonObject3.getString("answer1");
                                    String answer22=jsonObject3.getString("answer2");
                                    specialization.add(sname);
                                    topics.add(topicName);
                                    question1.add(question11);
                                    question2.add(question22);
                                    answer1.add(answer11);
                                    answer2.add(answer22);
                                    //Toast.makeText(CompJobsFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(QuizFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(QuizFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(QuizFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(QuizFragment.this.getActivity());
        rQueue.add(stringRequest);
    }
}

