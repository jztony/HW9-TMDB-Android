package com.example.cs571hw9;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {

    private ArrayList<RecInfo> recData;
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";
    private Context c;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final CardView card;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            card = (CardView) view.findViewById(R.id.rec_card);
            poster = (ImageView) view.findViewById(R.id.rec_poster);
        }

        public ImageView getPoster() {
            return poster;
        }
        public CardView getCard() {
            return card;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public RecAdapter(ArrayList<RecInfo> dataSet, Context c) {
        recData = dataSet;
        this.c = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rec_item, viewGroup, false);

        return new RecAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        Glide.with(viewHolder.getPoster().getContext())
                .load(recData.get(position).getPoster_path())
                .into(viewHolder.getPoster());

        viewHolder.getCard().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String[] data = {recData.get(position).getType(), recData.get(position).getId()};
                sendMessage(v, data);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recData.size();
    }

    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(c, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_MESSAGE, data);
        c.startActivity(intent);
    }
}
