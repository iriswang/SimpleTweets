package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by iris on 8/12/16.
 */
public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
  }

    public void getInitialHomeTimeline(int itemsCount) {
        client.getInitialHomeTimeline(
            new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    ArrayList<Tweet> newTweets = Tweet.fromJsonArray(response);
                    addAll(newTweets);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable,
                    JSONObject errorResponse) {
                    Log.d("DEBUG", errorResponse.toString());
                }
            }, itemsCount);
    }

    public void loadMoreTweets(int itemsCount) {
        if (tweets.size() == 0) {
            getInitialHomeTimeline(itemsCount);
        } else {
            long lastTweetId = tweets.get(tweets.size() - 1).getUid();
            client.getMoreHomeTweets(new JsonHttpResponseHandler() {
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
}
