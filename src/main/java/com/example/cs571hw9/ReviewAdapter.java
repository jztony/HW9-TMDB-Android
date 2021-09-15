package com.example.cs571hw9;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";
    private ArrayList<ReviewInfo> reviewData;
    private String date_final = "";
    private Context c;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView author;
//        private final TextView date;
        private final TextView score;
        private final TextView content;
        private final ConstraintLayout layout;
//        private final TextView reviewContent;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            author = (TextView) view.findViewById(R.id.review_author);
//            date = (TextView) view.findViewById(R.id.review_date);
            score = (TextView) view.findViewById(R.id.review_score);
            content = (TextView) view.findViewById(R.id.review_content);
            layout = (ConstraintLayout) view.findViewById(R.id.review_layout);
//            reviewContent = view.findViewById(R.id.review_content);
        }

        public TextView getAuthor() {
            return author;
        }
//        public TextView getDate() {
//            return date;
//        }
        public TextView getScore() {
            return score;
        }
        public TextView getContent() {
            return content;
        }

        public ConstraintLayout getLayout() {
            return layout;
        }

//        public TextView getReviewContent() {
//            return reviewContent;
//        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */

    public ReviewAdapter(ArrayList<ReviewInfo> dataSet, Context c) {
        reviewData = dataSet;
        this.c = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false);

        return new ReviewAdapter.ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element



//        Instant instant = Instant.parse(date);
//        date = "";
//        String pattern = "yyyy-MM-ddTHH:mm:sssZ";

//        String date = reviewData.get(position).getDate().replaceAll("[TZ]", " ");

        // format date
        String date = reviewData.get(position).getDate().replaceAll("[TZ]", " ");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS ");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, MMM dd yyyy");
        String formattedDate = dateTime.format(myFormatObj);

        viewHolder.getAuthor().setText("by " + reviewData.get(position).getAuthor() + " on " + formattedDate);
        if (!reviewData.get(position).getScore().equals("null")) {
            viewHolder.getScore().setText((Math.round(Double.parseDouble(reviewData.get(position).getScore())) / 2) + "/5");
        } else {
            viewHolder.getScore().setText("0/5");
        }
        viewHolder.getContent().setText(reviewData.get(position).getContent().substring(0, Math.min(200, reviewData.get(position).getContent().length())) + "...");

        viewHolder.getLayout().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String[] data = {reviewData.get(position).getAuthor(), reviewData.get(position).getDate(), reviewData.get(position).getScore(), reviewData.get(position).getContent()};
                sendMessage(v, data);
            }
        });

//        viewHolder.getReviewContent().setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Do something in response to button click
//                String[] data = {reviewData.get(position).getAuthor(), reviewData.get(position).getDate(), reviewData.get(position).getScore(), reviewData.get(position).getContent()};
//                sendMessage(v, data);
//            }
//        });


//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss ");
//            Date date1 = sdf.parse(date);
//            SimpleDateFormat sdf1 = new SimpleDateFormat("E, MMM dd yyyy");
//            date_final = sdf1.format(date1);
//
//            viewHolder.getAuthor().setText("by " + reviewData.get(position).getAuthor() + " on " + date_final);
//            viewHolder.getScore().setText((Math.round(Double.parseDouble(reviewData.get(position).getScore())) / 2) + "/5");
//            viewHolder.getContent().setText(reviewData.get(position).getContent());
//
//        } catch (Exception e) {
//            System.out.println(e);
//        }




    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reviewData.size();
    }


    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(c, ReviewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_MESSAGE, data);
        c.startActivity(intent);
    }
}
