package com.codepath.apps.mysimpletweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

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
        Log.d("FETCH_USER_INFO: ", Long.toString(userId));
        if (userId == -1 ) {
            _client.getUserCredentials(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    _user = User.fromJson(response);
                    populateUserInfo();
                    Log.d("TEST MY PROFILE: ", _user.getName());
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
                    populateUserInfo();
                    Log.d("TEST MY PROFILE: ", _user.toString());
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

    private void populateUserInfo() {
        getSupportActionBar().setTitle(_user.getScreenName());
        TextView tvFollower = (TextView) findViewById(R.id.tvFollowers);
        tvFollower.setText(String.format("%d FOLLOWERS", _user.getFollowerCount()));
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing.setText(String.format("%d FOLLOWING", _user.getFollowingCount()));
        TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDescription.setText(_user.getDescription());
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(_user.getName());
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        Picasso.with(getApplicationContext()).load(_user.getProfileImageUrl())
               .transform(new RoundedCornersTransformation(3, 3)).into(ivProfileImage);
        Log.d("CALLING FOR USER: ", _user.getName());
    }


}
