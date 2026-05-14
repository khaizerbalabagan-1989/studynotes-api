package com.example.studynotesapp;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText name, email, password;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        registerBtn = findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(v -> {

            if (!validateInputs()) {
                return;
            }

            registerUser();
        });
    }

    private boolean validateInputs() {

        if (name.getText().toString().trim().isEmpty()) {

            name.setError("Enter your name");
            return false;
        }

        if (email.getText().toString().trim().isEmpty()) {

            email.setError("Enter your email");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(
                email.getText().toString()
        ).matches()) {

            email.setError("Invalid email");
            return false;
        }

        if (password.getText().toString().trim().isEmpty()) {

            password.setError("Enter password");
            return false;
        }

        if (password.getText().toString().length() < 6) {

            password.setError(
                    "Password must be at least 6 characters"
            );

            return false;
        }

        return true;
    }

    private void registerUser() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/auth/v1/signup";

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        url,

                        response -> {

                            Toast.makeText(
                                    this,
                                    "Registration Successful!",
                                    Toast.LENGTH_LONG
                            ).show();

                            finish();
                        },

                        error -> Toast.makeText(
                                this,
                                "Registration failed",
                                Toast.LENGTH_LONG
                        ).show()

                ) {

                    @Override
                    public byte[] getBody() {

                        try {

                            JSONObject jsonBody =
                                    new JSONObject();

                            jsonBody.put(
                                    "email",
                                    email.getText().toString().trim()
                            );

                            jsonBody.put(
                                    "password",
                                    password.getText().toString().trim()
                            );

                            JSONObject data =
                                    new JSONObject();

                            data.put(
                                    "name",
                                    name.getText().toString().trim()
                            );

                            jsonBody.put(
                                    "data",
                                    data
                            );

                            return jsonBody
                                    .toString()
                                    .getBytes("utf-8");

                        } catch (Exception e) {
                            return null;
                        }
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }

                    @Override
                    public Map<String, String> getHeaders() {

                        Map<String, String> headers =
                                new HashMap<>();

                        headers.put(
                                "apikey",
                                SupabaseConfig.API_KEY
                        );

                        headers.put(
                                "Content-Type",
                                "application/json"
                        );

                        return headers;
                    }
                };

        Volley.newRequestQueue(this).add(request);
    }
}

