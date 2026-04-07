package com.guruclasses;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AdminStudentsActivity extends AppCompatActivity {

    private RecyclerView rvStudents;
    private TextView tvCount;
    private DatabaseHelper db;
    private List<int[]> studentIds = new ArrayList<>();
    private List<String[]> studentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_students);

        db = new DatabaseHelper(this);
        rvStudents = findViewById(R.id.rvStudents);
        tvCount = findViewById(R.id.tvCount);
        rvStudents.setLayoutManager(new LinearLayoutManager(this));

        loadStudents();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void loadStudents() {
        studentList.clear();
        studentIds.clear();

        Cursor cursor = db.getAllStudents();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                studentIds.add(new int[]{cursor.getInt(cursor.getColumnIndexOrThrow("id"))});
                studentList.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("full_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("mobile")),
                        cursor.getString(cursor.getColumnIndexOrThrow("class_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username"))
                });
            }
            cursor.close();
        }

        tvCount.setText(studentList.size() + " Students");

        rvStudents.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] s = studentList.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvInitial))
                        .setText(s[0].substring(0, 1).toUpperCase());
                ((TextView) holder.itemView.findViewById(R.id.tvName)).setText(s[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvMobile)).setText("\uD83D\uDCF1 " + s[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvClassInfo)).setText("Class: " + s[2]);

                ImageView ivDelete = holder.itemView.findViewById(R.id.ivDelete);
                ivDelete.setOnClickListener(v -> {
                    int sid = studentIds.get(position)[0];
                    new AlertDialog.Builder(AdminStudentsActivity.this)
                            .setTitle("Student Delete Karein")
                            .setMessage(s[0] + " ko delete karna chahte hain?")
                            .setPositiveButton("Haan", (d, w) -> {
                                if (db.deleteStudent(sid)) {
                                    Toast.makeText(AdminStudentsActivity.this, "Delete ho gaya", Toast.LENGTH_SHORT).show();
                                    loadStudents();
                                }
                            })
                            .setNegativeButton("Nahi", null)
                            .show();
                });
            }

            @Override
            public int getItemCount() { return studentList.size(); }
        });
    }
}
