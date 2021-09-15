package com.example.cs571hw9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).getRootView().setBackgroundColor(0xFF1c272e);
        setTheme(R.style.Theme_CS571HW9_noActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] message = intent.getStringArrayExtra(DetailsActivity.EXTRA_MESSAGE);

        String author = message[0];
        String date = message[1];
        String score = message[2];
        String content = message[3];

        TextView revAuthor = findViewById(R.id.rev_author);
        TextView revScore = findViewById(R.id.rev_score);
        TextView revContent = findViewById(R.id.rev_content);

        // format date
        date = date.replaceAll("[TZ]", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS ");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
        String formattedDate = dateTime.format(myFormatObj);


        revAuthor.setText("by " + author + " on " + formattedDate);
        if (!score.equals("null")) {
            revScore.setText((Math.round(Double.parseDouble(score)) / 2) + "/5");
        } else {
            ImageView revStar = findViewById(R.id.rev_star);
            revStar.setVisibility(View.INVISIBLE);
        }
        revContent.setText(content);
    }
}