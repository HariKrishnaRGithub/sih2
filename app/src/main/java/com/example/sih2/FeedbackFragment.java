package com.example.sih2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FeedbackFragment extends Fragment {

    SharedPrefrencesHelper sharedPrefrencesHelper;

    EditText editTextSubject,editTextMessage;
    Button send;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_feedback,container,false);

        super.onCreate(savedInstanceState);
        //(R.layout.fragment_feedback);

        editTextSubject=view.findViewById(R.id.edit_text_subject);
        editTextMessage=view.findViewById(R.id.edit_text_message);
        send=view.findViewById(R.id.button_send);

        send.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String to="sih@betterfuture.tech";
                String subject=editTextSubject.getText().toString();
                String message=editTextMessage.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        return view;
    }

}