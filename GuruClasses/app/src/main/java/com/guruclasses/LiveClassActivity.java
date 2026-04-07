package com.guruclasses;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class LiveClassActivity extends AppCompatActivity {

    private RecyclerView rvLiveClasses;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_class);

        db = new DatabaseHelper(this);
        rvLiveClasses = findViewById(R.id.rvLiveClasses);
        rvLiveClasses.setLayoutManager(new LinearLayoutManager(this));

        loadLiveClasses();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void loadLiveClasses() {
        Cursor cursor = db.getAllLiveClasses();
        List<String[]> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("class_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("meet_link")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("time"))
                });
            }
            cursor.close();
        }

        if (list.isEmpty()) {
            Toast.makeText(this, "Koi live class schedule nahi hai abhi", Toast.LENGTH_SHORT).show();
        }

        rvLiveClasses.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_class, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] cls = list.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvSubject)).setText(cls[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvClassName)).setText(cls[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvSchedule))
                        .setText("\uD83D\uDCC5 " + cls[3] + "  |  \uD83D\uDD52 " + cls[4]);
                MaterialButton btnJoin = holder.itemView.findViewById(R.id.btnJoin);
                btnJoin.setOnClickListener(v -> joinClass(cls[2]));
            }

            @Override
            public int getItemCount() { return list.size(); }
        });
    }

    private void joinClass(String link) {
        if (link == null || link.isEmpty()) {
            Toast.makeText(this, "Meeting link available nahi hai", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)));
        } catch (Exception e) {
            Toast.makeText(this, "Link open nahi ho saka", Toast.LENGTH_SHORT).show();
        }
    }
}
