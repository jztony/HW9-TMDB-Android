package com.example.cs571hw9;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {
    // list for storing urls of images.
    private final List<SliderData> mSliderItems;
    private Context context;
    public static final String EXTRA_MESSAGE = "com.example.cs571hw9.MESSAGE";

    // Constructor
    public SliderAdapter(Context context, ArrayList<SliderData> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.context = context;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, parent, false);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final SliderData sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .fitCenter()
                .transform(new jp.wasabeef.glide.transformations.BlurTransformation(25, 3))
                .into(viewHolder.imageViewBackground);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageOnTop);
//        viewHolder.type.setText(sliderItem.getImgType());
//        viewHolder.id.setText(sliderItem.getImgId());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                String[] data = {sliderItem.getImgType(), sliderItem.getImgId()};
                sendMessage(v, data);
            }
        });
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;
        ImageView imageOnTop;
//        TextView type;
//        TextView id;
        RelativeLayout layout;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.myimage);
            imageOnTop = itemView.findViewById(R.id.img_top);
//            type = itemView.findViewById(R.id.media_type);
//            id = itemView.findViewById(R.id.media_id);
            layout = itemView.findViewById(R.id.slider_layout);
            this.itemView = itemView;
        }
    }

    public void sendMessage(View view, String[] data) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra(EXTRA_MESSAGE, data);
        context.startActivity(intent);
    }
}
