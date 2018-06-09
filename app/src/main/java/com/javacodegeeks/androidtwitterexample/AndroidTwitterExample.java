package com.javacodegeeks.androidtwitterexample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javacodegeeks.androidtwitterexample.util.Constants;
import com.javacodegeeks.androidtwitterexample.util.InstagramApp;
import com.javacodegeeks.androidtwitterexample.util.InstagramSession;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.*;
import com.twitter.sdk.android.core.identity.*;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.util.HashMap;


public class AndroidTwitterExample extends AppCompatActivity implements View.OnClickListener{
    private TwitterLoginButton twitterButton;
    TextView userId,userName;
    ImageView userProfile;
    private InstagramSession mSession;
    private InstagramApp mApp;
    private Button btnConnect;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(AndroidTwitterExample.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        mSession = new InstagramSession(getApplicationContext());
        userId=findViewById(R.id.textViewUserId);
        userName=findViewById(R.id.textViewUsername);
        userProfile=findViewById(R.id.userProfilePic);
        btnConnect = (Button) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(this);
        setUpViews();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterButton.onActivityResult(requestCode, resultCode, data);
    }

    private void setUpViews() {
        setUpTwitterButton();
        mApp = new InstagramApp(this, Constants.INSTA_CLIENT_ID,
                Constants.INSTA_CLIENT_SECRET, Constants.INSTA_CALLBACK_URL);
        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                // tvSummary.setText("Connected as " + mApp.getUserName());
                btnConnect.setText("Logout");
                // userInfoHashmap = mApp.
                mApp.fetchUserName(handler);
                setInstragramData(true);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT)
                        .show();
            }
        });


        if (mApp.hasAccessToken()) {
            // tvSummary.setText("Connected as " + mApp.getUserName());
            btnConnect.setText("LogOut");
            mApp.fetchUserName(handler);
/*
            Log.w("Id",mApp.getId());
            Log.w("Name",mApp.getName());
            Log.w("Token",mApp.getTOken());
            Log.w("UserName",mApp.getUserName());
            Log.w("Profile",mApp.getProfilePicture());*/
            setInstragramData(true);
        }

    }
public void setInstragramData(boolean isLogin)
{
    if (isLogin && mSession.getLoginType().equals("insta")) {
//        Log.w("InstraData",userInfoHashmap.get(InstagramApp.TAG_ID));
        Log.w("ID",mApp.getName());
        userId.setText(mApp.getId());
        userName.setText(mApp.getName());
        twitterButton.setVisibility(View.GONE);
        Picasso.with(this).load(mApp.getProfilePicture()).into(userProfile);
    }
    else if (isLogin && mSession.getLoginType().equals("twitter")) {
//        Log.w("InstraData",userInfoHashmap.get(InstagramApp.TAG_ID));
        Log.w("ID",mApp.getName());
        userId.setText(mSession.getId());
        userName.setText(mSession.getName());
        btnConnect.setText("Logout");
        twitterButton.setVisibility(View.INVISIBLE);
        Picasso.with(this).load(mSession.getUserProfile()).into(userProfile);
    }

}
    private void setUpTwitterButton() {
        twitterButton = (TwitterLoginButton) findViewById(R.id.twitter_button);

        twitterButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.w("Result",result+"");
           /*     Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();*/
                TwitterSession session = result.data;

                getTwitterProfile(session);
                if (session!=null)
                    Log.w("userName", session.getUserName());
                Log.w("Session",session.toString());
               // setUpViewsForTweetComposer();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.app_name),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTwitterProfile(final TwitterSession session) {
//This code will fetch the profile image URL
        //Getting the account service of the user logged in
        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void failure(TwitterException e) {
                        //If any error occurs handle it here
                    }

                    @Override
                    public void success(Result<User> userResult)
                    {
                        //If it succeeds creating a User object from userResult.data
//                        session.getAuthToken();
                        User user = userResult.data;
                            Log.w("\nUserClass",user.toString());
                        //Getting the profile image url
                        String profileImage = user.profileImageUrl.replace("_normal", "");
                        Log.w(",profileImage",profileImage);
                       userId.setText(user.idStr);
                       userName.setText(user.name);
                        mSession.storeAccessToken(String.valueOf(session.getAuthToken()), user.idStr, user.screenName, user.name,profileImage,"twitter");
//                        Log.w("Mobile",user.)
                        Picasso.with(getApplicationContext()).load(profileImage).error(R.drawable.dgts__logo).into(userProfile);
                        btnConnect.setText("Logout");
                        twitterButton.setVisibility(View.GONE);
                        //Creating an Intent
                     /*   Intent intent = new Intent(MainActivity.this, ProfileActivity.class);

                        //Adding the values to intent
                        intent.putExtra(KEY_USERNAME,username);
                        intent.putExtra(KEY_PROFILE_IMAGE_URL, profileImage);*/

                        //Starting intent
//                        startActivity(intent);
                    }
                });
    }

    private void setUpViewsForTweetComposer() {
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text("Just setting up my Fabric!");
        builder.show();
        }
        @Override
    public void onBackPressed()
        {
         finishAffinity();
        }

    @Override
    public void onClick(View view) {

        if (view == btnConnect) {
            connectOrDisconnectUser();
        }
    }
    private void connectOrDisconnectUser() {
        if (mSession.getAccessToken() != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    AndroidTwitterExample.this);
            builder.setMessage("Logout ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();
                                    // btnConnect.setVisibility(View.VISIBLE);
                                    twitterButton.setVisibility(View.VISIBLE);
                                    btnConnect.setText("Login With Instagram");
                                    // tvSummary.setText("Not connected");
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();
        }
    }
}
