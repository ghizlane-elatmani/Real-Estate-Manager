package com.openclassrooms.realestatemanager.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentMapViewBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.io.IOException;
import java.util.List;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private FragmentMapViewBinding binding;
    private Activity activity;
    private GoogleMap map;
    private Location lastKnownLocation;
    private EstateViewModel viewModel;

    public MapViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMapViewBinding.inflate(inflater, container, false);
        binding.fragmentAgentLocationMapview.onCreate(savedInstanceState);
        binding.fragmentAgentLocationMapview.getMapAsync(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle("Map");
        configureViewModel();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
    }


    private void getAllEstates() {
        viewModel.getAllEstate().observe(getViewLifecycleOwner(), new Observer<List<Estate>>() {
            @Override
            public void onChanged(@Nullable List<Estate> estates) {
                addRealEstatesMarker(estates);
            }
        });
    }


    private void addRealEstatesMarker(List<Estate> estateList) {
        if (map != null) {
            for (Estate estate : estateList) {
                if (estate.getLat() != 0 || estate.getLng() != 0) {
                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(estate.getLat(), estate.getLng()))
                            .title(estate.getAddress())
                            .snippet(String.valueOf(estate.getId())));
                }
            }
        }
    }

    private void passRealEstateIdToFragmentDetails(long id) {
        if (getResources().getBoolean(R.bool.isTabletLand)) {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment2);
            Bundle args = new Bundle();
            args.putLong("estateId",id);
            controller.navigate(R.id.detailsFragment,args);
        } else {
            NavController controller = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
            controller.navigate(MapViewFragmentDirections.actionMapViewFragmentToDetailFragment(id));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
//                Sets a custom layout inflated for InfoWindow view.
                View view = LayoutInflater.from(getContext()).inflate(R.layout.infowindow_item, null);
                TextView title = view.findViewById(R.id.infowindow_item_txt);
                title.setText(marker.getTitle());
                return view;
            }
        });

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                long id = Long.parseLong(marker.getSnippet());
                MapViewFragment.this.passRealEstateIdToFragmentDetails(id);

            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                long id = Long.parseLong(marker.getSnippet());
                if (marker.getTitle().equals(marker.getTag())) {
                    marker.setTag(null);
                    MapViewFragment.this.passRealEstateIdToFragmentDetails(id);
                } else {
                    marker.showInfoWindow();
                    marker.setTag(marker.getTitle());
                }
                return true;
            }
        });
        getLastKnownLocation();
    }

    /**
     * Fetches the user's last known position (Latitude, Longitude) from the Google Play Services
     * and move the map's camera on this position.
     */
    private void getLastKnownLocation() {
        try {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        map.setMyLocationEnabled(true);
                        lastKnownLocation = location;
                        getAllEstates();
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom((
                                new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude())), 12));
                    }
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStart() {
        binding.fragmentAgentLocationMapview.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.i("test", "onresume");
        binding.fragmentAgentLocationMapview.onResume();
        if(lastKnownLocation != null){
            getAllEstates();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        binding.fragmentAgentLocationMapview.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        binding.fragmentAgentLocationMapview.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        binding.fragmentAgentLocationMapview.onLowMemory();
        super.onLowMemory();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {
        if (map != null)
            map.setMyLocationEnabled(false);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        binding.fragmentAgentLocationMapview.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.fragmentAgentLocationMapview.onDestroy();
        binding = null;
    }
}
