package com.guruclasses;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        db = new DatabaseHelper(this);

        SharedPreferences prefs = getSharedPreferences("GuruClasses", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        String name = prefs.getString("student_name", "Student");

        ((TextView) findViewById(R.id.tvStudentName)).setText(name);

        if (studentId != -1) {
            int totalQuiz = db.getTotalQuizByStudent(studentId);
            double avgScore = db.getAvgScoreByStudent(studentId);

            ((TextView) findViewById(R.id.tvTotalQuiz)).setText(String.valueOf(totalQuiz));
            ((TextView) findViewById(R.id.tvAvgScore)).setText(String.format("%.0f%%", avgScore));
            ((TextView) findViewById(R.id.tvVideosWatched)).setText("0");

            loadHistory(studentId);
        }

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void loadHistory(int studentId) {
        Cursor cursor = db.getResultsByStudent(studentId);
        List<String[]> list = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("score")) + "/" +
                                cursor.getInt(cursor.getColumnIndexOrThrow("total")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date"))
                });
            }
            cursor.close();
        }

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progress, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] item = list.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvQuizSubject)).setText(item[0] + " Quiz");
                ((TextView) holder.itemView.findViewById(R.id.tvScoreResult)).setText(item[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvDate)).setText(item[2]);
            }

            @Override
            public int getItemCount() { return list.size(); }
        });
    }
}
