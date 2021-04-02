package com.openclassrooms.realestatemanager.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentAddBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.ui.adapter.AddPhotoAdapter;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.utils.Constant.ESTATE_ID;
import static com.openclassrooms.realestatemanager.utils.Constant.IMAGE_CAPTURE_CODE;
import static com.openclassrooms.realestatemanager.utils.Constant.IMAGE_PICK_CODE;
import static com.openclassrooms.realestatemanager.utils.Constant.READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA;


public class AddOrEditFragment extends Fragment {

    // variables
    private Estate estate;
    private String type;
    private int price;
    private int surface;
    private int number_rooms;
    private String description;
    private String address;
    private int zipCode;
    private String countryCode;
    private double lat;
    private double lng;
    private String city;
    private String points_interest;
    private boolean isSold;
    private Date entry_date;
    private Date date_sale;
    private int agent_id;
    private int number_picture;

    private List<Photo> photoList;
    private String currentPhotoPath;
    private int estateId;
    private AddPhotoAdapter adapter;

    private Activity activity;
    private EstateViewModel viewModel;

    // views, binding
    private FragmentAddBinding binding;

    public AddOrEditFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
            estateId = getArguments().getInt(ESTATE_ID);

        photoList = new ArrayList<>();
        if (savedInstanceState != null) {
            currentPhotoPath = savedInstanceState.getString("photoFile", null);
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        Toolbar toolbar = activity.findViewById(R.id.main_toolbar);

        if (estateId == 0)
            toolbar.setTitle(getString(R.string.add_new_estate));
        else
            toolbar.setTitle(getString(R.string.edit_estate));

        configureViewModel();
        configureRecyclerView();

        // fetch the property's attributes.
        if (estateId != 0)
            retrieveEstateAttribute(estateId);

    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
    }

    //Configures observers to fetch the property's attributes and the property's pictures.
    private void retrieveEstateAttribute(int estate_id) {
        viewModel.getEstate(estate_id).observe(getViewLifecycleOwner(), new Observer<Estate>() {
            @Override
            public void onChanged(@Nullable Estate estate1) {
                updateUiWithEstateInfo(estate1);
            }
        });

        viewModel.getPhotos(estate_id).observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photoList1) {
                updateRecyclerViewPhotoWithEstateInfo(photoList1);
            }
        });
    }

    //Sets the LiveData values in picturesList and notifies the RecyclerView adapter that the data changed
    private void updateRecyclerViewPhotoWithEstateInfo(List<Photo> photos) {
        photoList.clear();
        for (Photo photo : photos) {
            if (!photoList.contains(photo))
                photoList.add(photo);
        }
        binding.addPhotoRecyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    //Sets the LiveData values in mProperty and binds mProperty's attributes into the dedicated views.
    private void updateUiWithEstateInfo(Estate estate1) {
        estate = estate1;
        // Estate' type
        if (estate.getType() != null) {
            if (estate.getType().contains("duplex"))
                binding.addTypeDuplexChip.setChecked(true);
            if (estate.getType().contains("loft"))
                binding.addTypeLoftsChip.setChecked(true);
            if (estate.getType().contains("penthouse"))
                binding.addTypePenthouseChip.setChecked(true);
            if (estate.getType().contains("manor"))
                binding.addTypeManorChip.setChecked(true);
        }

        // Estate' price
        binding.addPriceTextInputEditText.setText(estate.getPrice());
        price = Integer.parseInt(binding.addPriceTextInputEditText.getText().toString());

        // Estate' surface
        binding.addSurfaceInputEditText.setText(estate.getSurface());
        surface = Integer.parseInt(binding.addSurfaceInputEditText.getText().toString());

        // Estate' number of rooms
        binding.addNumberRoomInputEditText.setText(estate.getNumber_rooms());
        number_rooms = Integer.parseInt(binding.addNumberRoomInputEditText.getText().toString());

        // Estate' address

        // Estate' description
        //TODO: layout + code

        // Estate' points of interest
        if (estate.getPoints_interest() != null) {
            if (estate.getPoints_interest().contains("school"))
                binding.addPointInterestSchoolChip.setChecked(true);
            if (estate.getPoints_interest().contains("shop"))
                binding.addPointInterestShopChip.setChecked(true);
            if (estate.getPoints_interest().contains("park"))
                binding.addPointInterestParkChip.setChecked(true);
            if (estate.getPoints_interest().contains("transportation"))
                binding.addPointInterestTransportationChip.setChecked(true);
        }

        // Estate' date --- We convert the dates in database into String values
//        if (estate.getInitialSale() != null)
//            binding.fragmentAddInitialSaleTxt.setText(Utils.convertDateToString(estate.getInitialSale(), activity));
//        if (estate.getFinalSale() != null)
//            binding.fragmentAddFinalSaleTxt.setText(Utils.convertDateToString(estate.getFinalSale(), activity));

        // Estate' agent
        //TODO : estate agent, description, date

    }


    // --- method: configure recyclerView's adapter and setOnClickListener to delete a photo in the rv ---
    private void configureRecyclerView() {
        binding.addPhotoRecyclerView.setVisibility(View.GONE);
        adapter = new AddPhotoAdapter(photoList, 2);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPhotoAdapter.AddPhotoViewHolder holder = (AddPhotoAdapter.AddPhotoViewHolder) view.getTag();
                final int position = holder.getAdapterPosition();
                new MaterialAlertDialogBuilder(activity)
                        .setTitle(AddOrEditFragment.this.getString(R.string.delete_photo))
                        .setMessage(AddOrEditFragment.this.getString(R.string.would_you_delete_picture))
                        .setNegativeButton(AddOrEditFragment.this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(AddOrEditFragment.this.getString(R.string.delete), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if (estateId != 0) {
                                    viewModel.deletePhoto(photoList.get(position));
                                }
                                photoList.remove(position);
                                adapter.notifyDataSetChanged();
                                if (photoList.size() == 0)
                                    binding.addPhotoRecyclerView.setVisibility(View.GONE);
                            }
                        })
                        .show();
            }
        });
        binding.addPhotoRecyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.addPhotoRecyclerView.setAdapter(adapter);
    }

    // --- method: setOnClickListener to button add, addPhoto and date' inputEditText ---
    void setClickListener() {
        // TODO: setOnClickListener for date
        binding.addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // --- request permission ---
            }
        });
    }


}
