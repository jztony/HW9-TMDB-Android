package com.example.cs571hw9.ui.search;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cs571hw9.CustomAdapter;
import com.example.cs571hw9.R;
import com.example.cs571hw9.SearchCard;
import com.example.cs571hw9.SliderAdapter;
import com.example.cs571hw9.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

import kotlin.random.Random;

public class SearchFragment extends Fragment {

    // variables to be used by requests and views
    private SearchViewModel searchViewModel;
//    private JSONArray result = new JSONArray();
//    private String[] listItems = {"1st", "2nd", "3rd"};
    private ArrayList<SearchCard> initData = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter adapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        final TextView textView = root.findViewById(R.id.text_search);

        textView.setText("");

//        Toolbar toolbar = root.findViewById(R.id.toolbar);
//        toolbar.setDispla

        // populate the data Arraylist
        // empty initial screen
//        initData.add(new SearchCard("first movie"));
//        initData.add(new SearchCard("second movie"));
//        initData.add(new SearchCard("third movie"));

        final SearchView search = root.findViewById(R.id.search_bar);

        // initialize recyclerView variables
        recyclerView = root.findViewById(R.id.search_result);

//        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CustomAdapter(getActivity(), initData);

//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        final String url = "https://cs571upup-be.wl.r.appspot.com";

        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        search.requestFocus();
//        search.setQuery("test",true);
        search.setFocusable(true);
        search.setIconified(false);
        search.requestFocusFromTouch();

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                search(newText);

                if (!newText.equals("")) { // don't search if the search query is empty

                    String get_url = "https://cs571hw9-be.wl.r.appspot.com" + "/search?search=" + newText;
//                "https://cs571upup-be.wl.r.appspot.com/search?action=opensearch&format=json&origin=*&search=ha"

                    // Request a string response from the provided URL.
                    JsonArrayRequest searchRequest = new JsonArrayRequest
                            (Request.Method.GET, get_url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    // Display the first 500 characters of the response string.
//                                    result = response;

                                    // Recycler view adapter

                                    //debug method
//                                textView.setText(result.toString());

                                    ArrayList<SearchCard> searchResults = new ArrayList<>();

                                    try {

                                        for (int i = 0; i < Math.min(20, response.length()); i++) {
                                            if (!response.getJSONObject(i).getString("media_type").equals("person")) {

                                                String type = response.getJSONObject(i).getString("media_type");
                                                String name = "";
                                                String year = "";
                                                if (type.equals("movie")) {
                                                    name = response.getJSONObject(i).getString("title");
                                                    if (!response.getJSONObject(i).optString("release_date").equals("")) {
                                                        year = response.getJSONObject(i).getString("release_date").substring(0, 4);
                                                    }

                                                } else {
                                                    name = response.getJSONObject(i).getString("name");
                                                    if (!response.getJSONObject(i).optString("first_air_date").equals("")) {
                                                        year = response.getJSONObject(i).getString("first_air_date").substring(0, 4);
                                                    }
                                                }
                                                String path = response.getJSONObject(i).getString("backdrop_path");
                                                if (path.equals("null")) {
                                                    path = "https://bytes.usc.edu/cs571/s21_JSwasm00/hw/HW6/imgs/movie-placeholder.jpg";
                                                } else {
                                                    path = "https://image.tmdb.org/t/p/w780" + path;
                                                }
                                                String id = response.getJSONObject(i).getString("id");
                                                String score = response.getJSONObject(i).getString("vote_average");

                                                searchResults.add(new SearchCard(name, path, type, id, year, score));
                                            }
                                        }

                                        adapter = new CustomAdapter(getActivity(), searchResults);
                                        recyclerView.setAdapter(adapter);

                                        if (searchResults.size() == 0) {
                                            textView.setText("No result found");
                                        }

//                                    searchResults.add(new SearchCard(result.getJSONArray(1).getJSONObject(0).getString("name")));
//                                    searchResults.add(new SearchCard(result.getJSONArray(1).getJSONObject(1).getString("name")));
//                                    searchResults.add(new SearchCard(result.getJSONArray(1).getJSONObject(2).getString("name")));

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

                    queue.add(searchRequest);

                } else { // search query is empty
                    adapter = new CustomAdapter(getActivity(), new ArrayList<SearchCard>());
                    recyclerView.setAdapter(adapter);
                    textView.setText("No result found");
                }


                return false;
            }
        });

        searchViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

// Instantiate the RequestQueue.






        return root;
    }
}