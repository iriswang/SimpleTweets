package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
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
import org.w3c.dom.Text;

public class NewTweetActivity extends AppCompatActivity {

    private User user;
    private EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tweet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
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

        final TextView tvCharactersLeft = (TextView) findViewById(R.id.tvCharactersLeft);
        tvCharactersLeft.setText("140");
        etTweet = (EditText) findViewById(R.id.etEditStatus);

        etTweet.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int charactersLeft = 140 - s.length();
                if (charactersLeft < 0) {
                    tvCharactersLeft.setTextColor(Color.RED);
                } else {
                    tvCharactersLeft.setTextColor(getResources().getColor(
                        R.color.grayTextColor));
                }
                tvCharactersLeft.setText(Integer.toString(charactersLeft));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
