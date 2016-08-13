package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.mysimpletweets.EndlessRecyclerViewScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsAdapter;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activity.NewTweetActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;


import org.json.JSONObject;
import org.parceler.Parcels;


import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by iris on 8/12/16.
 */
public abstract class TweetsListFragment extends Fragment {
    private final int REQUEST_CODE = 20;

    protected  ArrayList<Tweet> tweets;
    protected TweetsAdapter adapter;
    private RecyclerView rvTweets;
    private LinearLayoutManager _linearLayoutManager;
    protected TwitterClient client;
    protected User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        rvTweets = (RecyclerView) v.findViewById(R.id.rvTweets);
        rvTweets.setLayoutManager(_linearLayoutManager);
        rvTweets.setAdapter(adapter);
        FloatingActionButton fabAddTweet = (FloatingActionButton) v.findViewById(R.id.fabAddTweet);
        fabAddTweet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                handleNewTweetActivity();
            }
        });
        return v;
    }

    //creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(getActivity(), tweets);
        _linearLayoutManager = new LinearLayoutManager(getActivity());
        client = TwitterApplication.getRestClient();
        getInitialHomeTimeline(25);
        fetchUserInfo();
    }

    public void addAll(List<Tweet> newTweets) {
        tweets.addAll(newTweets);
        adapter.notifyDataSetChanged();
    }

    public void clearTweets() {
        tweets.clear();
        adapter.notifyDataSetChanged();
    }

    private void handleNewTweetActivity() {
        Intent i = new Intent(getActivity(), NewTweetActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == REQUEST_CODE) {
            String status = data.getExtras().getString("tweet");
            // Toast the name to display temporarily on screen
            client.postTweet(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    clearTweets();
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

    public void fetchUserInfo() {
        client.getUserCredentials(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJson(response);
                setUpScrollListener();
                Log.d("test", user.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

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

    abstract void getInitialHomeTimeline(int count);

    abstract void loadMoreTweets(int count);

}
