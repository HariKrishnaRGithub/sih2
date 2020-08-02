package com.example.sih2;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    ListView qLv;
    ArrayList speciazationL, topicL, ans1L, ans2L, q1, q2;
    int length;
    private RequestQueue rQueue;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    ListView simpleList;
    String[] questions;
    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        sharedPrefrencesHelper = new SharedPrefrencesHelper(QuizFragment.this.getActivity());

        ArrayList<String> arrayList=new ArrayList();
        arrayList.add("q1");
        arrayList.add("q2");
// get the string array from string.xml file
        //questions = arrayList.con

// get the reference of ListView and Button
        simpleList = (ListView) view.findViewById(R.id.simpleListView);
        submit = (Button) view.findViewById(R.id.submit);
// set the adapter to fill the data in the ListView
        CustomAdapter customAdapter = new CustomAdapter(getContext(), questions);
        simpleList.setAdapter(customAdapter);
// perform setOnClickListerner event on Button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
// get the value of selected answers from custom adapter
                for (int i = 0; i < CustomAdapter.selectedAnswers.size(); i++) {
                    message = message + "\n" + (i + 1) + " " + CustomAdapter.selectedAnswers.get(i);
                }
// display the message on screen with the help of Toast.
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
}

class CustomAdapter extends BaseAdapter {
    Context context;
    String[] questionsList;
    LayoutInflater inflter;
    public static ArrayList<String> selectedAnswers;

    public CustomAdapter(Context applicationContext, String[] questionsList) {
        this.context = context;
        this.questionsList = questionsList;
// initialize arraylist and add static string for all the questions
        selectedAnswers = new ArrayList<>();
        for (int i = 0; i < questionsList.length; i++) {
            selectedAnswers.add("Not Attempted");
        }
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return questionsList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_listview_quiz, null);
// get the reference of TextView and Button's
        TextView question = (TextView) view.findViewById(R.id.question);
        RadioButton yes = (RadioButton) view.findViewById(R.id.yes);
        RadioButton no = (RadioButton) view.findViewById(R.id.no);
// perform setOnCheckedChangeListener event on yes button
        yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// set Yes values in ArrayList if RadioButton is checked
                if (isChecked)
                    selectedAnswers.set(i, "Yes");
            }
        });
// perform setOnCheckedChangeListener event on no button
        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
// set No values in ArrayList if RadioButton is checked
                if (isChecked)
                    selectedAnswers.set(i, "No");

            }
        });
// set the value in TextView
        question.setText(questionsList[i]);
        return view;
    }
}