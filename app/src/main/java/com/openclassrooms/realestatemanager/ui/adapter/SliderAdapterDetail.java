package com.openclassrooms.realestatemanager.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Photo;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapterDetail extends SliderViewAdapter<SliderAdapterDetail.Holder> {

    private Context context;
    private List<Photo> photoList;

    public SliderAdapterDetail(Context context, List<Photo> photoList) {
        this.context = context;
        this.photoList = photoList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_slider_estate, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        Photo photo = photoList.get(position);
        Glide.with(context).load(photo.getUri().toString()).into(viewHolder.imageView);
        viewHolder.textView.setText(photo.getLabel());
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    public class Holder extends SliderViewAdapter.ViewHolder{

        ImageView imageView;
        TextView textView;

        public Holder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_auto_image_slider);
            textView = itemView.findViewById(R.id.tv_auto_image_slider);
        }
    }

}

