package com.example.cs571hw9;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Scanner;

public class DetailsActivity extends AppCompatActivity {

//    private JSONObject result = new JSONObject();
    private RecyclerView recyclerView;
    private CastAdapter adapter;

    private RecyclerView reviewRecycler;
    private ReviewAdapter rAdapter;

    private RecyclerView recRecycler;
    private RecAdapter recAdapter;

    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    private TextView overviewText;
    private TextView genreText;
    private TextView yearText;
    private TextView castText;
    private TextView reviewsText;
    private TextView recText;
    private TextView detailName;

    private ImageView watchBtn;
    private ImageView twitter;
    private ImageView facebook;

    private ImageView detailBack;

    private YouTubePlayerView youtube;

    private ProgressBar detailProgress;
    private TextView detailLoad;

    private ReadMoreTextView detailOverview;

    private TextView detailGenres;
    private TextView detailYear;

    private boolean hasVideo = false;

    // shared preference init
    SharedPreferences pref;
    SharedPreferences.Editor editor;

//    boolean inWatchlist = false;

    // for facebook, twitter button
    private void visitLink(String site, String type, String id) {
        String tmdb = "https://www.themoviedb.org/" + type + "/" + id;
        Intent browserIntent;
        if (site.equals("twitter")) {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=check%20this%20out&url=" + tmdb));
        } else {
            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + tmdb));
        }

        startActivity(browserIntent);
    }

    public String[] addToString(String input, String input2, String input3, String keys, String paths, String names) {

        String output = "";
        String output2 = "";
        String output3 = "";
        Boolean inList = false;
        Scanner scan = new Scanner(keys);
        Scanner scan2 = new Scanner(paths);
        Scanner scan3 = new Scanner(names);
//        scan3.useDelimiter("^");
        while (scan.hasNext()) {
            String temp = scan.next();
            String temp2 = scan2.next();
            String temp3 = scan3.nextLine();
            if (!temp.equals(input)) {
                output += (temp + " ");
                output2 += (temp2 + " ");
                output3 += (temp3 + "\n");
            } else {
                inList = true;
            }
        }
        if (!inList) {
            output += (input + " ");
            output2 += (input2 + " ");
            output3 += (input3 + "\n");
        }

        String[] results = {output, output2, output3};
        return results;
    }

    public void setWatchList(String type, String id, String poster_path, String name) {
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String key = type + id;

        String keys = "";
        String paths = "";
        String names = "";

        if (pref.getString("keys", null) == null) {
            editor.putString("keys", "");
            editor.putString("paths", "");
            editor.putString("names", "");
            editor.commit();
        } else {
            keys = pref.getString("keys", null);
            paths = pref.getString("paths", null);
            names = pref.getString("names", null);
        }

        String[] results = addToString(key, poster_path, name, keys, paths, names);

        editor.clear();
        editor.putString("keys", results[0]);
        editor.putString("paths", results[1]);
        editor.putString("names", results[2]);
        editor.commit();
    }

    // add/rm to shared preference according to status
    private boolean setWatch(String type, String id, String poster_path, String name) {
        String key = type + id;
        boolean setAdd = false;
        if (inWatchlist(type, id)) {
            setAdd = true;
//            editor.remove(key);
            setWatchList(type, id, poster_path, name);
            Toast.makeText(this, name + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        } else {
            setAdd = false;
//            editor.putString(key, poster_path);
            setWatchList(type, id, poster_path, name);
            Toast.makeText(this, name + " was added to Watchlist", Toast.LENGTH_SHORT).show();
        }

//        MainActivity.R.id.watch_view.
//        editor.commit();
        return setAdd;
    }

    private boolean inWatchlist(String type, String id) {
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String key = type + id;

        String keys = "";

        if (pref.getString("keys", null) == null) {
            editor.putString("keys", "");
            editor.putString("paths", "");
            editor.putString("names", "");
            editor.commit();
            return false;
        } else {
            keys = pref.getString("keys", null);
//            paths = pref.getString("paths", null);

            Scanner scan = new Scanner(keys);

            while (scan.hasNext()) {
                String temp = scan.next();
                if (temp.equals(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        findViewById(android.R.id.content).getRootView().setBackgroundColor(0xFF1c272e);
        setTheme(R.style.Theme_CS571HW9_noActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String[] message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
//        TextView detailType = findViewById(R.id.detail_type);
//        TextView detailId = findViewById(R.id.detail_id);
//        detailType.setText(message[0]);
//        detailId.setText(message[1]);

        // set the pref objects
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

        // set up recyclerView
        recyclerView = findViewById(R.id.cast_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

//        reviewRecycler = findViewById(R.id.review_recycler);
        reviewRecycler = findViewById(R.id.review_recycler);
        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);
        reviewRecycler.setLayoutManager(rLayoutManager);

        recRecycler = findViewById(R.id.rec_recycler);
        RecyclerView.LayoutManager recLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recRecycler.setLayoutManager(recLayoutManager);

        youtube = findViewById(R.id.youtube_player_view);

        detailName = findViewById(R.id.detail_name);
        detailOverview = findViewById(R.id.detail_overview);
        detailGenres = findViewById(R.id.detail_genres);
        detailYear = findViewById(R.id.detail_year);

        TextView textReview = findViewById(R.id.reviews_text);

        watchBtn = findViewById(R.id.watchlist_button);
        twitter = findViewById(R.id.tw_button);
        facebook = findViewById(R.id.fb_button);
        detailBack = findViewById(R.id.rec_backdrop);

        detailProgress = (ProgressBar) findViewById(R.id.detail_progress);
        detailLoad = findViewById(R.id.detail_load);
        detailProgress.setVisibility(View.VISIBLE);
        detailLoad.setVisibility(View.VISIBLE);

        overviewText = findViewById(R.id.overview_text);
        genreText = findViewById(R.id.genres_text);
        yearText = findViewById(R.id.year_text);
        castText = findViewById(R.id.cast_text);
        reviewsText = findViewById(R.id.reviews_text);
        recText = findViewById(R.id.rec_text);

        hideAll();

//        TextView test = findViewById(R.id.test_text);

        final String url = "https://cs571hw9-be.wl.r.appspot.com";

        String type = message[0];
        String id = message[1];

        Log.d("DETAIL INFO: " + type, id);

//        inWatchlist = pref.getString(type+id, null) != null;

        RequestQueue queue = Volley.newRequestQueue(this);

        String cast_url = url + "/cast/" + type + "?id=" + id;
        JsonObjectRequest castRequest = new JsonObjectRequest
            (Request.Method.GET, cast_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Display the first 500 characters of the response string.
//                    JSONObject casts = response;
                    try {
                        JSONArray casts = response.getJSONArray("cast");
                        ArrayList<CastInfo> castList = new ArrayList<>();

                        for (int i = 0; i < Math.min(6, casts.length()); i++) {
                            String name = casts.getJSONObject(i).getString("name");
                            String profile_path = casts.getJSONObject(i).getString("profile_path");
                            if (profile_path.equals("null")) {
                                profile_path = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/person-placeholder.png";
                            } else {
                                profile_path = "https://image.tmdb.org/t/p/w185" + profile_path;
                            }
                            // add to RecyclerView adapter
                            castList.add(new CastInfo(name, profile_path));
                        }
                        adapter = new CastAdapter(castList);
                        recyclerView.setAdapter(adapter);

                        displayAll();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                      textView.setText(get_url + ": That didn't work!");
                }
            });

        String trailer_url = url + "/trailer/" + type + "?id=" + id;
        JsonObjectRequest trailerRequest = new JsonObjectRequest
                (Request.Method.GET, trailer_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
//                    JSONObject casts = response;
                        try {
                            JSONArray videos = response.getJSONArray("results"); // 'results', not 'result'

                            if (videos.length() == 0) {
                                youtube.setVisibility(View.INVISIBLE);
                                detailBack.setVisibility(View.VISIBLE);
                                hasVideo = false;

                            } else {

                                hasVideo = true;

                                youtube.setVisibility(View.VISIBLE);
                                detailBack.setVisibility(View.INVISIBLE);

                                String key = videos.getJSONObject(0).getString("key");
                                if (!key.equals("null")) {
                                    detailBack.setVisibility(View.GONE);
                                }

                                getLifecycle().addObserver(youtube);

                                youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        String videoId = key;
                                        youTubePlayer.cueVideo(videoId, 0);
                                    }
                                });

//                                detailName.setText(key);
                            }




//                            youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                                @Override
//                                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                                    String videoId = key;
//                                    youTubePlayer.loadVideo(videoId, 0);
//                                }
//                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                      test.setText("That didn't work!");
                    }
                });


        String review_url = url + "/review/" + type + "?id=" + id;
        JsonObjectRequest reviewRequest = new JsonObjectRequest
                (Request.Method.GET, review_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
//                    JSONObject casts = response;
                        try {
                            JSONArray reviews = response.getJSONArray("results");
                            ArrayList<ReviewInfo> reviewList = new ArrayList<>();

                            for (int i = 0; i < Math.min(3, reviews.length()); i++) {
                                String author = reviews.getJSONObject(i).getString("author");
                                String date = reviews.getJSONObject(i).getString("updated_at");
                                String score = reviews.getJSONObject(i).getJSONObject("author_details").getString("rating");
                                String content = reviews.getJSONObject(i).getString("content");
//                                content = content.substring(0, Math.min(200, content.length())) + "...";

                                // add to RecyclerView adapter
                                reviewList.add(new ReviewInfo(author, date, score, content));

                            }

                            rAdapter = new ReviewAdapter(reviewList, getApplicationContext());
                            reviewRecycler.setAdapter(rAdapter);

                            if (reviewList.size() > 0) {
                                textReview.setVisibility(View.VISIBLE);
                                reviewRecycler.setVisibility(View.VISIBLE);

                            } else {
                                textReview.setVisibility(View.GONE);
                                reviewRecycler.setVisibility(View.GONE);
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                      textView.setText(get_url + ": That didn't work!");
                    }
                });

        String rec_url = url + "/rec/" + type + "?id=" + id;
        JsonArrayRequest recRequest = new JsonArrayRequest
                (Request.Method.GET, rec_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.
//                    JSONObject casts = response;
                        try {
//                            JSONArray reviews = response.getJSONArray("results");
                            ArrayList<RecInfo> recList = new ArrayList<>();

                            for (int i = 0; i < Math.min(10, response.length()); i++) {
                                String path = response.getJSONObject(i).getString("poster_path");

                                if (path.equals("null")) {
                                    path = "https://cinemaone.net/images/movie_placeholder.png";
                                } else {
                                    path = "https://image.tmdb.org/t/p/w154" + path;
                                }

                                String rec_id = response.getJSONObject(i).getString("id");

                                // add to RecyclerView adapter
                                recList.add(new RecInfo(path, type, rec_id));

                            }

                            recAdapter = new RecAdapter(recList, getApplicationContext());
                            recRecycler.setAdapter(recAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                      textView.setText(get_url + ": That didn't work!");
                    }
                });

        String get_url = url + "/detail/" + type + "?id=" + id;
        // Request a string response from the provided URL.
        JsonObjectRequest detailRequest = new JsonObjectRequest
            (Request.Method.GET, get_url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    // Display the first 500 characters of the response string.

                    try {

                        String name = "";
                        String year = "";

                        if (type.equals("movie")) {
                            name = response.getString("title");
                            year = response.getString("release_date").substring(0, 4);
                        } else if (type.equals("tv")) {
                            name = response.getString("name");
                            year = response.getString("first_air_date").substring(0, 4);
                        }

                        String poster_path = response.getString("poster_path");
                        String overview = response.getString("overview");

                        String genres = "";
                        JSONArray genres_array = response.getJSONArray("genres");
                        for (int i = 0; i < genres_array.length(); i++) {
                            genres += genres_array.getJSONObject(i).getString("name");
                            genres += ", ";
                        }
                        genres = genres.substring(0, genres.length() - 2);

                        detailName.setText(name);
                        detailYear.setText(year);
                        detailGenres.setText(genres);
                        detailOverview.setText(overview);
//                        detailOverview.setTrimLines(4);

                        String backdrop_path = response.getString("backdrop_path");
                        if (backdrop_path.equals("null")) {
                            backdrop_path = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
                        } else {
                            backdrop_path = "https://image.tmdb.org/t/p/w780" + backdrop_path;
                        }

                        Glide.with(getApplicationContext())
                                .load(backdrop_path)
                                .into(detailBack);

                        if (inWatchlist(type, id)) {
                            watchBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
                        }
                        String finalName = name;
                        watchBtn.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Do something in response to button click
                                if (setWatch(type, id, poster_path, finalName)) {
                                    watchBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_add_circle_outline_24));
//                                    Toast.makeText(getApplicationContext(), finalName + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                                } else {
                                    watchBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_remove_circle_outline_24));
//                                    Toast.makeText(getApplication(), finalName + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        facebook.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Do something in response to button click
                                visitLink("facebook", type, id);
                            }
                        });

                        twitter.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                // Do something in response to button click
                                visitLink("twitter", type, id);
                            }
                        });

                        // requests for additional information
                        queue.add(castRequest);
                        queue.add(trailerRequest);
                        queue.add(reviewRequest);
                        queue.add(recRequest);

//                            String path = result.getJSONArray(1).getJSONObject(i).getString("path");
//                            String type = result.getJSONArray(1).getJSONObject(i).getString("type");
//                            String id = result.getJSONArray(1).getJSONObject(i).getString("id");
//
//                            searchResults.add(new SearchCard(name, path, type, id));
//
//                            adapter = new CustomAdapter(searchResults);
//                            recyclerView.setAdapter(adapter);
//
//                            if (searchResults.size() == 0) {
//                                textView.setText("No result found");
//                            } else { // no result found for search query
//                                textView.setText("");
//                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
//                                textView.setText(get_url + ": That didn't work!");
                }

            });

        queue.add(detailRequest);

    }

    private void hideAll() {
        overviewText.setVisibility(View.INVISIBLE);
        genreText.setVisibility(View.INVISIBLE);
        yearText.setVisibility(View.INVISIBLE);
        castText.setVisibility(View.INVISIBLE);
        reviewsText.setVisibility(View.INVISIBLE);
        recText.setVisibility(View.INVISIBLE);
        detailName.setVisibility(View.INVISIBLE);

        watchBtn.setVisibility(View.INVISIBLE);
        twitter.setVisibility(View.INVISIBLE);
        facebook.setVisibility(View.INVISIBLE);

//        detailBack.setVisibility(View.INVISIBLE);

        youtube.setVisibility(View.INVISIBLE);

        detailOverview.setVisibility(View.INVISIBLE);
        detailGenres.setVisibility(View.INVISIBLE);
        detailYear.setVisibility(View.INVISIBLE);
    }

    private void displayAll() {
        detailProgress.setVisibility(View.GONE);
        detailLoad.setVisibility(View.GONE);

        overviewText.setVisibility(View.VISIBLE);
        genreText.setVisibility(View.VISIBLE);
        yearText.setVisibility(View.VISIBLE);
        castText.setVisibility(View.VISIBLE);
        reviewsText.setVisibility(View.VISIBLE);
        recText.setVisibility(View.VISIBLE);
        detailName.setVisibility(View.VISIBLE);

        watchBtn.setVisibility(View.VISIBLE);
        twitter.setVisibility(View.VISIBLE);
        facebook.setVisibility(View.VISIBLE);

//        detailBack.setVisibility(View.VISIBLE);

        youtube.setVisibility(View.VISIBLE);

        detailOverview.setVisibility(View.VISIBLE);
        detailGenres.setVisibility(View.VISIBLE);
        detailYear.setVisibility(View.VISIBLE);
    }
}