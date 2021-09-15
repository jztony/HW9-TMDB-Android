package com.example.cs571hw9.ui.watchlist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cs571hw9.CastAdapter;
import com.example.cs571hw9.ItemMoveCallback;
import com.example.cs571hw9.R;
import com.example.cs571hw9.WatchAdapter;
import com.example.cs571hw9.WatchInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class WatchlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private WatchAdapter adapter;

    private TextView textView;

//    private ItemTouchHelper touchHelper;

    // shared preference init
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    private WatchlistViewModel watchlistViewModel;

//    private ArrayList<WatchInfo> removeWatch(String key, String path, ArrayList<WatchInfo> watchData) {
//
//        editor.remove(key);
////        Toast.makeText(this, " was removed from Watchlist", Toast.LENGTH_SHORT).show();
//        editor.commit();
//        watchData.remove(new WatchInfo(key, path));
//        return watchData;
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        // set the pref objects
//        pref = getContext().getSharedPreferences("MyPref", 0);
//        editor = pref.edit();
//
////        // loop through pref to load everything
////        Map<String, ?> keys = pref.getAll();
////
////        // load values into watchlist recycler view ArrayList
//        ArrayList<WatchInfo> watchData = new ArrayList<>();
////        for(Map.Entry<String, ?> entry : keys.entrySet()){
////            watchData.add(new WatchInfo(entry.getKey(), entry.getValue().toString()));
////        }
//
//        // clear storage when error
////        editor.clear();
////        editor.commit();
//
//        if (pref.getString("keys", null) == null) {
//            editor.putString("keys", "");
//            editor.putString("paths", "");
//            editor.putString("names", "");
//            editor.commit();
//        } else {
//            Scanner keyScan = new Scanner(pref.getString("keys", null));
//            Scanner pathScan = new Scanner(pref.getString("paths", null));
//            Scanner nameScan = new Scanner(pref.getString("names", null));
////            nameScan.useDelimiter("^");
//
//            while (keyScan.hasNext()) {
//
//                String temp = keyScan.next();
//                String temp2 = pathScan.next();
//                String temp3 = nameScan.nextLine();
//
//                if (!temp2.substring(0, 4).equals("http")) {
//                    if (temp2.equals("null")) {
//                        temp2 = "https://cinemaone.net/images/movie_placeholder.png";
//                    } else {
//                        temp2 = "https://image.tmdb.org/t/p/w154" + temp2;
//                    }
//                }
//
//                watchData.add(new WatchInfo(temp, temp2, temp3));
//
//            }
//
//        }
//
//        // text box message
//        if (watchData.size() == 0) {
//            textView.setText("Nothing saved to Watchlist");
//        } else {
//            textView.setText("");
//        }
//
//        // use for debug keys sequence
////        textView.setText(pref.getString("keys", null));
//
//        adapter = new WatchAdapter(watchData, getContext());
//
//        ItemTouchHelper.Callback callback =
//                new ItemMoveCallback(adapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(recyclerView);
//
////        Log.d("DECORATION count: ", recyclerView.getItemDecorationCount() + "");
//
//        // DETACH touchHelper
////        touchHelper.attachToRecyclerView(null);
//
//        recyclerView.setAdapter(adapter);
////        recyclerView.setNestedScrollingEnabled(false);
//
//    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        watchlistViewModel =
                new ViewModelProvider(this).get(WatchlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_watchlist, container, false);
        textView = root.findViewById(R.id.text_watchlist);

        // set up recyclerView
        recyclerView = root.findViewById(R.id.watch_view);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
//        adapter = new WatchAdapter(new ArrayList<WatchInfo>(), getContext());
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(new WatchAdapter(new ArrayList<WatchInfo>(), getContext()));

        watchlistViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
//                textView.setText(s);
            }
        });



        // set the pref objects
        pref = getContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();

//        // loop through pref to load everything
//        Map<String, ?> keys = pref.getAll();
//
//        // load values into watchlist recycler view ArrayList
        ArrayList<WatchInfo> watchData = new ArrayList<>();
//        for(Map.Entry<String, ?> entry : keys.entrySet()){
//            watchData.add(new WatchInfo(entry.getKey(), entry.getValue().toString()));
//        }

        // clear storage when error
//        editor.clear();
//        editor.commit();



        if (pref.getString("keys", null) == null) {
            editor.putString("keys", "");
            editor.putString("paths", "");
            editor.putString("names", "");
            editor.commit();
        } else {
            Scanner keyScan = new Scanner(pref.getString("keys", null));
            Scanner pathScan = new Scanner(pref.getString("paths", null));
            Scanner nameScan = new Scanner(pref.getString("names", null));
//            nameScan.useDelimiter("^");

            while (keyScan.hasNext()) {

                String temp = keyScan.next();
                String temp2 = pathScan.next();
                String temp3 = nameScan.nextLine();

                if (!temp2.substring(0, 4).equals("http")) {
                    if (temp2.equals("null")) {
                        temp2 = "https://cinemaone.net/images/movie_placeholder.png";
                    } else {
                        temp2 = "https://image.tmdb.org/t/p/w154" + temp2;
                    }
                }

                watchData.add(new WatchInfo(temp, temp2, temp3));

            }

        }

        // text box message
        if (watchData.size() == 0) {
            textView.setText("Nothing saved to Watchlist");
        } else {
            textView.setText("");
        }

        // use for debug keys sequence
//        textView.setText(pref.getString("keys", null));

        adapter = new WatchAdapter(watchData, getContext());

        ItemTouchHelper.Callback callback =
                new ItemMoveCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        // DETACH touchHelper
//        touchHelper.attachToRecyclerView(null);

        recyclerView.setAdapter(adapter);
//        recyclerView.setNestedScrollingEnabled(false);


        return root;

    }
}