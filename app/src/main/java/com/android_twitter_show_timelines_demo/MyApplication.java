package com.android_twitter_show_timelines_demo;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

/**
 * Created by sonu on 17/01/18.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //initiate Twitter config
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig("bltZgcfW9bQBwfIbFDUQdzd07", "IRmdK2GLjAUW0DtUYL5jP6bT03jewLPoRuJVtKIK03FcRHqMy5"))//pass Twitter API Key and Secret
                .debug(true)
                .build();
        Twitter.initialize(config);
    }
}
