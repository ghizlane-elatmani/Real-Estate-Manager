package com.openclassrooms.realestatemanager.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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
import com.openclassrooms.realestatemanager.utils.DateUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
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
import java.util.TimeZone;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static com.openclassrooms.realestatemanager.App.CHANNEL_1_ID;
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
    private String city;
    private double lat;
    private double lng;
    private String points_interest;
    private boolean isSold;
    private Date entry_date;
    private Date date_sale;
    private String agent_name;
    private int number_picture;

    private List<Photo> photoList;
    private String currentPhotoPath;
    private int estateId;
    private AddPhotoAdapter adapter;

    private Activity activity;
    private EstateViewModel viewModel;

    private NotificationManagerCompat notificationManager;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        onClick();

        // toolbar
        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        if (estateId == 0)
            toolbar.setTitle(getString(R.string.add_new_estate));
        else
            toolbar.setTitle(getString(R.string.edit_estate));

        // init view model, recyclerView ect...
        configureViewModel();
        configureRecyclerView();

        notificationManager = NotificationManagerCompat.from(getContext());

        if (estateId != 0)
            fetchDetailsAndPicturesAccordingToRealEstateId(estateId);
    }


    // configure ViewModel
    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
    }


    // configure observers to fetch the estate' data
    private void fetchDetailsAndPicturesAccordingToRealEstateId(int estate_id) {
        viewModel.getEstate(estate_id).observe(getViewLifecycleOwner(), new Observer<Estate>() {
            @Override
            public void onChanged(@Nullable Estate estate) {
                updateUiWithEstateData(estate);
            }
        });

        viewModel.getPhotos(estate_id).observe(getViewLifecycleOwner(), new Observer<List<Photo>>() {
            @Override
            public void onChanged(@Nullable List<Photo> photos) {
                updateRecyclerViewWithEstatePhotos(photos);
            }
        });
    }

    // Sets the LiveData values in estate and binds estate's attributes into the dedicated views.
    private void updateUiWithEstateData(Estate estate1) {
        estate = estate1;

        if (estate.getType() != null) {
            binding.addTypeTextInputEditText.setText(estate.getType());
            type = binding.addTypeTextInputEditText.getText().toString();
        }

        if (!estate.getDescription().equals("")) {
            binding.addDescriptionTextInputEditText.setText(estate.getDescription());
            description = binding.addDescriptionTextInputEditText.getText().toString();
        }

        if (estate.getPrice() != 0) {
            binding.addPriceTextInputEditText.setText(estate.getType());
            price = Integer.parseInt(binding.addPriceTextInputEditText.getText().toString());
        }

        if (estate.getSurface() != 0) {
            binding.addSurfaceTextInputEditText.setText(estate.getSurface());
            surface = Integer.parseInt(binding.addSurfaceTextInputEditText.getText().toString());
        }

        if (estate.getNumber_rooms() != 0) {
            binding.addNumberRoomsTextInputEditText.setText(estate.getNumber_rooms());
            number_rooms = Integer.parseInt(binding.addNumberRoomsTextInputEditText.getText().toString());
        }

        if (estate.getZipCode() != 0) {
            binding.addZipCodeTextInputEditText.setText(estate.getZipCode());
            zipCode = Integer.parseInt(binding.addZipCodeTextInputEditText.getText().toString());
        }

        if (estate.getAddress() != null) {
            binding.addAddressTextInputEditText.setText(estate.getAddress());
            address = binding.addAddressTextInputEditText.getText().toString();
        }

        if (estate.getCity() != null) {
            binding.addCityTextInputEditText.setText(estate.getCity());
            city = binding.addCityTextInputEditText.getText().toString();
        }

        if (estate.getAgent_name() != null || !estate.getAgent_name().equals("")) {
            binding.addAgentTextInputEditText.setText(estate.getAgent_name());
            agent_name = binding.addAgentTextInputEditText.getText().toString();
        }

        if (estate.getEntry_date() != null)
            binding.addEntryDateTextInputEditText.setText(DateUtils.convertDateToString(estate.getEntry_date(), activity));

        if (estate.getDate_sale() != null)
            binding.addSoldDateTextInputEditText.setText(DateUtils.convertDateToString(estate.getDate_sale(), activity));

        if (estate.getPoints_interest() != null || !estate.getPoints_interest().equals("")) {
            if (estate.getPoints_interest().contains("School"))
                binding.addPointInterestSchoolChip.setChecked(true);

            if (estate.getPoints_interest().contains("Shops"))
                binding.addPointInterestShopChip.setChecked(true);

            if (estate.getPoints_interest().contains("Park"))
                binding.addPointInterestParkChip.setChecked(true);

            if (estate.getPoints_interest().contains("Transportation"))
                binding.addPointInterestTransportationChip.setChecked(true);
        }

    }

    // sets the LiveData values in photoList and notifies the RecyclerView adapter that the data changed
    private void updateRecyclerViewWithEstatePhotos(List<Photo> photos) {
        photoList.clear();
        for (Photo photo : photos) {
            if (!photoList.contains(photo))
                photoList.add(photo);
        }
        binding.addPhotoRecyclerView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }


    // Configures the RecyclerView's adapter and sets the expected behavior when user click on an RecyclerView's item
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
                                // if this fragment is used to add a property in database, it removes le selected picture of the list
                                if (estateId != 0) {
                                    // if the fragment is used to edit a property, it deletes the picture in database too.
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

    // fetch the user's input, verifies if the input is not empty and sets the values into the corresponding fields.
    private void getTheUserInput() {
        // estate' type
        if (!TextUtils.isEmpty(binding.addTypeTextInputEditText.getText()))
            type = binding.addTypeTextInputEditText.getText().toString();

        // estate' price
        if (!TextUtils.isEmpty(binding.addPriceTextInputEditText.getText()))
            price = Integer.parseInt(binding.addPriceTextInputEditText.getText().toString());

        // estate' surface
        if (!TextUtils.isEmpty(binding.addSurfaceTextInputEditText.getText()))
            surface = Integer.parseInt(binding.addSurfaceTextInputEditText.getText().toString());

        // estate' number of rooms
        if (!TextUtils.isEmpty(binding.addNumberRoomsTextInputEditText.getText()))
            number_rooms = Integer.parseInt(binding.addNumberRoomsTextInputEditText.getText().toString());

        // estate' description
        if (!TextUtils.isEmpty(binding.addDescriptionTextInputEditText.getText()))
            description = binding.addDescriptionTextInputEditText.getText().toString();

        // estate' address
        if (!TextUtils.isEmpty(binding.addAddressTextInputEditText.getText()))
            address = binding.addAddressTextInputEditText.getText().toString();

        // estate' zip code
        if (!TextUtils.isEmpty(binding.addZipCodeTextInputEditText.getText()))
            zipCode = Integer.parseInt(binding.addZipCodeTextInputEditText.getText().toString());

        // estate' city
        if (!TextUtils.isEmpty(binding.addCityTextInputEditText.getText())) {
            city = binding.addCityTextInputEditText.getText().toString();
        }

        // estate' points of interest
        points_interest = Utils.getEstatePointInterest(binding.addPointInterestSchoolChip, binding.addPointInterestShopChip, binding.addPointInterestParkChip, binding.addPointInterestTransportationChip);

        if (!TextUtils.isEmpty(binding.addEntryDateTextInputEditText.getText()))
            entry_date = DateUtils.convertStringToDate(binding.addEntryDateTextInputEditText.getText().toString());


        if (!TextUtils.isEmpty(binding.addSoldDateTextInputEditText.getText())) {
            date_sale = DateUtils.convertStringToDate(binding.addSoldDateTextInputEditText.getText().toString());
            // if a sale date is filled then the property is considered sold
            isSold = true;
        } else
            isSold = false;

        // If the set of fields relative to the address are filled then it calls the method getLocation.
        if (address != null && zipCode != 0 && city != null) {
            if (Utils.isNetworkAvailable(activity)) {
                getLocation(address + " " + zipCode + " " + city);
            }
        }
    }

    /**
     * Updates estate that is the current Estate with the fetched user's input.
     */
    private void updateEstate (String type,int price, int surface, int number_rooms, String
            description, String address,
                               int zipCode, String city,double lat, double lng, String points_interest,boolean isSold,
                               Date entry_date, Date date_sale, String agent_name,int number_picture){
        estate.setType(type);
        estate.setPrice(price);
        estate.setSurface(surface);
        estate.setNumber_rooms(number_rooms);
        estate.setDescription(description);
        estate.setAddress(address);
        estate.setZipCode(zipCode);
        estate.setLat(lat);
        estate.setLng(lng);
        estate.setCity(city);
        estate.setPoints_interest(points_interest);
        estate.setSold(isSold);
        estate.setEntry_date(entry_date);
        estate.setDate_sale(date_sale);
        estate.setAgent_name(agent_name);
        estate.setNumber_picture(number_picture);
    }

    public void onClick () {
        binding.addEntryDateTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(binding.addEntryDateTextInputEditText);
            }
        });

        binding.addSoldDateTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(binding.addSoldDateTextInputEditText);
            }
        });

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estateId == 0) {
                    if (verifyAddressInputForGeoCoding()) {
                        createNewRealEstateFromInputValues();
                    }
                } else {
                    if (verifyAddressInputForGeoCoding()) {
                        updateRealEstateWithNewValues();
                    }
                }
            }
        });

        binding.addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPermissionsExternalStorageAndCamera();
            }
        });
    }

    /**
     * Creates the selected pictures in database with the current estate's Id for attribute.
     */
    private void savePictureInDb ( long estate_id){
        for (Photo photo : photoList) {
            photo.setEstateId(estate_id);
            viewModel.createPictures(photo);
        }
    }

    /**
     * Uses Geocoder object to fetch the latitude and the longitude from an address.
     */
    private void getLocation (String address){
        Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            for (Address address1 : addresses) {
                lat = address1.getLatitude();
                lng = address1.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Updates a property in database with the user's input
    private void updateRealEstateWithNewValues () {
        getTheUserInput();
        number_picture = photoList.size();
        updateEstate(type, price, surface, number_rooms, description, address, zipCode, city, lat, lng,
                points_interest, isSold, entry_date, date_sale, agent_name, number_picture);

        savePictureInDb(estateId);

        int nbRows = viewModel.updateEstate(estate);
        if (nbRows == 1) {
            Snackbar.make(activity.findViewById(R.id.main_nav_host_fragment),
                    getString(R.string.update_success), Snackbar.LENGTH_LONG).show();
            Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment).popBackStack();
        } else
            Snackbar.make(activity.findViewById(R.id.main_nav_host_fragment),
                    getString(R.string.update_failed), Snackbar.LENGTH_LONG).show();

    }

    //Creates a property in database with the user's input.
    private void createNewRealEstateFromInputValues () {
        getTheUserInput();
        number_picture = photoList.size();

        long rowId = viewModel.createEstate(new Estate(type, price, surface, number_rooms, description, address, zipCode, city, lat, lng,
                points_interest, isSold, entry_date, date_sale, agent_name, number_picture));

        if (rowId != -1) {
            savePictureInDb(rowId);
            sendOnChannel();
            Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment).popBackStack();
        } else
            Snackbar.make(activity.findViewById(R.id.main_nav_host_fragment), getString(R.string.save_failed), Snackbar.LENGTH_LONG).show();

    }

    private void sendOnChannel() {
        String title = "Real Estate Manager";
        String message = "The real estate has been successfully added to the list";
        Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_circle_notifications)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
    }


    //Displays a DatePicker to select a date and sets the selected value in the corresponding view.
    private void displayDatePickerAndUpdateUi ( final View view){
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        final long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select a Date");
        builder.setSelection(today);
        final MaterialDatePicker materialDatePicker = builder.build();

        binding.addEntryDateTextInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                int id = view.getId();
                if (id == R.id.add_entry_date_text_input_edit_text) {
                    String dateString = DateFormat.format("dd/MM/yyyy", new Date(selection)).toString();
                    binding.addEntryDateTextInputEditText.setText(dateString);
                } else if (id == R.id.add_sold_date_text_input_edit_text) {
                    String dateString = DateFormat.format("dd/MM/yyyy", new Date(selection)).toString();
                    binding.addEntryDateTextInputEditText.setText(dateString);
                }
            }
        });
    }


    private boolean verifyAddressInputForGeoCoding () {
        if (TextUtils.isEmpty(binding.addAddressTextInputEditText.getText())
                || (TextUtils.isEmpty(binding.addZipCodeTextInputEditText.getText())
                || TextUtils.isEmpty(binding.addCityTextInputEditText.getText()))) {

            if (TextUtils.isEmpty(binding.addAddressTextInputEditText.getText()))
                binding.addAddressTextInputEditText.setError(getString(R.string.please_address));

            if (TextUtils.isEmpty(binding.addZipCodeTextInputEditText.getText()))
                binding.addZipCodeTextInputEditText.setError(getString(R.string.please_zip_code));

            if (TextUtils.isEmpty(binding.addCityTextInputEditText.getText()))
                binding.addCityTextInputEditText.setError(getString(R.string.please_city));
            return false;

        } else
            return true;
    }


    @AfterPermissionGranted(READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA)
    private void getPermissionsExternalStorageAndCamera () {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (!EasyPermissions.hasPermissions(activity, perms)) {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_storage),
                    READ_AND_WRITE_EXTERNAL_STORAGE_AND_CAMERA, perms);
        } else {
            showDialogToFetchPictures();
            Log.i("getStorageAndCamera()", "showDialogToFetchPictures");
        }
    }

    // Displays a AlertDialog to propose to the user to add to the choice either a photo from camera or gallery
    private void showDialogToFetchPictures () {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_fetch_photo,
                (ConstraintLayout) getActivity().findViewById(R.id.validationLayoutDialog)
        );
        builder.setView(view);
        ((TextView) view.findViewById(R.id.validation_title_text)).setText(getResources().getString(R.string.title_dialog_picture));
        ((TextView) view.findViewById(R.id.validation_text_input_layout)).setText(getResources().getString(R.string.dialog_text));
        ((Button) view.findViewById(R.id.validation_cancel_button)).setText(getResources().getString(R.string.gallery));
        ((Button) view.findViewById(R.id.validation_save_button)).setText(getResources().getString(R.string.camera));
        ((ImageView) view.findViewById(R.id.validation_image_icon)).setImageResource(R.drawable.ic_photo);

        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.validation_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                pickPhotoFromGallery();
            }
        });

        view.findViewById(R.id.validation_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                openCameraForPhoto();
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

    //Creates the file to save the taken photo by user.
    private File createPhotoFile () throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    // Creates an intent to allow the user to take a photo with his device's camera and with the right camera application.
    private void openCameraForPhoto () {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(activity.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
            }
        }
    }

    //Creates an intent to allow the user to pick a photo in memory with the right application.
    private void pickPhotoFromGallery () {
        Uri collection;
        collection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Intent intentGallery = new Intent(Intent.ACTION_PICK);
        intentGallery.setDataAndType(collection, "image/*");
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intentGallery, IMAGE_PICK_CODE);
    }


    // Displays a MaterialAlertDialog to ask the user to enter a caption for the chosen photo.
    private void savePictureCaption ( final String uri){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_photo_validation,
                (ConstraintLayout) getActivity().findViewById(R.id.validationLayoutDialog)
        );
        builder.setView(view);

        Glide.with(requireContext()).load(uri).into((ImageView) view.findViewById(R.id.dialog_picture_preview_img));
        final TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.validation_text_input_edit_text);
        final TextInputLayout textInputLayout = view.findViewById(R.id.validation_text_input_layout);
        final AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.validation_cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.validation_save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText.getText() != null && editText.getText().length() == 0) {
                    textInputLayout.setError(AddOrEditFragment.this.getString(R.string.please_caption));
                } else {
                    textInputLayout.setError(null);
                    photoList.add(new Photo(Uri.parse(uri), editText.getText().toString()));
                    binding.addPhotoRecyclerView.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }


    @Override
    public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
                                             @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            savePictureCaption(currentPhotoPath);
        } else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null) {
            Uri uri = data.getData();
            String path = getRealPathFromURI(uri);
            savePictureCaption(path);
        }
    }


    private String getRealPathFromURI (Uri contentUri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = Objects.requireNonNull(cursor).getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        } else {
            return null;
        }
    }


    @Override
    public void onDestroyView () {
        // sets adapters and listeners to null to avoid memory leaks.
        binding.addPhotoRecyclerView.setAdapter(null);
        adapter.setOnClickListener(null);
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onSaveInstanceState (@NonNull Bundle outState){
        outState.putString("photoFile", currentPhotoPath);
        super.onSaveInstanceState(outState);
    }

}