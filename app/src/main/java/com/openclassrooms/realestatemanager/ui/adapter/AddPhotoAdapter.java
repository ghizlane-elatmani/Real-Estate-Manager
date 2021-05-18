package com.openclassrooms.realestatemanager.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.databinding.RowAddPhotoBinding;
import com.openclassrooms.realestatemanager.model.Photo;

import java.util.List;

public class AddPhotoAdapter extends RecyclerView.Adapter<AddPhotoAdapter.AddPhotoViewHolder> {

    private final List<Photo> photoList;
    private final int origin;

    private View.OnClickListener mClickListener;

    public AddPhotoAdapter(List<Photo> photoList, int origin) {
        this.photoList = photoList;
        this.origin = origin;
    }

    @NonNull
    @Override
    public AddPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RowAddPhotoBinding binding = RowAddPhotoBinding.inflate(LayoutInflater.from(context),parent,false);
        AddPhotoViewHolder viewHolder = new AddPhotoViewHolder(binding, origin, context);
        binding.addItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                mClickListener.onClick(view1);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddPhotoViewHolder holder, int position) {
        holder.updateUI(photoList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setOnClickListener(View.OnClickListener clickListener) {
        mClickListener = clickListener;
    }


    public static class AddPhotoViewHolder extends RecyclerView.ViewHolder {

        public RowAddPhotoBinding binding;
        Context context;

        public AddPhotoViewHolder(RowAddPhotoBinding binding, int origin, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setTag(this);

            this.binding.addItemDeleteButton.setTag(this);
            if (origin == 1)
                this.binding.addItemDeleteButton.setVisibility(View.GONE);
            this.context = context;
        }


        public void updateUI(Photo photo) {
            Glide.with(context).load(photo.getUri().toString()).into(binding.addItemPhotoImageView);
            binding.addItemTitleTextView.setText(photo.getLabel());
        }


    }

}
