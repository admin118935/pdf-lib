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

public class AdminAddQuizActivity extends AppCompatActivity {

    private Spinner spinnerSubject, spinnerAnswer;
    private TextInputEditText etQuestion, etOptionA, etOptionB, etOptionC, etOptionD;
    private RecyclerView rvQuestions;
    private DatabaseHelper db;
    private String[] subjects = {"Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};
    private String[] answers = {"A", "B", "C", "D"};
    private List<int[]> qIds = new ArrayList<>();
    private List<String[]> qList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_quiz);

        db = new DatabaseHelper(this);
        spinnerSubject = findViewById(R.id.spinnerSubject);
        spinnerAnswer = findViewById(R.id.spinnerAnswer);
        etQuestion = findViewById(R.id.etQuestion);
        etOptionA = findViewById(R.id.etOptionA);
        etOptionB = findViewById(R.id.etOptionB);
        etOptionC = findViewById(R.id.etOptionC);
        etOptionD = findViewById(R.id.etOptionD);
        rvQuestions = findViewById(R.id.rvQuestions);
        rvQuestions.setLayoutManager(new LinearLayoutManager(this));

        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, subjects);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubject.setAdapter(subjectAdapter);

        ArrayAdapter<String> answerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, answers);
        answerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnswer.setAdapter(answerAdapter);

        findViewById(R.id.btnSave).setOnClickListener(v -> saveQuestion());
        loadQuestions();
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void saveQuestion() {
        String subject = spinnerSubject.getSelectedItem().toString();
        String question = getText(etQuestion);
        String optA = getText(etOptionA);
        String optB = getText(etOptionB);
        String optC = getText(etOptionC);
        String optD = getText(etOptionD);
        String correct = spinnerAnswer.getSelectedItem().toString();

        if (question.isEmpty() || optA.isEmpty() || optB.isEmpty() || optC.isEmpty() || optD.isEmpty()) {
            Toast.makeText(this, "Sabhi fields bharein", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.addQuizQuestion(subject, question, optA, optB, optC, optD, correct)) {
            Toast.makeText(this, "Question save ho gaya!", Toast.LENGTH_SHORT).show();
            etQuestion.setText(""); etOptionA.setText(""); etOptionB.setText("");
            etOptionC.setText(""); etOptionD.setText("");
            loadQuestions();
        } else {
            Toast.makeText(this, "Save nahi hua", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestions() {
        qList.clear();
        qIds.clear();
        Cursor cursor = db.getAllQuestions();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                qIds.add(new int[]{cursor.getInt(cursor.getColumnIndexOrThrow("id"))});
                qList.add(new String[]{
                        cursor.getString(cursor.getColumnIndexOrThrow("question")),
                        cursor.getString(cursor.getColumnIndexOrThrow("subject")),
                        cursor.getString(cursor.getColumnIndexOrThrow("correct_answer"))
                });
            }
            cursor.close();
        }

        rvQuestions.setAdapter(new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                TextView tv = new TextView(parent.getContext());
                tv.setPadding(16, 12, 16, 12);
                tv.setTextSize(14);
                tv.setTextColor(0xFF333333);
                return new RecyclerView.ViewHolder(tv) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                String[] q = qList.get(position);
                TextView tv = (TextView) holder.itemView;
                tv.setText((position + 1) + ". [" + q[1] + "] " + q[0] + "  (Ans: " + q[2] + ")");
                tv.setOnLongClickListener(v -> {
                    int qid = qIds.get(position)[0];
                    new AlertDialog.Builder(AdminAddQuizActivity.this)
                            .setTitle("Delete Karein?")
                            .setMessage("Yeh question delete karna chahte hain?")
                            .setPositiveButton("Haan", (d, w) -> {
                                if (db.deleteQuestion(qid)) {
                                    Toast.makeText(AdminAddQuizActivity.this, "Delete ho gaya", Toast.LENGTH_SHORT).show();
                                    loadQuestions();
                                }
                            })
                            .setNegativeButton("Nahi", null).show();
                    return true;
                });
            }

            @Override
            public int getItemCount() { return qList.size(); }
        });
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }
}
