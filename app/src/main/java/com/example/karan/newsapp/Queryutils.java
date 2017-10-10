package com.example.karan.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan on 4/13/2017.
 */
public class Queryutils {
    private static final String Tag = Queryutils.class.getSimpleName();

    public Queryutils() {

    }

    public static List<News> fetchNews(String rurl) {
        URL url = createUrl(rurl);
        String jsonrpse = null;
        try {
            jsonrpse = makehttpRequest(url);
        } catch (IOException e) {
            Log.e(Tag, "Problem making the HTTP request", e);
        }
        List<News> news = extractFeature(jsonrpse);
        return news;
    }

    private static String makehttpRequest(URL url) throws IOException {
        String jsonresponse = "";
        if (url == null) {
            return jsonresponse;
        }
        HttpURLConnection urlConnect = null;
        InputStream input = null;
        try {
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setRequestMethod("GET");
            urlConnect.setReadTimeout(10000);
            urlConnect.setConnectTimeout(12000);
            urlConnect.connect();
            if (urlConnect.getResponseCode() == 200) {
                input = urlConnect.getInputStream();
                jsonresponse = readFromStream(input);
            } else {
                Log.e(Tag, "Error Response code:" + urlConnect.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(Tag, "Problem retrieving the News", e);
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (input != null) {
                input.close();
            }

        }
        return jsonresponse;
    }

    private static String readFromStream(InputStream input) throws IOException {
        StringBuilder display = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStreamR = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader BufferedReader = new BufferedReader((inputStreamR));
            String line = BufferedReader.readLine();
            while (line != null) {
                display.append(line);
                line = BufferedReader.readLine();
            }
        }
        return display.toString();
    }

    private static List<News> extractFeature(String bookjson) {
        if (TextUtils.isEmpty(bookjson)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            JSONObject baseJsonresponse = new JSONObject(bookjson);
            JSONObject resObject = baseJsonresponse.getJSONObject("response");
            JSONArray NewsArray = resObject.getJSONArray("results");
            for (int i = 0; i < NewsArray.length(); i++) {
                JSONObject currentNews = NewsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String section = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                Log.i("Log my message", "Title is" + title);
                Log.i("Log my message", "webtitle is" + section);
                Log.i("Log my message", "url is" + url);
                news.add(new News(title, section, url));
            }
        } catch (JSONException e) {
            Log.e(Tag, "", e);
        }
        return news;
    }

    private static URL createUrl(String sUrl) {
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException except) {
            Log.e(Tag, "Error creating Url", except);
            return null;
        }
        return url;
    }

}
