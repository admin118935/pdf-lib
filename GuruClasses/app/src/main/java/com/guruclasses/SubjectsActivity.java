package com.guruclasses;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SubjectsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        findViewById(R.id.ivBack).setOnClickListener(v -> finish());

        String[] subjects = {"Maths", "GK", "Hindi", "English", "Science", "Biology", "Chemistry", "Physics"};
        int[] cardIds = {R.id.cardMaths, R.id.cardGK, R.id.cardHindi, R.id.cardEnglish,
                R.id.cardScience, R.id.cardBiology, R.id.cardChemistry, R.id.cardPhysics};

        for (int i = 0; i < cardIds.length; i++) {
            final String subject = subjects[i];
            CardView card = findViewById(cardIds[i]);
            if (card != null) {
                card.setOnClickListener(v -> {
                    Intent intent = new Intent(this, VideoLecturesActivity.class);
                    intent.putExtra("subject", subject);
                    startActivity(intent);
                });
            }
        }
    }
}
