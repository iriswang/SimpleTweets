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
 * Created by iris on 8/13/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    public static final String USER_ID = "timeline_user_id";

    public static UserTimelineFragment newInstance(long userId) {
        UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        getInitialHomeTimeline(25);
        fetchUserInfo();
    }

    public void getInitialHomeTimeline(int itemsCount) {
        long userId = getArguments().getLong(USER_ID);
        client.getUserTimeline(
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
            }, userId);
    }

    public void loadMoreTweets(int itemsCount) {
        long userId = getArguments().getLong(USER_ID);
        if (tweets.size() == 0) {
            getInitialHomeTimeline(itemsCount);
        } else {
            long lastTweetId = tweets.get(tweets.size() - 1).getUid();
            client.getMoreUserTweets(new JsonHttpResponseHandler() {
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
            }, userId, lastTweetId, itemsCount);
        }
    }

}
