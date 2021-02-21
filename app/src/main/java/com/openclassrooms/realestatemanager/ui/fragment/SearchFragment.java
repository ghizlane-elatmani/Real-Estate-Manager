package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }


    public void getPrice(){
        //TODO
    }

    public void getType(){
        //TODO
    }

    public void getSurface(){
        //TODO
    }

    public void getNumberRoom(){
        //TODO
    }

    public void getLocation(){
        //TODO
    }

    public void getPointInterest(){
        //TODO
    }

    public void getAvailability(){
        //TODO
    }

    public void getAvailabilityDate(){
        //TODO
    }

    public void getNumberPhoto(){
        //TODO
    }

}