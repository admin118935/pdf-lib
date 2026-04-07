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

public class AdminLiveClassActivity extends AppCompatActivity {

    private Spinner spinnerSubject;
    private TextInputEditText etClassName, etMeetLink, etDate, etTime;
    private RecyclerView rvClasses;
    private DatabaseHelper db;
    private String[] subjects = {"Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};
    private List<int[]> classIds = new ArrayList<>();
    private List<String[]> classList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_live_class);

        db = new DatabaseHelper(this);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etClassName = findViewById(R.id.etClassName);
        etMeetLink = findViewById(R.id.etMeetLink);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        rvClasses = findViewById(R.id.rvClasses);
        rvClasses.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);

        findViewById(R.id.btnSave).setOnClickListener(v -> saveClass());
        loadClasses();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void saveClass() {
        String subject = spinnerSubject.getSelectedItem().toString();
        String name = getText(etClassName);
        String link = getText(etMeetLink);
        String date = getText(etDate);
        String time = getText(etTime);

        if (name.isEmpty() || link.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Sabhi fields bharein", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addLiveClass(subject, name, link, date, time)) {
            Toast.makeText(this, "Class schedule ho gaya!", Toast.LENGTH_SHORT).show();
            etClassName.setText(""); etMeetLink.setText("");
            etDate.setText(""); etTime.setText("");
            loadClasses();
        } else {
            Toast.makeText(this, "Save nahi hua", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadClasses() {
        classList.clear();
        classIds.clear();
        Cursor cursor = db.getAllLiveClasses();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                classIds.add(new int[]{cursor.getInt(cursor.getColumnIndexOrThrow("id"))});
                classList.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("class_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("meet_link")),
                        cursor.getString(cursor.getColumnIndexOrThrow("date")),
                        cursor.getString(cursor.getColumnIndexOrThrow("time"))
                });
            }
            cursor.close();
        }

        rvClasses.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_live_class, parent, false);
                return new RecyclerView.ViewHolder(v) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] cls = classList.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvSubject)).setText(cls[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvClassName)).setText(cls[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvSchedule))
                        .setText("\uD83D\uDCC5 " + cls[3] + "  |  \uD83D\uDD52 " + cls[4]);
                MaterialButton btnJoin = holder.itemView.findViewById(R.id.btnJoin);
                btnJoin.setText("Delete");
                btnJoin.setOnClickListener(v -> {
                    int cid = classIds.get(position)[0];
                    new AlertDialog.Builder(AdminLiveClassActivity.this)
                            .setTitle("Delete?")
                            .setMessage(cls[1] + " class delete karna chahte hain?")
                            .setPositiveButton("Haan", (d, w) -> {
                                if (db.deleteLiveClass(cid)) {
                                    Toast.makeText(AdminLiveClassActivity.this, "Delete ho gaya", Toast.LENGTH_SHORT).show();
                                    loadClasses();
                                }
                            })
                            .setNegativeButton("Nahi", null).show();
                });
            }

            @Override
            public int getItemCount() { return classList.size(); }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
