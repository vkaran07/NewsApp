package com.example.karan.newsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String news_url = " https://content.guardianapis.com/search?api-key=dec32bce-411f-4a88-828b-ee5943ab6980";
    private static final int news_loader_id = 1;
    private newsAdapter mAdapter;
    private TextView memptystate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.ListView);
        memptystate = (TextView) findViewById(R.id.Text);

        mAdapter = new newsAdapter(this, new ArrayList<News>());
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNew = mAdapter.getItem(position);
                Uri newsuri = Uri.parse(currentNew.getWebUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsuri);
                startActivity(webIntent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connectivityManager.getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            LoaderManager loader = getLoaderManager();
            loader.initLoader(news_loader_id, null, this);
            Log.i("Log my message", "Loader initiated");
            mAdapter = new newsAdapter(this, new ArrayList<News>());
            listView.setAdapter(mAdapter);
        } else {
            View loading = findViewById(R.id.Progressbar);
            loading.setVisibility(View.GONE);
            memptystate.setText(R.string.NO_CONNECTION);
        }

    }


    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, news_url);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {

        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
            Log.i("log my message", "data is not null");
        } else {
            View loadingIndicator = findViewById(R.id.Progressbar);
            loadingIndicator.setVisibility(View.GONE);
            memptystate.setText(R.string.NO_ARTICLES);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }


    public static class NewsLoader extends AsyncTaskLoader<List<News>> {

        private String murl;

        public NewsLoader(Context context, String s) {
            super(context);
            murl = s;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<News> loadInBackground() {
            if (murl == null) {
                return null;
            }
            return Queryutils.fetchNews(murl);
            // return feeds;
        }
    }
}




