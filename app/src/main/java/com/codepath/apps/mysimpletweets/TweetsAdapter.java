package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.ConvertJsonDate;
import com.squareup.picasso.Picasso;


import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by iris on 8/6/16.
 */
public class TweetsAdapter extends
    RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row

        ImageView ivProfileImage;
        TextView tvUsername;
        TextView tvBody;
        TextView tvName;
        TextView tvTimeStamp;

        public ViewHolder(View itemView) {
            super(itemView);
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTimeStamp = (TextView) itemView.findViewById(R.id.tvTimeStamp);

        }
    }
    private List<Tweet> mTweets;
    private Context mContext;

    public TweetsAdapter(Context context, List<Tweet> tweets) {
        mTweets = tweets;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @Override
    public TweetsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_tweet, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TweetsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        final Tweet tweet = mTweets.get(position);

        // Set item views based on your views and data model
        viewHolder.tvTimeStamp.setText(ConvertJsonDate.getRelativeTimeAgo(tweet.getCreatedAt()));
        viewHolder.tvUsername.setText("@" + tweet.getUser().getScreenName());
        viewHolder.tvName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        viewHolder.ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl())
            .transform(new RoundedCornersTransformation(3, 3)).into(viewHolder.ivProfileImage);

        viewHolder.ivProfileImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra(UserTimelineFragment.USER_ID, tweet.getUser().getUid());
                getContext().startActivity(i);
            }
        });

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mTweets.size();
    }

}
