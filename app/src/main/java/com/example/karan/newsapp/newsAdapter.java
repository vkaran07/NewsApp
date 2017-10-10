package com.example.karan.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by karan on 4/11/2017.
 */
public class newsAdapter extends ArrayAdapter<News> {
    public newsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);

    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View ListItemView = convertView;
        if (ListItemView == null) {
            ListItemView = LayoutInflater.from(getContext()).inflate(R.layout.list, parent, false);
        }
        News currentnews = getItem(position);
        Log.i("My message is", "Title is :-" + currentnews.getmTitle());
        TextView txt = (TextView) ListItemView.findViewById(R.id.Title);
        txt.setText(currentnews.getmTitle());

        TextView txt2 = (TextView) ListItemView.findViewById(R.id.Description);
        txt2.setText(currentnews.getsec());
        return ListItemView;
    }
}
