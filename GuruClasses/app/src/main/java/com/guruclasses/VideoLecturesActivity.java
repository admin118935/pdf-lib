package com.guruclasses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class VideoLecturesActivity extends AppCompatActivity {

    private RecyclerView rvVideos;
    private LinearLayout llFilter;
    private DatabaseHelper db;
    private String currentSubject = "All";
    private String[] subjects = {"All", "Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_lectures);

        db = new DatabaseHelper(this);
        rvVideos = findViewById(R.id.rvVideos);
        llFilter = findViewById(R.id.llSubjectFilter);
        rvVideos.setLayoutManager(new LinearLayoutManager(this));

        String subject = getIntent().getStringExtra("subject");
        if (subject != null) currentSubject = subject;

        setupFilters();
        loadVideos();

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
                loadVideos();
            });
            llFilter.addView(btn);
        }
    }

    private void loadVideos() {
        Cursor cursor = db.getVideosBySubject(currentSubject);
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

        if (list.isEmpty()) {
            Toast.makeText(this, "Is subject mein koi video nahi hai", Toast.LENGTH_SHORT).show();
        }

        rvVideos.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] video = list.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvVideoTitle)).setText(video[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvSubject)).setText(video[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvDescription)).setText(video[2]);

                ImageView ivPlay = holder.itemView.findViewById(R.id.ivPlay);
                ivPlay.setOnClickListener(v -> openLink(video[3]));
                holder.itemView.setOnClickListener(v -> openLink(video[3]));
            }

            @Override
            public int getItemCount() { return list.size(); }
        });
    }

    private void openLink(String link) {
        if (link == null || link.isEmpty()) {
            Toast.makeText(this, "Link available nahi hai", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Link open nahi ho saka", Toast.LENGTH_SHORT).show();
        }
    }
}
