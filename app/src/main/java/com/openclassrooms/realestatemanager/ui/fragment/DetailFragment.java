package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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
import com.openclassrooms.realestatemanager.ui.adapter.SliderAdapterDetail;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {

    // variables
    private FragmentDetailBinding binding;
    private long estate_id;
    private List<Photo> photoList;
    private SliderAdapterDetail adapter;
    private Activity activity;
    private EstateViewModel viewModel;
    // private GoogleMap googleMap;
    private double lat;
    private double lng;


    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            estate_id = DetailFragmentArgs.fromBundle(getArguments()).getEstateID();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            setHasOptionsMenu(true);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        if (!getResources().getBoolean(R.bool.isTabletLand))
            toolbar.setTitle("Details");
        configureViewModel();
        configureRecyclerView();
        getEstateDetail();
        // binding.fragmentDetailsMapView.onCreate(savedInstanceState);
        // binding.fragmentDetailsMapView.getMapAsync(this);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            openAddFragmentToEditRealEstate(estate_id);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        this.googleMap = googleMap;
//        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
//        this.googleMap.setOnMapClickListener(latLng -> {
//        });
//    }
//
//    private void addPropertyPositionOnMap() {
//        LatLng latLng = new LatLng(lat, lng);
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
//        googleMap.addMarker(new MarkerOptions().position(latLng));
//    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
        viewModel.getSelectedEstateId().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                estate_id = aLong;
                DetailFragment.this.getEstateDetail();
            }
        });
    }


    private void getEstateDetail() {
        viewModel.getEstate(estate_id).observe(getViewLifecycleOwner(), new Observer<Estate>() {
            @Override
            public void onChanged(Estate estate) {
                initDetails(estate);
            }
        });

        viewModel.getPhotos(estate_id).observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                bindPhotoInRecyclerView(photos);
            }
        });
    }


    private void bindPhotoInRecyclerView(List<Photo> photos) {
        photoList.clear();
        photoList.addAll(photos);
        adapter.notifyDataSetChanged();
    }


    private void configureRecyclerView() {
        photoList = new ArrayList<>();
        adapter = new SliderAdapterDetail(getContext(), photoList);

        binding.sliderView.setSliderAdapter(adapter);

        binding.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        binding.sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        binding.sliderView.setIndicatorSelectedColor(Color.WHITE);
        binding.sliderView.setIndicatorUnselectedColor(Color.GRAY);
        binding.sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        binding.sliderView.startAutoCycle();
    }

    private void initDetails(Estate estate) {
        binding.detailDescriptionTextView.setText(estate.getDescription());
        binding.detailSurfaceTextView.setText(estate.getSurface() + "m²");
        binding.detailNumberBedroomTextView.setText(String.valueOf(estate.getNumber_rooms()));
        String address = estate.getAddress() + ", " + estate.getZipCode() + ", " + estate.getCity();
        binding.detailAddressTextView.setText(address);
        binding.detailInterestTextView.setText(estate.getPoints_interest());
        binding.detailTypeTextView.setText(estate.getType());
        String price = String.valueOf(estate.getPrice());
        binding.detailPriceTextView.setText(Utils.formatNumberCurrency(price));

        // TODO :
//        binding.detailStatusTextView.setText(estate.);
//        binding.detailEntryDateTextView.setText(estate.getEntry_date());
//        binding.detailSoldDateTextView.setText(estate.getDate_sale());

        lat = estate.getLat();
        lng = estate.getLng();
        // addEstateOnMap();
    }


    private void openAddFragmentToEditRealEstate(long realEstateId) {
        if (!getResources().getBoolean(R.bool.isTabletLand)) {
            NavController mController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
            //mController.navigate(.actionDetailsFragmentToAddPropertyFragment().setRealEstateId(realEstateId));
        } else {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
            Bundle args = new Bundle();
            args.putLong("estateId", realEstateId);
            navController.navigate(R.id.addOrEditFragment, args);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // googleMap = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        //binding.fragmentDetailsMapView.getMapAsync(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.sliderView.setSliderAdapter(null);
        adapter = null;
        binding = null;
    }

}