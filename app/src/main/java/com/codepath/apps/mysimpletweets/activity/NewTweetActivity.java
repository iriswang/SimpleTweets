package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

public class NewTweetActivity extends AppCompatActivity {

    private User user;
    private EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));
        setUpViews();
    }

    private void setUpViews() {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(user.getName());
        TextView tvUsername = (TextView) findViewById(R.id.tvScreenName);
        tvUsername.setText("@" + user.getScreenName());
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
        etTweet = (EditText) findViewById(R.id.etEditStatus);

        Button cancelButton = (Button) findViewById(R.id.btnCancel);
        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Button tweetButton = (Button) findViewById(R.id.btnTweet);
        tweetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra("tweet", etTweet.getText().toString());
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

}
