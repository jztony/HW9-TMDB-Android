package com.example.cs571hw9;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Scanner;

public class TopAdapter extends RecyclerView.Adapter<TopAdapter.ViewHolder> {

    private ArrayList<TopInfo> topData;
    private Context c;
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    // shared preference init
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final ImageButton more;
        private ConstraintLayout layout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            layout = (ConstraintLayout) view.findViewById(R.id.top_layout);
            more = (ImageButton) view.findViewById(R.id.clickBtn);
            poster = (ImageView) view.findViewById(R.id.top_poster);
        }
        public ConstraintLayout getLayout() {
            return layout;
        }
        public ImageButton getMore() {
            return more;
        }
        public ImageView getPoster() {
            return poster;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public TopAdapter(ArrayList<TopInfo> dataSet, Context c) {
        topData = dataSet;
        this.c = c;

        // set the pref objects
        pref = c.getSharedPreferences("MyPref", 0);
        editor = pref.edit();
    }

    private boolean inWatchlist(String type, String id) {
        SharedPreferences pref = c.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        String key = type + id;

        String keys = "";
        String paths = "";

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
        SharedPreferences pref = c.getSharedPreferences("MyPref", 0);
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
            Toast.makeText(c, name + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        } else {
            setAdd = false;
//            editor.putString(key, poster_path);
            setWatchList(type, id, poster_path, name);
            Toast.makeText(c, name + " was added to Watchlist", Toast.LENGTH_SHORT).show();
        }

//        MainActivity.R.id.watch_view.
//        editor.commit();
        return setAdd;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TopAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.top_item, viewGroup, false);

        return new TopAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TopAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
//        viewHolder.getTopName().setText(topData.get(position).getTop_name());

        viewHolder.getLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String[] data = {topData.get(position).getType(), topData.get(position).getId()};
                sendMessage(v, data);
            }
        });


        viewHolder.getMore().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initializing the popup menu and giving the reference as current context
                PopupMenu popupMenu = new PopupMenu(c, viewHolder.getMore());

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.pop_up_menu, popupMenu.getMenu());

                if (inWatchlist(topData.get(position).getType(), topData.get(position).getId())) {
                    popupMenu.getMenu().findItem(R.id.add_watch).setTitle("Remove from watchlist");
                }

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        Intent browserIntent;
                        String tmdb = "https://www.themoviedb.org/" + topData.get(position).getType() + "/" + topData.get(position).getId();

                        if (menuItem.getItemId() == R.id.add_watch) {
                            setWatch(topData.get(position).getType(), topData.get(position).getId(), topData.get(position).getPath(), topData.get(position).getName());

                            if (inWatchlist(topData.get(position).getType(), topData.get(position).getId())) {
                                menuItem.setTitle("Remove from Watchlist");
                                Toast.makeText(c, topData.get(position).getName() + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                            } else {
                                menuItem.setTitle("Add to Watchlist");
                                Toast.makeText(c, topData.get(position).getName() + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                            }
                        } else if (menuItem.getItemId() == R.id.open_tmdb) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmdb));
                            c.startActivity(browserIntent);
                        } else if (menuItem.getItemId() == R.id.share_tw) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/intent/tweet?text=check%20this%20out&url=" + tmdb));
                            c.startActivity(browserIntent);
                        } else if (menuItem.getItemId() == R.id.share_fb) {
                            browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + tmdb));
                            c.startActivity(browserIntent);
                        }
//                        Toast.makeText(getActivity(), " was added to Watchlist", Toast.LENGTH_SHORT).show();
                        // Toast message on menu item clicked

                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();


            }
        });

        Glide.with(viewHolder.getPoster().getContext())
                .load(topData.get(position).getPath())
                .into(viewHolder.getPoster());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return topData.size();
    }

    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(c, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, data);
        c.startActivity(intent);
    }
}
