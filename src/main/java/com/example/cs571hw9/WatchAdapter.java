package com.example.cs571hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.ViewHolder>
                        implements ItemMoveCallback.ItemTouchHelperContract {

    private Context c;
    private ArrayList<WatchInfo> watchData;
    private AdapterView.OnItemClickListener mItemClickListener;
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView type;
        private final ImageView poster;
        private final ImageView remove;
        private final CardView card;
        private View rowView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            rowView = view;
            type = (TextView) view.findViewById(R.id.watch_type);
            poster = (ImageView) view.findViewById(R.id.watch_poster);
            remove = (ImageView) view.findViewById(R.id.watch_remove);
            card = (CardView) view.findViewById(R.id.card_view);

//            remove.setOnClickListener(this);
//            view.setOnClickListener(this);
//            remove.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (mItemClickListener != null) {
//                        mItemClickListener.onItemClick(view, getAdapterPosition());
//                    }
//                    return false;
//                }
//            });
        }
        public CardView getCard() {
            return card;
        }
        public TextView getType() {
            return type;
        }
        public ImageView getPoster() {
            return poster;
        }
        public ImageView getRemove() {
            return remove;
        }
    }

//    @Override
//    public void onClick(View v) {
//        if (v.equals(remove)) {
//            removeAt(getPosition());
//        } else if (mItemClickListener != null) {
//            mItemClickListener.onItemClick(v, getPosition());
//        }
//    }

    public String[] removeFromString(String input, String keys, String paths, String names) {

        String output = "";
        String output2 = "";
        String output3 = "";
//        Boolean inList = false;
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
            }
        }

        String[] results = {output, output2, output3};
        return results;
    }

    private void removeWatch(String key) {

//        // shared preference init
        SharedPreferences pref = c.getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();

        String keys = pref.getString("keys", null);
        String paths = pref.getString("paths", null);
        String names = pref.getString("names", null);

        String[] results = removeFromString(key, keys, paths, names);

        editor.clear();
        editor.putString("keys", results[0]);
        editor.putString("paths", results[1]);
        editor.putString("names", results[2]);
        editor.commit();
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public WatchAdapter(ArrayList<WatchInfo> dataSet, Context c) {
        watchData = dataSet;
        this.c = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WatchAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.watch_item, viewGroup, false);

        return new WatchAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(WatchAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (watchData.get(position).getKey().substring(0,2).equals("tv")) {
            viewHolder.getType().setText("TV");
        } else {
            viewHolder.getType().setText("Movie");
        }

        Glide.with(viewHolder.getPoster().getContext())
                .load(watchData.get(position).getPoster_path())
                .into(viewHolder.getPoster());

        viewHolder.getCard().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String type = "";
                String id = "";
                if (watchData.get(position).getKey().substring(0, 1).equals("m")) {
                    type = "movie";
                    id = watchData.get(position).getKey().substring(5);
                } else {
                    type = "tv";
                    id = watchData.get(position).getKey().substring(2);
                }

                String[] data = {type, id};
                sendMessage(v, data);
            }
        });

        // set remote button onclick here
        viewHolder.getRemove().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // remove from list and update view
                removeWatch(watchData.get(position).getKey());
                Toast.makeText(c, watchData.get(position).getName() + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                watchData.remove(watchData.get(position));
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,watchData.size());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return watchData.size();
    }

//    public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
//        this.mItemClickListener = mItemClickListener;
//    }
//
//    public void removeAt(int position) {
//        watchData.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, watchData.size());
//    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(watchData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(watchData, i, i - 1);
            }
        }

        updatePref();
//        notifyItemChanged(fromPosition);
        notifyItemMoved(fromPosition, toPosition);


    }

    @Override
    public void onRowSelected(ViewHolder myViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(ViewHolder myViewHolder) {
//        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }

    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(c, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, data);
        c.startActivity(intent);
    }
//
//    public String[] addToString(String input, String input2, String keys, String paths) {
//
//        String output = "";
//        String output2 = "";
//        Boolean inList = false;
//        Scanner scan = new Scanner(keys);
//        Scanner scan2 = new Scanner(paths);
//        while (scan.hasNext()) {
//            String temp = scan.next();
//            String temp2 = scan2.next();
//            if (!temp.equals(input)) {
//                output += (temp + " ");
//                output2 += (temp2 + " ");
//            } else {
//                inList = true;
//            }
//        }
//        if (!inList) {
//            output += (input + " ");
//            output2 += (input2 + " ");
//        }
//
//        String[] results = {output, output2};
//        return results;
//    }
//
//    public void setWatchList(String type, String id, String poster_path) {
//        SharedPreferences pref = c.getSharedPreferences("MyPref", 0);
//        SharedPreferences.Editor editor = pref.edit();
//        String key = type + id;
//
//        String keys = "";
//        String paths = "";
//
//        if (pref.getString("keys", null) == null) {
//            editor.putString("keys", "");
//            editor.putString("paths", "");
//            editor.commit();
//        } else {
//            keys = pref.getString("keys", null);
//            paths = pref.getString("paths", null);
//        }
//
//        String[] results = addToString(key, poster_path, keys, paths);
//
//        editor.clear();
//        editor.putString("keys", results[0]);
//        editor.putString("paths", results[1]);
//        editor.commit();
//    }

    public void updatePref() {

        SharedPreferences pref = this.c.getSharedPreferences("MyPref", 0);;
        SharedPreferences.Editor editor = pref.edit();

        editor.clear();
//        editor.commit();


        String keys = "";
        String paths = "";
        String names = "";


        for (int i = 0; i < watchData.size(); i++) {
//            editor.putString(watchData.get(i).getKey(), watchData.get(i).getPoster_path());
            keys += (watchData.get(i).getKey() + " ");
            paths += (watchData.get(i).getPoster_path() + " ");
            names += (watchData.get(i).getName() + "\n");
        }

        editor.putString("keys", keys);
        editor.putString("paths", paths);
        editor.putString("names", names);
        editor.commit();

    }

}



