package com.example.cs571hw9.ui.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.cs571hw9.CastAdapter;
import com.example.cs571hw9.R;
import com.example.cs571hw9.SliderAdapter;
import com.example.cs571hw9.SliderData;
import com.example.cs571hw9.TopAdapter;
import com.example.cs571hw9.TopInfo;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.ArrayList;



public class HomeFragment extends Fragment {

    // Urls of our images.
    private String url1 = "";
    private String url2 = "";
    private String url3 = "";

    private ArrayList<String[]> playingUrl;
    private ArrayList<String[]> trendUrl;

    private ArrayList<TopInfo> topMovieList;
    private ArrayList<TopInfo> topTVList;
    private ArrayList<TopInfo> popMovieList;
    private ArrayList<TopInfo> popTVList;

    private RecyclerView recyclerView, pRecyclerView, recycler1, recycler2;
    private TopAdapter adapter, pAdapter, adapter1, adapter2;

    private Boolean displayMovie = true;

    private ArrayList<SliderData> sliderDataArrayList;
    private SliderView sliderView;

    private TextView topText;
    private TextView popText;

    // method to load image
    static void loadImage(RequestManager glide, String url, ImageView view) {
        glide.load(url).into(view);
    }

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final Item homeIcon = root.findViewById(R.id.home_icon);

        playingUrl = new ArrayList<>();
        trendUrl = new ArrayList<>();
        topMovieList = new ArrayList<>();
        topTVList = new ArrayList<>();
        popMovieList = new ArrayList<>();
        popTVList = new ArrayList<>();

        final TextView titleView = (TextView) root.findViewById(R.id.text_title);
        final TextView res_info = (TextView) root.findViewById(R.id.response_info);
        final TextView footerView = (TextView) root.findViewById(R.id.text_footer);

        topText = (TextView) root.findViewById(R.id.top_text);
        popText = (TextView) root.findViewById(R.id.pop_text);
        topText.setVisibility(View.INVISIBLE);
        popText.setVisibility(View.INVISIBLE);

        final Button toggleMovie = (Button) root.findViewById(R.id.toggle_movie);
        final Button toggleTV = (Button) root.findViewById(R.id.toggle_tv);

        // set up recyclerView
        recyclerView = root.findViewById(R.id.top_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        pRecyclerView = root.findViewById(R.id.pop_recycler);
        RecyclerView.LayoutManager playoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        pRecyclerView.setLayoutManager(playoutManager);

        // init button color
        if (displayMovie) {
            toggleMovie.setTextColor(getResources().getColor(R.color.tmdb_blue));
        } else {
            toggleTV.setTextColor(getResources().getColor(R.color.tmdb_blue));
        }

        // set movie or tv display
        toggleMovie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (!displayMovie) {
                    displayMovie = true;
                     // set movie and tv text color
                    toggleMovie.setTextColor(getResources().getColor(R.color.tmdb_blue));
                    toggleTV.setTextColor(getResources().getColor(R.color.white));
                    loadSlider(playingUrl);
                    loadTop(topMovieList);
                    loadPop(popMovieList);
                }

            }
        });

        toggleTV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                if (displayMovie) {
                    displayMovie = false;

                    toggleTV.setTextColor(getResources().getColor(R.color.tmdb_blue));
                    toggleMovie.setTextColor(getResources().getColor(R.color.white));
                    loadSlider(trendUrl);
                    loadTop(topTVList);
                    loadPop(popTVList);
                }

            }
        });

//        int poster_num = 3;


        homeViewModel.getTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                titleView.setText(s);
            }
        });

        homeViewModel.getFooter().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                footerView.setText(s);
            }
        });

        // spinner
        ProgressBar spinner;
        spinner = (ProgressBar)root.findViewById(R.id.indeterminateBar);
        TextView homeLoad = root.findViewById(R.id.home_load);
        spinner.setVisibility(View.VISIBLE);
        homeLoad.setVisibility(View.VISIBLE);

        /* Image Slide in Fragment */
        // we are creating array list for storing our image urls.
        sliderDataArrayList = new ArrayList<>();

        // initializing the slider view.
        sliderView = (SliderView) root.findViewById(R.id.slider);
        sliderView.setVisibility(View.INVISIBLE);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = "https://cs571hw9-be.wl.r.appspot.com";

        String get_url = url + "/playing";
        // Request a string response from the provided URL.
        JsonArrayRequest playingRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.

                        try {
//                            Log.d("RES LEN", response.length() + "");
                            for (int i = 0; i < Math.min(6, response.length()); i++) {

                                String[] info = new String[3];

                                String path = response.getJSONObject(i).getString("poster_path");


                                if (!path.equals("null")) {
                                    info[0] = "https://image.tmdb.org/t/p/w500" + path;
                                } else {
                                    info[0] = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                info[1] = "movie";
                                info[2] = response.getJSONObject(i).getString("id");

                                playingUrl.add(info);

                            }
//                            Log.d("PLAYING AL: ", playingUrl.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (displayMovie) {
                            loadSlider(playingUrl);
                        }

                        // hide spinner
                        spinner.setVisibility(View.GONE);
                        homeLoad.setVisibility(View.GONE);
                        displayAll();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        backendView.setText("That didn't work!");
                    }

                });

        get_url = url + "/trendTV";
        // Request a string response from the provided URL.
        JsonArrayRequest trendRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Display the first 500 characters of the response string.

                        try {
                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                String[] info = new String[3];

                                String path = response.getJSONObject(i).getString("poster_path");


                                if (!path.equals("null")) {
                                    info[0] = "https://image.tmdb.org/t/p/w500" + path;
                                } else {
                                    info[0] = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                info[1] = "tv";
                                info[2] = response.getJSONObject(i).getString("id");

                                trendUrl.add(info);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!displayMovie) {
                            loadSlider(trendUrl);
                        }

                        // hide spinner
//                        spinner.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        backendView.setText("That didn't work!");
                    }

                });


        get_url = url + "/topTV";
        // Request a string response from the provided URL.
        JsonArrayRequest topTVRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                String name = response.getJSONObject(i).getString("name");
                                String type = "tv";
//                                if (response.getJSONObject(i).getString("title") != null) {
//                                    name = response.getJSONObject(i).getString("title");
//                                    type = "movie";
//                                } else {
//                                    name = response.getJSONObject(i).getString("name");
//                                    type = "tv";
//                                }

                                String path = "";
                                if (!response.getJSONObject(i).getString("poster_path").equals("null")) {
                                    path = "https://image.tmdb.org/t/p/w154" + response.getJSONObject(i).getString("poster_path");
                                } else {
                                    path = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                String id = response.getJSONObject(i).getString("id");

                                topTVList.add(new TopInfo(name, path, type, id));
                            }

                            if (!displayMovie) {
                                loadTop(topTVList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView debug = (TextView) root.findViewById(R.id.response_info);
                        debug.setText("TopTV error!");
                    }

                });

        get_url = url + "/topMovie";
        // Request a string response from the provided URL.
        JsonArrayRequest topMovieRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                String name = "";
                                String type = "";
                                if (!response.getJSONObject(i).getString("title").equals("null")) {
                                    name = response.getJSONObject(i).getString("title");
                                    type = "movie";
                                } else {
                                    name = response.getJSONObject(i).getString("name");
                                    type = "tv";
                                }

                                String path = "";
                                if (!response.getJSONObject(i).getString("poster_path").equals("null")) {
                                    path = "https://image.tmdb.org/t/p/w154" + response.getJSONObject(i).getString("poster_path");
                                } else {
                                    path = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                String id = response.getJSONObject(i).getString("id");

                                topMovieList.add(new TopInfo(name, path, type, id));
                            }

                            if (displayMovie) {
                                loadTop(topMovieList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        res_info.setText("TopMovie ERROR!");
                    }

                });


        get_url = url + "/popTV";
        // Request a string response from the provided URL.
        JsonArrayRequest popTVRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                String name = response.getJSONObject(i).getString("name");
                                String type = "tv";
//                                if (response.getJSONObject(i).getString("title") != null) {
//                                    name = response.getJSONObject(i).getString("title");
//                                    type = "movie";
//                                } else {
//                                    name = response.getJSONObject(i).getString("name");
//                                    type = "tv";
//                                }

                                String path = "";
                                if (!response.getJSONObject(i).getString("poster_path").equals("null")) {
                                    path = "https://image.tmdb.org/t/p/w154" + response.getJSONObject(i).getString("poster_path");
                                } else {
                                    path = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                String id = response.getJSONObject(i).getString("id");

                                popTVList.add(new TopInfo(name, path, type, id));
                            }

                            if (!displayMovie) {
                                loadPop(popTVList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        TextView debug = (TextView) root.findViewById(R.id.response_info);
                        debug.setText("PopTV error!");
                    }

                });

        get_url = url + "/popMovie";
        // Request a string response from the provided URL.
        JsonArrayRequest popMovieRequest = new JsonArrayRequest
                (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        try {
                            for (int i = 0; i < Math.min(6, response.length()); i++) {
                                String name = "";
                                String type = "";
                                if (!response.getJSONObject(i).getString("title").equals("null")) {
                                    name = response.getJSONObject(i).getString("title");
                                    type = "movie";
                                } else {
                                    name = response.getJSONObject(i).getString("name");
                                    type = "tv";
                                }

                                String path = "";
                                if (!response.getJSONObject(i).getString("poster_path").equals("null")) {
                                    path = "https://image.tmdb.org/t/p/w154" + response.getJSONObject(i).getString("poster_path");
                                } else {
                                    path = "https://cinemaone.net/images/movie_placeholder.png";
                                }

                                String id = response.getJSONObject(i).getString("id");

                                popMovieList.add(new TopInfo(name, path, type, id));
                            }

                            if (displayMovie) {
                                loadPop(popMovieList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        backendView.setText("That didn't work!");
                    }

                });



        // Add the request to the RequestQueue.

        queue.add(topTVRequest);
        queue.add(popTVRequest);
        queue.add(playingRequest);
        queue.add(trendRequest);
        queue.add(topMovieRequest);
        queue.add(popMovieRequest);


        // selectively load resource
        // slider

        // top-rated


        // popular


        return root;
    }

    private void displayAll() {
        topText.setVisibility(View.VISIBLE);
        popText.setVisibility(View.VISIBLE);
        sliderView.setVisibility(View.VISIBLE);
    }

    private void loadSlider(ArrayList<String[]> src) {

        sliderDataArrayList.clear();

        for (int i = 0; i < src.size(); i++) {
            sliderDataArrayList.add(new SliderData(src.get(i)[0], src.get(i)[1], src.get(i)[2]));
        }

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(getActivity(), sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }

    private void loadTop(ArrayList<TopInfo> src) {
        adapter = new TopAdapter(src, getContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadPop(ArrayList<TopInfo> src) {
        pAdapter = new TopAdapter(src, getContext());
        pRecyclerView.setAdapter(pAdapter);
    }
}