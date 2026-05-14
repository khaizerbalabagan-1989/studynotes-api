package com.example.studynotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewNoteActivity extends AppCompatActivity {

    EditText title, content;

    Button updateBtn, deleteBtn;

    String noteId = "";
    String userId = "";
    String userName = "";
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        noteId = getIntent().getStringExtra("note_id");
        userId = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("user_name");
        accessToken = getIntent().getStringExtra("access_token");

        title.setText(getIntent().getStringExtra("note_title"));
        content.setText(getIntent().getStringExtra("note_content"));

        updateBtn.setOnClickListener(v -> updateNote());

        deleteBtn.setOnClickListener(v -> deleteNote());
    }

    private void updateNote() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/rest/v1/notes?id=eq." +
                        noteId;

        StringRequest request = new StringRequest(
                Request.Method.PATCH,
                url,

                response -> {

                    Toast.makeText(
                            this,
                            "Note Updated!",
                            Toast.LENGTH_SHORT
                    ).show();

                    goBack();
                },

                error -> Toast.makeText(
                        this,
                        "Update failed",
                        Toast.LENGTH_SHORT
                ).show()

        ) {

            @Override
            public byte[] getBody() {

                try {

                    JSONObject json = new JSONObject();

                    json.put(
                            "title",
                            title.getText().toString()
                    );

                    json.put(
                            "content",
                            content.getText().toString()
                    );

                    return json.toString().getBytes("utf-8");

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
                        "Authorization",
                        "Bearer " + accessToken
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

    private void deleteNote() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/rest/v1/notes?id=eq." +
                        noteId;

        StringRequest request = new StringRequest(
                Request.Method.DELETE,
                url,

                response -> {

                    Toast.makeText(
                            this,
                            "Note Deleted!",
                            Toast.LENGTH_SHORT
                    ).show();

                    goBack();
                },

                error -> Toast.makeText(
                        this,
                        "Delete failed",
                        Toast.LENGTH_SHORT
                ).show()

        ) {

            @Override
            public Map<String, String> getHeaders() {

                Map<String, String> headers =
                        new HashMap<>();

                headers.put(
                        "apikey",
                        SupabaseConfig.API_KEY
                );

                headers.put(
                        "Authorization",
                        "Bearer " + accessToken
                );

                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void goBack() {

        Intent intent =
                new Intent(this, NotesActivity.class);

        intent.putExtra("user_id", userId);
        intent.putExtra("user_name", userName);
        intent.putExtra("access_token", accessToken);

        startActivity(intent);

        finish();
    }
}