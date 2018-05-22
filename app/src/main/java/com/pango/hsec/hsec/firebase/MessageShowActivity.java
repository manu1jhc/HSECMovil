package com.pango.hsec.hsec.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pango.hsec.hsec.R;

public class MessageShowActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView titleTextView;
    private TextView timeStampTextView;
    private TextView articleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_show);
        viewInitialization();

        //receive data from MyFirebaseMessagingService class
        String title = getIntent().getStringExtra("title");
        String timeStamp = getIntent().getStringExtra("timestamp");
        String article = getIntent().getStringExtra("article_data");
        String imageUrl = getIntent().getStringExtra("image");

        //Set data on UI
        titleTextView.setText(title);
        timeStampTextView.setText(timeStamp);
        articleTextView.setText(article);
        Glide.with(this)
                .load(imageUrl)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

    }

    private void viewInitialization() {
        imageView = (ImageView) findViewById(R.id.featureGraphics);
        titleTextView = (TextView) findViewById(R.id.header);
        timeStampTextView = (TextView) findViewById(R.id.timeStamp);
        articleTextView = (TextView) findViewById(R.id.article);
    }
}
