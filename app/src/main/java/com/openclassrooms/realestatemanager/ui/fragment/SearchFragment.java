package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.database.DataConverters;
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.DateUtils;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.Calendar;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constant.SEARCH_FRAGMENT;


public class SearchFragment extends Fragment {

    // --- Attribute ---
    private FragmentSearchBinding binding;
    private Activity activity;
    private int priceMin;
    private int priceMax;
    private String type;
    private int surfaceMin;
    private int surfaceMax;
    private String city;
    private String pointInterest;
    private int isSold;
    private long date_min;
    private long date_max;
    private long sold_min;
    private long sold_max;
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
        activity = getActivity();
        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle("Search");
        onClick();
        configureViewModel();
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
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

        binding.searchSoldDateMinTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(view);
            }
        });

        binding.searchSolddateMaxTextInputEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDatePickerAndUpdateUi(view);
            }
        });

        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment.this.getDataInput();
                getEstateQuery();
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
                }else if (id == R.id.search_sold_date_min_text_input_edit_text) {
                    binding.searchSoldDateMinTextInputEditText.setText(SearchFragment.this.getString(R.string.hour_format, day1, month1 + 1, year1));

                } else if (id == R.id.search__solddate_max_text_input_edit_text) {
                    binding.searchSolddateMaxTextInputEditText.setText(SearchFragment.this.getString(R.string.hour_format, day1, month1 + 1, year1));
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

                } else if (id == R.id.search_sold_date_min_text_input_edit_text) {
                    binding.searchSoldDateMinTextInputEditText.setText(null);

                } else if (id == R.id.search__solddate_max_text_input_edit_text) {
                    binding.searchSolddateMaxTextInputEditText.setText(null);
                }
            }
        });
        datePickerDialog.show();
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    // --- When the user click on search button, this method will get all the user'input ---
    public void getDataInput(){
        getType();
        getPrice();
        getSurface();
        getCityLocation();
        getPointInterest();
        getAvailability();
        getDate();
        getPhoto();
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


    public void getPrice(){
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            priceMin = Integer.parseInt(binding.searchPriceMinTextInputEditText.getText().toString().trim());
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            priceMax = Integer.parseInt(binding.searchPriceMaxTextInputEditText.getText().toString().trim());
    }

    public void getSurface(){
        if (!TextUtils.isEmpty(binding.searchSurfaceMinTextInputEditText.getText()))
            surfaceMin = Integer.parseInt(binding.searchSurfaceMinTextInputEditText.getText().toString().trim());
        if (!TextUtils.isEmpty(binding.searchSurfaceMaxTextInputEditText.getText()))
            surfaceMax = Integer.parseInt(binding.searchSurfaceMaxTextInputEditText.getText().toString().trim());
    }

    public void getCityLocation(){
        if (!TextUtils.isEmpty(binding.searchCityTextInputEditText.getText()))
            city = binding.searchCityTextInputEditText.getText().toString().trim();
    }

    public void getPointInterest(){
        pointInterest = Utils.getEstatePointInterest(binding.searchPointInterestSchoolChip,
                binding.searchPointInterestShopChip, binding.searchPointInterestParkChip,
                binding.searchPointInterestTransportationChip);
    }

    public void getAvailability(){
        int chipId = binding.searchIsSoldChipGroup.getCheckedChipId();

        if (chipId == R.id.search_date_available_chip) {
            isSold = 0;

        } else if (chipId == R.id.search_date_sold_chip) {
            isSold = 1;

        } else {
            isSold = 123;
        }
    }

    public void getDate(){
        if (!TextUtils.isEmpty(binding.searchDateMinTextInputEditText.getText()))
            date_min = DataConverters.dateToTimestamp(DateUtils.convertStringToDate(binding.searchDateMinTextInputEditText.getText().toString().trim()));

        if (!TextUtils.isEmpty(binding.searchDateMaxTextInputEditText.getText()))
            date_max = DataConverters.dateToTimestamp(DateUtils.convertStringToDate(binding.searchDateMaxTextInputEditText.getText().toString().trim()));

        if (!TextUtils.isEmpty(binding.searchSoldDateMinTextInputEditText.getText()))
            sold_min = DataConverters.dateToTimestamp(DateUtils.convertStringToDate(binding.searchSoldDateMinTextInputEditText.getText().toString().trim()));

        if (!TextUtils.isEmpty(binding.searchSolddateMaxTextInputEditText.getText()))
            sold_max = DataConverters.dateToTimestamp(DateUtils.convertStringToDate(binding.searchSolddateMaxTextInputEditText.getText().toString().trim()));
    }

    public void getPhoto(){
        if (!TextUtils.isEmpty(binding.searchPhotoAutocompleteTextView.getText()))
            numberPhoto = Integer.parseInt(binding.searchPhotoAutocompleteTextView.getText().toString().trim());
    }

    public void getEstateQuery(){
        String query = "SELECT * FROM estate WHERE id > 0";

        // --- type ---
        if (!type.equals("()"))
            query += " AND estate.type IN " + type;

        // --- price ---
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            query += " AND estate.price >= " + priceMin;
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            query += " AND estate.price <= " + priceMax;

        // --- surface ---
        if (!TextUtils.isEmpty(binding.searchSurfaceMinTextInputEditText.getText()))
            query += " AND estate.surface >= " + surfaceMin;
        if (!TextUtils.isEmpty(binding.searchSurfaceMaxTextInputEditText.getText()))
            query += " AND estate.surface <= " + surfaceMax;

        // --- location ---
        if (!TextUtils.isEmpty(binding.searchCityTextInputEditText.getText()))
            query += " AND estate.city LIKE " + "'%" + city + "%'";

        // --- point of interest ---
        if (pointInterest.contains("school"))
            query += " AND estate.points_interest LIKE '%school%'";

        if (pointInterest.contains("shop"))
            query += " AND estate.points_interest LIKE '%shop%'";

        if (pointInterest.contains("park"))
            query += " AND estate.points_interest LIKE '%park%'";

        if (pointInterest.contains("transportation"))
            query += " AND estate.points_interest LIKE '%transportation%'";

        // --- isSold ---
        if (isSold != 123)
            query += " AND estate.isSold = " + isSold;

        // --- entry date ---
        if (date_min != 0)
            query += " AND estate.entry_date >= " + date_min;

        if (date_max != 0)
            query += " AND estate.entry_date <= " + date_max;

        // --- sold date ---
        if (sold_min != 0)
            query += " AND estate.date_sale >= " + sold_min;

        if (sold_max != 0)
            query += " AND estate.date_sale <= " + sold_max;

        // --- number picture ---
        if (!TextUtils.isEmpty(binding.searchPhotoAutocompleteTextView.getText()))
            query += " AND estate.number_picture >= " + numberPhoto;

        query += " ;";

        sendQueryToListFragment(query);

    }

    private void sendQueryToListFragment(String query) {
        viewModel.getAllEstatesAccordingToUserSearch(new SimpleSQLiteQuery(query)).observe(getViewLifecycleOwner(), new Observer<List<Estate>>() {
            @Override
            public void onChanged(@Nullable List<Estate> estates) {
                if (estates.isEmpty()) {
                    Snackbar.make(getActivity().findViewById(R.id.main_nav_host_fragment), getString(R.string.no_result), Snackbar.LENGTH_SHORT).show();
                } else {
                    viewModel.addEstateList(estates);
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
                    SearchFragmentDirections.ActionSearchFragmentToListFragment action = SearchFragmentDirections.actionSearchFragmentToListFragment();
                    action.setOrigin(SEARCH_FRAGMENT);
                    navController.navigate(action);
                }
            }
        });
    }


}