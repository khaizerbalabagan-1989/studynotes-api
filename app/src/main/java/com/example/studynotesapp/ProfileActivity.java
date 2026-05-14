package com.example.studynotesapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView profileName;
    TextView profileEmail;

    Button backBtn;

    String userId = "";
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);

        backBtn = findViewById(R.id.backBtn);

        userId = getIntent().getStringExtra("user_id");
        accessToken = getIntent().getStringExtra("access_token");

        String userName =
                getIntent().getStringExtra("user_name");

        String userEmail =
                getIntent().getStringExtra("user_email");

        if (userName != null) {
            profileName.setText(userName);
        }

        if (
                userEmail != null &&
                        !userEmail.isEmpty()
        ) {

            profileEmail.setText(userEmail);

        } else {

            profileEmail.setText(
                    "No email found"
            );
        }

        backBtn.setOnClickListener(v -> {
            finish();
        });
    }
}