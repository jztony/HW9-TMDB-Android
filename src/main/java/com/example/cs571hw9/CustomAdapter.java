package com.example.cs571hw9;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
//    private String[] localDataSet;
    private ArrayList<SearchCard> localData;
    private Context context;
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mediaName;
        private final TextView mediaType;
        private final TextView mediaScore;
//        private final TextView mediaId;
        private final ImageView backdrop;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            card = (CardView)  view.findViewById(R.id.card_view);
            mediaName = (TextView) view.findViewById(R.id.media_name);
            mediaType = (TextView) view.findViewById(R.id.media_type);
            mediaScore = (TextView) view.findViewById(R.id.media_score);
//            mediaId = (TextView) view.findViewById(R.id.media_id);
            backdrop = (ImageView) view.findViewById(R.id.backdrop);
        }

        public CardView getCard() {
            return card;
        }

        public TextView getMediaName() {
            return mediaName;
        }
        public ImageView getBackdrop() {
            return backdrop;
        }

        public TextView getMediaType() {
            return mediaType;
        }

        public TextView getMediaScore() {
            return mediaScore;
        }

//        public TextView getMediaId() {
//            return mediaId;
//        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public CustomAdapter(Context context, ArrayList<SearchCard> dataSet) {
        this.context = context;
        localData = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getMediaName().setText(localData.get(position).getMedia_name().toUpperCase());
        viewHolder.getMediaType().setText(localData.get(position).getMedia_type().toUpperCase() + " (" + localData.get(position).getMedia_year() + ")");
        Double score = (int)(Double.parseDouble(localData.get(position).getMedia_score()) * 10 / 2) / 10.0;
//        String score_str = String.format("%.1f", score + "");
        viewHolder.getMediaScore().setText(score + "");
//        viewHolder.getMediaId().setText(localData.get(position).getMedia_id());
        Glide.with(viewHolder.getBackdrop().getContext())
                .load(localData.get(position).getBackdrop_path())
                .into(viewHolder.getBackdrop());

        viewHolder.getCard().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String[] data = {localData.get(position).getMedia_type(), localData.get(position).getMedia_id()};
                Log.d("DATA PRINT: " + data[0], data[1]);
                sendMessage(v, data);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localData.size();
    }

    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, data);
        context.startActivity(intent);
    }

}
