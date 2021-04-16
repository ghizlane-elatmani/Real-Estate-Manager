package com.openclassrooms.realestatemanager.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.RowEstateBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class EstateAdapter extends RecyclerView.Adapter<EstateAdapter.EstateViewHolder> {

    private Context context;
    private List<Estate> estateList;
    private List<Photo> photoList;

    public EstateAdapter(Context context, List<Estate> estateList, List<Photo> photoList){
        this.context = context;
        this.estateList = estateList;
        this.photoList = photoList;
    }

    @NonNull
    @Override
    public EstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RowEstateBinding binding = RowEstateBinding.inflate(LayoutInflater.from(context),parent,false);
        return new EstateViewHolder(binding, context);
    }

    @Override
    public void onBindViewHolder(@NonNull EstateViewHolder holder, final int position) {
        holder.updateUI(estateList.get(position), photoList);
    }

    @Override
    public int getItemCount() {
        return estateList.size();
    }


    public static class EstateViewHolder extends RecyclerView.ViewHolder{

        public RowEstateBinding binding;
        private Context context;

        public EstateViewHolder(RowEstateBinding binding, Context context) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setTag(this);

            this.context = context;
        }

        public void updateUI(Estate estate, List<Photo> photoList) {
            if(photoList.size() > 0) {
                for (Photo photo : photoList) {
                    if (photo != null) {
                        if (photo.getEstateId() == estate.getId()) {
                            Glide.with(context).load(photo.getUri().toString()).into(binding.urlPictureImageView);
                        }
                    }
                }
            } else {
                binding.urlPictureImageView.setImageResource(R.drawable.ic_no_photo);
            }

            String type = estate.getType();
            String type_upper = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
            binding.typeTextView.setText(type_upper);

            String number = String.valueOf(estate.getPrice());
            binding.priceTextView.setText("$ " + Utils.formatNumberCurrency(number));
        }

    }

}
