package com.codepath.apps.mysimpletweets.models;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

/**
 * Created by iris on 8/6/16.
 */
public class Tweet {
    private String body;
    private long uid; // unique ID for the tweet

    private User user;
    private String createdAt;

    private boolean retweetedStatus;
    private String retweetedBy;

    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getRetweetedBy() {
        return retweetedBy;
    }

    public boolean isRetweetedStatus() {
        return retweetedStatus;
    }

    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            JSONObject tweetJson;
            if (json.has("retweeted_status")) {
                tweet.retweetedStatus = true;
                tweet.retweetedBy = json.getJSONObject("user").getString("name");
                tweetJson = json.getJSONObject("retweeted_status");
            } else {
                tweet.retweetedStatus = false;
                tweetJson = json;
            }
            tweet.body = tweetJson.getString("text");
            tweet.uid = tweetJson.getLong("id");
            tweet.createdAt = tweetJson.getString("created_at");
            tweet.user = User.fromJson(tweetJson.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject j = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJson(jsonArray.getJSONObject(i));
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

}
