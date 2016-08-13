package com.codepath.apps.mysimpletweets.utils;

import android.text.format.DateUtils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by iris on 8/6/16.
 */
public class ConvertJsonDate {
    private  static final int MINUTE_IN_SECONDS = 60;
    private  static final int HOUR_IN_SECONDS = 60 * MINUTE_IN_SECONDS;
    private static final int DAY_IN_SECONDS =  24 * HOUR_IN_SECONDS;
    private static final int WEEK_IN_SECONDS =  7 * DAY_IN_SECONDS;

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            long currTimeMillis = System.currentTimeMillis();
            long timeDiff = (currTimeMillis - dateMillis) / 1000;
            if (timeDiff / WEEK_IN_SECONDS > 0) {
                Date tweetDate = new Date(dateMillis);
                Date currDate = new Date(currTimeMillis);
                SimpleDateFormat dateFormat;
                if (tweetDate.getYear() == currDate.getYear()) {
                    dateFormat = new SimpleDateFormat("dd MMM");
                } else {
                    dateFormat = new SimpleDateFormat("dd MMM yy");
                }
                return dateFormat.format(tweetDate);
            } else if (timeDiff / DAY_IN_SECONDS > 0 ) {
                return String.format("%dd", timeDiff / DAY_IN_SECONDS);
            } else if (timeDiff / HOUR_IN_SECONDS > 0 ){
                return String.format("%dh", timeDiff / HOUR_IN_SECONDS);
            } else if (timeDiff / MINUTE_IN_SECONDS > 0){
                return String.format("%dm", timeDiff / MINUTE_IN_SECONDS);
            } else {
                return String.format("%ds", timeDiff);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
