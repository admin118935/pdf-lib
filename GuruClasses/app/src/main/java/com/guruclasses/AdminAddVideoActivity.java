package com.guruclasses;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class AdminAddVideoActivity extends AppCompatActivity {

    private Spinner spinnerSubject;
    private TextInputEditText etTitle, etLink, etDescription;
    private MaterialButton btnSave;
    private RecyclerView rvVideos;
    private DatabaseHelper db;
    private String[] subjects = {"Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};
    private List<int[]> videoIds = new ArrayList<>();
    private List<String[]> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_video);

        db = new DatabaseHelper(this);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etTitle = findViewById(R.id.etTitle);
        etLink = findViewById(R.id.etLink);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        rvVideos = findViewById(R.id.rvVideos);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveVideo());
        loadVideos();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void saveVideo() {
        String subject = spinnerSubject.getSelectedItem().toString();
        String title = getText(etTitle);
        String link = getText(etLink);
        String desc = getText(etDescription);

        if (title.isEmpty() || link.isEmpty()) {
            Toast.makeText(this, "Title aur link zaroori hain", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addVideo(subject, title, link, desc)) {
            Toast.makeText(this, "Video save ho gaya!", Toast.LENGTH_SHORT).show();
            etTitle.setText("");
            etLink.setText("");
            etDescription.setText("");
            loadVideos();
        } else {
            Toast.makeText(this, "Save nahi hua, dobara koshish karein", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVideos() {
        videoList.clear();
        videoIds.clear();
        Cursor cursor = db.getAllVideos();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                videoIds.add(new int[]{cursor.getInt(cursor.getColumnIndexOrThrow("id"))});
                videoList.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("subject"))
                });
            }
            cursor.close();
        }

        rvVideos.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] video = videoList.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvVideoTitle)).setText(video[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvSubject)).setText(video[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvDescription)).setText("");
                holder.itemView.findViewById(R.id.ivPlay).setOnClickListener(v -> {
                    int vid = videoIds.get(position)[0];
                    new AlertDialog.Builder(AdminAddVideoActivity.this)
                            .setTitle("Delete Karein?")
                            .setMessage(video[0] + " delete karna chahte hain?")
                            .setPositiveButton("Haan", (d, w) -> {
                                if (db.deleteVideo(vid)) {
                                    Toast.makeText(AdminAddVideoActivity.this, "Delete ho gaya", Toast.LENGTH_SHORT).show();
                                    loadVideos();
                                }
                            })
                            .setNegativeButton("Nahi", null).show();
                });
            }

            @Override
            public int getItemCount() { return videoList.size(); }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
