package com.example.studynotesapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotesActivity extends AppCompatActivity {

    ArrayList<String> notesList = new ArrayList<>();
    ArrayList<String> notesContent = new ArrayList<>();
    ArrayList<String> notesId = new ArrayList<>();

    ArrayList<String> filteredTitles = new ArrayList<>();
    ArrayList<String> filteredContents = new ArrayList<>();
    ArrayList<String> filteredIds = new ArrayList<>();

    ArrayAdapter<String> adapter;

    ListView listView;

    TextView emptyText;
    TextView greetingText;
    TextView totalNotesText;

    EditText searchInput;

    Button logoutBtn;
    Button profileBtn;

    ImageButton menuBtn;
    LinearLayout menuLayout;

    String userId = "";
    String userName = "";
    String userEmail = "";
    String accessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Button addBtn = findViewById(R.id.addNoteBtn);

        listView = findViewById(R.id.notesList);

        emptyText = findViewById(R.id.emptyText);

        greetingText = findViewById(R.id.greetingText);

        totalNotesText = findViewById(R.id.totalNotesText);

        searchInput = findViewById(R.id.searchInput);

        logoutBtn = findViewById(R.id.logoutBtn);

        profileBtn = findViewById(R.id.profileBtn);

        menuBtn = findViewById(R.id.menuBtn);

        menuLayout = findViewById(R.id.menuLayout);

        userId = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("user_name");
        userEmail = getIntent().getStringExtra("user_email");
        accessToken = getIntent().getStringExtra("access_token");

        greetingText.setText("Oh, Hi " + userName + "!");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                filteredTitles
        );

        listView.setAdapter(adapter);

        menuBtn.setOnClickListener(v -> {

            if (menuLayout.getVisibility() == View.GONE) {

                menuLayout.setVisibility(View.VISIBLE);

            } else {

                menuLayout.setVisibility(View.GONE);
            }
        });

        profileBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            ProfileActivity.class
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
                    "user_email",
                    userEmail
            );

            intent.putExtra(
                    "access_token",
                    accessToken
            );

            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            MainActivity.class
                    );

            startActivity(intent);

            finish();
        });

        addBtn.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            this,
                            AddNoteActivity.class
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
                    "user_email",
                    userEmail
            );

            intent.putExtra(
                    "access_token",
                    accessToken
            );

            startActivity(intent);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent =
                    new Intent(
                            this,
                            ViewNoteActivity.class
                    );

            intent.putExtra(
                    "note_id",
                    filteredIds.get(position)
            );

            intent.putExtra(
                    "note_title",
                    filteredTitles.get(position)
            );

            intent.putExtra(
                    "note_content",
                    filteredContents.get(position)
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
                    "user_email",
                    userEmail
            );

            intent.putExtra(
                    "access_token",
                    accessToken
            );

            startActivity(intent);
        });

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    CharSequence s,
                    int start,
                    int count,
                    int after
            ) {

            }

            @Override
            public void onTextChanged(
                    CharSequence s,
                    int start,
                    int before,
                    int count
            ) {

                filterNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loadNotes();
    }

    private void filterNotes(String text) {

        filteredTitles.clear();
        filteredContents.clear();
        filteredIds.clear();

        for (int i = 0; i < notesList.size(); i++) {

            String title =
                    notesList.get(i);

            if (title.toLowerCase()
                    .contains(text.toLowerCase())) {

                filteredTitles.add(title);

                filteredContents.add(
                        notesContent.get(i)
                );

                filteredIds.add(
                        notesId.get(i)
                );
            }
        }

        adapter.notifyDataSetChanged();

        totalNotesText.setText(
                "📚 Total Notes: " +
                        filteredTitles.size()
        );

        emptyText.setVisibility(
                filteredTitles.size() == 0
                        ? View.VISIBLE
                        : View.GONE
        );
    }

    private void loadNotes() {

        String url =
                SupabaseConfig.SUPABASE_URL +
                        "/rest/v1/notes?user_id=eq." +
                        userId +
                        "&select=*";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,

                response -> {

                    try {

                        JSONArray jsonArray =
                                new JSONArray(response);

                        notesList.clear();
                        notesContent.clear();
                        notesId.clear();

                        filteredTitles.clear();
                        filteredContents.clear();
                        filteredIds.clear();

                        for (int i = 0;
                             i < jsonArray.length();
                             i++) {

                            JSONObject note =
                                    jsonArray.getJSONObject(i);

                            String id =
                                    note.getString("id");

                            String title =
                                    note.getString("title");

                            String content =
                                    note.getString("content");

                            notesId.add(id);
                            notesList.add(title);
                            notesContent.add(content);

                            filteredIds.add(id);
                            filteredTitles.add(title);
                            filteredContents.add(content);
                        }

                        adapter.notifyDataSetChanged();

                        totalNotesText.setText(
                                "📚 Total Notes: " +
                                        filteredTitles.size()
                        );

                        emptyText.setVisibility(
                                filteredTitles.size() == 0
                                        ? View.VISIBLE
                                        : View.GONE
                        );

                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                },

                error -> Toast.makeText(
                        this,
                        "Load failed",
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
}