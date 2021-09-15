package com.example.cs571hw9;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private ArrayList<CastInfo> castData;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView castName;
        private final ImageView profile;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            castName = (TextView) view.findViewById(R.id.cast_name);
            profile = (ImageView) view.findViewById(R.id.cast_profile);
        }

        public TextView getCastName() {
            return castName;
        }
        public ImageView getProfile() {
            return profile;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public CastAdapter(ArrayList<CastInfo> dataSet) {
        castData = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cast_item, viewGroup, false);

        return new CastAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CastAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getCastName().setText(castData.get(position).getCast_name());
        Glide.with(viewHolder.getProfile().getContext())
                .load(castData.get(position).getProfile_path())
                .into(viewHolder.getProfile());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return castData.size();
    }

}
