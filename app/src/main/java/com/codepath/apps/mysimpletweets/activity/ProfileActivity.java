package com.codepath.apps.mysimpletweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;


import java.lang.reflect.Array;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    private TwitterClient _client;
    private User _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);
        _client = TwitterApplication.getRestClient();
        long userId = getIntent().getLongExtra(UserTimelineFragment.USER_ID, -1);
        fetchUserInfo(userId);
        if (savedInstanceState == null ) {
            UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(userId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
    }

    private void fetchUserInfo(long userId) {
        if (userId == -1 ) {
            _client.getUserCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    _user = User.fromJson(response);
                    getSupportActionBar().setTitle("@" + _user.getScreenName());
                    Log.d("TEST MY PROFILE: ", _user.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else {
            _client.getUserInfo(userId, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    _user = User.fromJsonArray(response).get(0);
                    Log.d("TEST MY PROFILE: ", _user.toString());
                    getSupportActionBar().setTitle(_user.getScreenName());
                    super.onSuccess(statusCode, headers, response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        }
    }
}
