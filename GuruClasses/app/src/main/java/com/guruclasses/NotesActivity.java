package com.guruclasses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends AppCompatActivity {

    private RecyclerView rvNotes;
    private LinearLayout llFilter;
    private DatabaseHelper db;
    private String currentSubject = "All";
    private String[] subjects = {"All", "Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        db = new DatabaseHelper(this);
        rvNotes = findViewById(R.id.rvNotes);
        llFilter = findViewById(R.id.llSubjectFilter);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        setupFilters();
        loadNotes();

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void setupFilters() {
        for (String s : subjects) {
            MaterialButton btn = new MaterialButton(this,
                    null, com.google.android.material.R.attr.materialButtonOutlinedStyle);
            btn.setText(s);
            btn.setTextSize(12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);
            btn.setLayoutParams(params);
            btn.setOnClickListener(v -> {
                currentSubject = s;
                loadNotes();
            });
            llFilter.addView(btn);
        }
    }

    private void loadNotes() {
        Cursor cursor = db.getNotesBySubject(currentSubject);
        List<String[]> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getString(cursor.getColumnIndexOrThrow("link"))
                });
            }
            cursor.close();
        }

        rvNotes.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] note = list.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteTitle)).setText(note[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteSubject)).setText(note[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteDesc)).setText(note[2]);
                MaterialButton btnDownload = holder.itemView.findViewById(R.id.btnDownload);
                btnDownload.setOnClickListener(v -> openLink(note[3]));
            }

            @Override
            public int getItemCount() { return list.size(); }
        });

        if (list.isEmpty()) {
            Toast.makeText(this, "Is subject mein koi notes nahi hai", Toast.LENGTH_SHORT).show();
        }
    }

    private void openLink(String link) {
        if (link == null || link.isEmpty()) {
            Toast.makeText(this, "Link available nahi hai", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } catch (Exception e) {
            Toast.makeText(this, "Link open nahi ho saka", Toast.LENGTH_SHORT).show();
        }
    }
}
