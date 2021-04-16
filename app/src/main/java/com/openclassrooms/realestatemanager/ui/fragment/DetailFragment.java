package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentDetailBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.ui.adapter.DetailAdapter;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.List;


public class DetailFragment extends Fragment {

    // variables
    private FragmentDetailBinding binding;
    private long estate_id;
    private List<Photo> photoList;
    private DetailAdapter adapter;
    private Activity activity;
    private EstateViewModel viewModel;

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            estate_id = DetailFragmentArgs.fromBundle(getArguments()).getEstateID();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        Toolbar toolbar = activity.findViewById(R.id.main_toolbar);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            toolbar.setTitle("Details");
        
        configureViewModel();
        configureRecyclerView();
        getEstateData();
        
        // TODO: Map ImageView
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_detail_edit){
            navigateToAddOrEditFragment(estate_id);
        } else{
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void navigateToAddOrEditFragment(long estate_id) {
        // TODO: navigate to editFragment
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
    }

    private void configureRecyclerView() {
        //
    }

    private void getEstateData() {

    }

    private void updateUI(Estate estate) {
        binding.detailDescriptionTextView.setText(estate.getDescription());
        binding.detailSurfaceTextView.setText(String.valueOf(estate.getSurface()));
        binding.detailNumberBedroomTextView.setText(String.valueOf(estate.getNumber_rooms()));
        String address = estate.getAddress() + ", " + estate.getCity() + " " + estate.getZipCode();
        binding.detailAddressTextView.setText(address);
        binding.detailInterestTextView.setText(estate.getPoints_interest());
    }

}