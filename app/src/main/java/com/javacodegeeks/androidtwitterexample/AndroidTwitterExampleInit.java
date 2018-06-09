package com.javacodegeeks.androidtwitterexample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;


public class AndroidTwitterExampleInit extends Activity {
    private static final String TWITTER_KEY = "RHMMjHEdZNmDJlILqrXbQq6cf";
//    private static final String TWITTER_KEY = "WDuhNUL7cEztJEKuPnqfBDV2K";
//    private static final String TWITTER_SECRET = "5vVNEtG2UvJBOgVbIZ6xBzb6sgwZefRMG7332JLPyeJVwGxqz4";
    private static final String TWITTER_SECRET = "VRTtC8oAi8z10O2meuN0MEg2NwXsZV1zeYTT03cBGY7xyurvsn";
    private static final String INSTA_SECRET = "e9912fa055e94518a96a8cefda3aa502";
    public static final String INSTA_ID = "39811890a32440c4945730ea57050b18";
    public static final String INSTA_URL = "http://www.aayaamlabs.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        startLoginActivity();
    }

    private void startLoginActivity() {
        startActivity(new Intent(this, AndroidTwitterExample.class));
    }

}
