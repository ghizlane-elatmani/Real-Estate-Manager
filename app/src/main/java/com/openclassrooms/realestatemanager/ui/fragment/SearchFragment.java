package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.Calendar;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constant.SEARCH_FRAGMENT;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private int priceMin;
    private int priceMax;
    private String type;
    private int surfaceMin;
    private int surfaceMax;
    private int numberRoom;
    private String city;
    private String pointInterest;
    private String available;
    private long date_min;
    private long date_max;
    private int numberPhoto;
    private EstateViewModel viewModel;


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onClick();
    }

    public void onClick(){
        binding.searchDateMinTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(view);
            }
        });

        binding.searchDateMaxTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(view);
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment.this.getDataInput();
                //searchEstatesAccordingToUserInput();
            }
        });
    }

    private void displayDatePickerAndUpdateUi(final View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                int id = view.getId();
                if (id == R.id.search_date_max_text_input_edit_text) {
                    binding.searchDateMaxTextInputEditText.setText(SearchFragment.this.getString(R.string.hour_format, day1, month1 + 1, year1));
                } else if (id == R.id.search_date_min_text_input_edit_text) {
                    binding.searchDateMinTextInputEditText.setText(SearchFragment.this.getString(R.string.hour_format, day1, month1 + 1, year1));
                }
            }
        }, year, month, day);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Delete Date", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int id = view.getId();
                if (id == R.id.search_date_max_text_input_edit_text) {
                    binding.searchDateMaxTextInputEditText.setText(null);
                } else if (id == R.id.search_date_min_text_input_edit_text) {
                    binding.searchDateMinTextInputEditText.setText(null);
                }
            }
        });
        datePickerDialog.show();
    }

    public void getDataInput(){
        getPrice();
        getType();
        getSurface();
        getNumberRoom();
        getLocation();
        getPointInterest();
        getAvailability();
        getDate();
        getNumberPhoto();
    }


    public void getPrice(){
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            priceMin = Integer.parseInt(binding.searchPriceMinTextInputEditText.getText().toString().trim());
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            priceMax = Integer.parseInt(binding.searchPriceMaxTextInputEditText.getText().toString().trim());
    }

    public void getType(){
        type = "(";
        if (binding.searchTypeDuplexChip.isChecked()) {
            if (!type.equals("("))
                type += ", ";
            type = type + "'duplex'";
        }
        if (binding.searchTypeLoftsChip.isChecked()) {
            if (!type.equals("("))
                type += ", ";
            type += "'loft'";
        }
        if (binding.searchTypePenthouseChip.isChecked()) {
            if (!type.equals("("))
                type += ", ";
            type += "'penthouse'";
        }
        if (binding.searchTypeManorChip.isChecked()) {
            if (!type.equals("("))
                type += ", ";
            type += "'manor'";
        }
        type += ")";
    }

    public void getSurface(){
        if (!TextUtils.isEmpty(binding.searchSurfaceMinTextInputEditText.getText()))
            surfaceMin = Integer.parseInt(binding.searchSurfaceMinTextInputEditText.getText().toString().trim());
        if (!TextUtils.isEmpty(binding.searchSurfaceMinTextInputEditText.getText()))
            surfaceMax = Integer.parseInt(binding.searchSurfaceMinTextInputEditText.getText().toString().trim());
    }

    public void getNumberRoom(){
        int chipId = binding.searchNumberRoomChipGroup.getCheckedChipId();
        if (chipId == R.id.number_chip_1) {
            numberRoom = 1;

        } else if (chipId == R.id.number_chip_2) {
            numberRoom = 2;

        } else if (chipId == R.id.number_chip_3) {
            numberRoom = 3;

        } else if (chipId == R.id.number_chip_4) {
            numberRoom = 4;

        } else if (chipId == R.id.number_chip_5) {
            numberRoom = 5;

        } else if (chipId == R.id.number_chip_6_to_n) {
            numberRoom = 6;

        } else {
            numberRoom = 0;
        }
    }

    public void getLocation(){
        if (!TextUtils.isEmpty(binding.searchCityTextInputEditText.getText()))
            city = binding.searchCityTextInputEditText.getText().toString().trim();
    }

    public void getPointInterest(){
        pointInterest = "(";
        if (binding.searchPointInterestSchoolChip.isChecked()) {
            if (!pointInterest.equals("("))
                pointInterest += ", ";
            pointInterest = pointInterest + "'school'";
        }
        if (binding.searchPointInterestShopChip.isChecked()) {
            if (!pointInterest.equals("("))
                pointInterest += ", ";
            pointInterest += "'shop'";
        }
        if (binding.searchPointInterestParkChip.isChecked()) {
            if (!pointInterest.equals("("))
                pointInterest += ", ";
            pointInterest += "'park'";
        }
        if (binding.searchPointInterestTransportationChip.isChecked()) {
            if (!pointInterest.equals("("))
                pointInterest += ", ";
            pointInterest += "'transportation'";
        }
        type += ")";
    }

    public void getAvailability(){
        available = "(";
        if (binding.searchDateAvailableChip.isChecked()) {
            if (!available.equals("("))
                available += ", ";
            available = available + "'available'";
        }
        if (binding.searchDateSoldChip.isChecked()) {
            if (!available.equals("("))
                available += ", ";
            available = available + "'sold'";
        }
        type += ")";
    }

    public void getDate(){
       // if (!TextUtils.isEmpty(binding.searchDateMinTextInputEditText.getText()))
            // date_min = Converters.dateToTimestamp(Utils.convertStringToDate(binding.searchDateMinTextInputEditText.getText().toString().trim()));
    }

    public void getNumberPhoto(){
        if (!TextUtils.isEmpty(binding.searchPhotoAutocompleteTextView.getText()))
            city = binding.searchCityTextInputEditText.getText().toString().trim();
    }

    public void getEstateThanksToTheRequest(){
        String query = "SELECT * FROM estate WHERE id > 0";

        // --- price ---
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            query += " AND estate.price >= " + priceMin;
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            query += " AND estate.price <= " + priceMax;

        // --- surface ---
        if (!TextUtils.isEmpty(binding.searchSurfaceMinTextInputEditText.getText()))
            query += " AND estate.surface >= " + surfaceMin;
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            query += " AND estate.surface <= " + surfaceMax;

        // --- type ---
        if (type.contains("duplex"))
            query += " AND estate.type LIKE '%duplex%'";
        if (type.contains("loft"))
            query += " AND estate.type LIKE '%loft%'";
        if (type.contains("penthouse"))
            query += " AND estate.type LIKE '%penthouse%'";
        if (type.contains("manor"))
            query += " AND estate.type LIKE '%manor%'";

        // --- number rooms ---
        if (numberRoom != 0)
            query += " AND estate.number_rooms >= " + numberRoom;

        // --- points of interest ---
        if (pointInterest.contains("school"))
            query += " AND estate.points_interest LIKE '%school%'";
        if (pointInterest.contains("shop"))
            query += " AND estate.points_interest LIKE '%shop%'";
        if (pointInterest.contains("park"))
            query += " AND estate.points_interest LIKE '%park%'";
        if (pointInterest.contains("transportation"))
            query += " AND estate.points_interest LIKE '%transportation%'";

        // --- availability ---
        if (available.contains("available"))
            query += " AND estate.status LIKE '%available%'";
        if (available.contains("sold"))
            query += " AND estate.status LIKE '%sold%'";

        // --- date ---
        if (date_min != 0)
            query += " AND estate.entry_date >= " + date_min;
        if (date_max != 0)
            query += " AND estate.entry_date <= " + date_max;

        // --- number photo ---
        if (numberPhoto != 0)
            query += " AND estate.number_picture >= " + numberPhoto;

        query += " ;";

        sendQueryToListFragment(query);

    }

    private void sendQueryToListFragment(String query) {
        viewModel.getAllEstatesAccordingToUserSearch(new SimpleSQLiteQuery(query)).observe(getViewLifecycleOwner(), new Observer<List<Estate>>() {
            @Override
            public void onChanged(@Nullable List<Estate> estates) {
                if (estates.isEmpty()) {
                    //Snackbar.make(getActivity().findViewById(R.id.main_nav_host_fragment), getString(R.string.no_result), Snackbar.LENGTH_SHORT).show();
                } else {
                    //viewModel.addEstateList(estates);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
                    SearchFragmentDirections.ActionSearchFragmentToListFragment action = SearchFragmentDirections.actionSearchFragmentToListFragment();
                    action.setOrigin(SEARCH_FRAGMENT);
                    navController.navigate(action);
                }
            }
        });
    }


}