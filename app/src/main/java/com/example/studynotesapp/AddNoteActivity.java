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

public class AddNoteActivity extends AppCompatActivity {

    String noteId = "";
    String userId = "";
    String userName = "";
    String accessToken = "";

    EditText title;
    EditText content;

    Button saveBtn;
    Button deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.title);
        content = findViewById(R.id.content);

        saveBtn = findViewById(R.id.saveNoteBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        noteId =
                getIntent().getStringExtra("note_id");

        userId =
                getIntent().getStringExtra("user_id");

        userName =
                getIntent().getStringExtra("user_name");

        accessToken =
                getIntent().getStringExtra("access_token");

        String existingTitle =
                getIntent().getStringExtra("note_title");

        String existingContent =
                getIntent().getStringExtra("note_content");

        if (existingTitle != null) {
            title.setText(existingTitle);
        }

        if (existingContent != null) {
            content.setText(existingContent);
        }

        if (noteId == null || noteId.isEmpty()) {

            deleteBtn.setVisibility(Button.GONE);

            saveBtn.setOnClickListener(v -> {
                saveNote();
            });

        } else {

            saveBtn.setText("Update Note");

            saveBtn.setOnClickListener(v -> {
                updateNote();
            });

            deleteBtn.setOnClickListener(v -> {
                deleteNote();
            });
        }
    }

    private void saveNote() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/rest/v1/notes";

        StringRequest request =
                new StringRequest(
                        Request.Method.POST,
                        url,

                        response -> {

                            Toast.makeText(
                                    this,
                                    "Note Saved!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            openNotesPage();
                        },

                        error -> Toast.makeText(
                                this,
                                "Save failed",
                                Toast.LENGTH_SHORT
                        ).show()

                ) {

                    @Override
                    public byte[] getBody() {

                        try {

                            JSONObject json =
                                    new JSONObject();

                            json.put(
                                    "title",
                                    title.getText().toString()
                            );

                            json.put(
                                    "content",
                                    content.getText().toString()
                            );

                            json.put(
                                    "user_id",
                                    userId
                            );

                            return json.toString()
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
                                "Authorization",
                                "Bearer " + accessToken
                        );

                        headers.put(
                                "Content-Type",
                                "application/json"
                        );

                        headers.put(
                                "Prefer",
                                "return=representation"
                        );

                        return headers;
                    }
                };

        Volley.newRequestQueue(this).add(request);
    }

    private void updateNote() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/rest/v1/notes?id=eq." +
                        noteId;

        StringRequest request =
                new StringRequest(
                        Request.Method.PATCH,
                        url,

                        response -> {

                            Toast.makeText(
                                    this,
                                    "Note Updated!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            openNotesPage();
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

                            JSONObject json =
                                    new JSONObject();

                            json.put(
                                    "title",
                                    title.getText().toString()
                            );

                            json.put(
                                    "content",
                                    content.getText().toString()
                            );

                            return json.toString()
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

        StringRequest request =
                new StringRequest(
                        Request.Method.DELETE,
                        url,

                        response -> {

                            Toast.makeText(
                                    this,
                                    "Note Deleted!",
                                    Toast.LENGTH_SHORT
                            ).show();

                            openNotesPage();
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

    private void openNotesPage() {

        Intent intent =
                new Intent(
                        this,
                        NotesActivity.class
                );

        intent.putExtra(
                "user_id",
                userId
        );

        intent.putExtra(
                "user_name",
                userName
        );

        intent.putExtra(
                "access_token",
                accessToken
        );

        startActivity(intent);

        finish();
    }
}