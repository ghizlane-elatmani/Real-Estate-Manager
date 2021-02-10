package com.openclassrooms.realestatemanager.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.Estate;

import java.util.List;

public class EstateAdapter extends RecyclerView.Adapter<EstateAdapter.EstateViewHolder> {


    private List<Estate> estateList;

    public EstateAdapter(List<Estate> estateList){
        this.estateList = estateList;
    }

    @NonNull
    @Override
    public EstateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.estate_item, parent, false);
        return new EstateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EstateViewHolder holder, int position) {
        holder.updateWithEstateDetails(this.estateList.get(position));
    }

    @Override
    public int getItemCount() {
        return estateList.size();
    }

    public static class EstateViewHolder extends RecyclerView.ViewHolder{

        private ImageView pictureImageView;
        private TextView typeTextView;
        private TextView priceTextView;

        public EstateViewHolder(@NonNull View itemView) {
            super(itemView);

            pictureImageView = itemView.findViewById(R.id.urlPictureImageView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
        }

        public void updateWithEstateDetails(Estate estate) {

            //TODO : Retrieve the list of photos using the property id
            pictureImageView.setImageResource(R.drawable.ic_no_photo);
            typeTextView.setText(estate.getType());
            priceTextView.setText(estate.getPrice());

        }

    }

}
