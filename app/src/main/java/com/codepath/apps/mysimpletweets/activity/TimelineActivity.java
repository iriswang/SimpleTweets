package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.mysimpletweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;


import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    private User user;
    private final int REQUEST_CODE = 20;
    private TweetsAdapter adapter;
    private RecyclerView rvTweets;
    private LinearLayoutManager _linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        getInitialHomeTimeline(25);

        setContentView(R.layout.activity_timeline);

        View view = findViewById(R.id.rvTweets);
        rvTweets = (RecyclerView) findViewById(R.id.rvTweets);
        _linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(_linearLayoutManager);
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(adapter);

        FloatingActionButton fabAddTweet = (FloatingActionButton) findViewById(R.id.fabAddTweet);
        fabAddTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleNewTweetActivity();
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpScrollListener();
        fetchUserInfo();
    }

    public void setUpScrollListener() {
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener(_linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreTweets(totalItemsCount);
            }
        });


    }

    public void fetchUserInfo() {
        client.getUserCreditentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                Log.d("test", user.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    public void loadMoreTweets(int itemsCount) {
        if (tweets.size() == 0) {
            getInitialHomeTimeline(itemsCount);
        } else {
            long lastTweetId = tweets.get(tweets.size() - 1).getUid();
            client.getMoreTweets(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    int size = adapter.getItemCount();
                    ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                    tweets.addAll(newTweets);
                    adapter.notifyItemRangeInserted(size, newTweets.size());
                    Log.d("LOAD MORE TWEETS", newTweets.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }, lastTweetId, itemsCount);
        }
    }

    private void getInitialHomeTimeline(int itemsCount) {
        client.getInitialHomeTimeline(
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    int size = adapter.getItemCount();
                    ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                    tweets.addAll(newTweets);
                    adapter.notifyItemRangeInserted(size, newTweets.size());
                    Log.d("INTIAL LOAD", newTweets.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }, itemsCount);
    }

    private void handleNewTweetActivity() {
        Intent i = new Intent(this, NewTweetActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String status = data.getExtras().getString("tweet");
            // Toast the name to display temporarily on screen
            client.postTweet(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweets.clear();
                    adapter.notifyDataSetChanged();
                    getInitialHomeTimeline(25);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString,
                    Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            }, status);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
