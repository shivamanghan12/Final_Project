package com.android.newsfeedapp.activity;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.android.newsfeedapp.R;

public class MainActivity extends AppCompatActivity {

    private Button mNewsFeed;
    private Button mNewYorkArticles;
    private Button mWebster;
    private Button mFlight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNewsFeed = (Button) findViewById(R.id.btn_news_feed);
        mNewYorkArticles = (Button) findViewById(R.id.btn_new_york_articles);
        mWebster = (Button) findViewById(R.id.btn_webster);
        mFlight=(Button)findViewById(R.id.btn_plane) ;

        mNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(intent);
            }
        });

        mNewYorkArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewYorkArticlesActivity.class);
                startActivity(intent);
            }
        });

        mWebster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WebsterActivity.class);
                startActivity(intent);
            }
        });

        mFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, flight_tracker.class);
                startActivity(intent);
            }
        });
    }
}
