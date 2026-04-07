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

public class AdminAddNotesActivity extends AppCompatActivity {

    private Spinner spinnerSubject;
    private TextInputEditText etTitle, etLink, etDescription;
    private RecyclerView rvNotes;
    private DatabaseHelper db;
    private String[] subjects = {"Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};
    private List<int[]> noteIds = new ArrayList<>();
    private List<String[]> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_notes);

        db = new DatabaseHelper(this);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        etTitle = findViewById(R.id.etTitle);
        etLink = findViewById(R.id.etLink);
        etDescription = findViewById(R.id.etDescription);
        rvNotes = findViewById(R.id.rvNotes);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(adapter);

        findViewById(R.id.btnSave).setOnClickListener(v -> saveNote());
        loadNotes();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void saveNote() {
        String subject = spinnerSubject.getSelectedItem().toString();
        String title = getText(etTitle);
        String link = getText(etLink);
        String desc = getText(etDescription);

        if (title.isEmpty() || link.isEmpty()) {
            Toast.makeText(this, "Title aur link zaroori hain", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addNote(subject, title, link, desc)) {
            Toast.makeText(this, "Note save ho gaya!", Toast.LENGTH_SHORT).show();
            etTitle.setText(""); etLink.setText(""); etDescription.setText("");
            loadNotes();
        } else {
            Toast.makeText(this, "Save nahi hua", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNotes() {
        noteList.clear();
        noteIds.clear();
        Cursor cursor = db.getAllNotes();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                noteIds.add(new int[]{cursor.getInt(cursor.getColumnIndexOrThrow("id"))});
                noteList.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("subject"))
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
                String[] note = noteList.get(position);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteTitle)).setText(note[0]);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteSubject)).setText(note[1]);
                ((TextView) holder.itemView.findViewById(R.id.tvNoteDesc)).setText("");
                MaterialButton btn = holder.itemView.findViewById(R.id.btnDownload);
                btn.setText("Delete");
                btn.setOnClickListener(v -> {
                    int nid = noteIds.get(position)[0];
                    new AlertDialog.Builder(AdminAddNotesActivity.this)
                            .setTitle("Delete?")
                            .setMessage(note[0] + " delete karna chahte hain?")
                            .setPositiveButton("Haan", (d, w) -> {
                                if (db.deleteNote(nid)) {
                                    Toast.makeText(AdminAddNotesActivity.this, "Delete ho gaya", Toast.LENGTH_SHORT).show();
                                    loadNotes();
                                }
                            })
                            .setNegativeButton("Nahi", null).show();
                });
            }

            @Override
            public int getItemCount() { return noteList.size(); }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
