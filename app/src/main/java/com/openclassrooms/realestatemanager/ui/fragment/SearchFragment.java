package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.Calendar;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constant.SEARCH_FRAGMENT;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private Activity activity;
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
        activity = getActivity();
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
    }


    public void getPrice(){
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            priceMin = Integer.parseInt(binding.searchPriceMinTextInputEditText.getText().toString().trim());
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            priceMax = Integer.parseInt(binding.searchPriceMaxTextInputEditText.getText().toString().trim());
    }

    public void getEstateQuery(){
        String query = "SELECT * FROM estate WHERE id > 0";

        // --- price ---
        if (!TextUtils.isEmpty(binding.searchPriceMinTextInputEditText.getText()))
            query += " AND estate.price >= " + priceMin;
        if (!TextUtils.isEmpty(binding.searchPriceMaxTextInputEditText.getText()))
            query += " AND estate.price <= " + priceMax;

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
                    Toast.makeText(activity, "List-Size" + estates.size(), Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
                    SearchFragmentDirections.ActionSearchFragmentToListFragment action = SearchFragmentDirections.actionSearchFragmentToListFragment();
                    action.setOrigin(SEARCH_FRAGMENT);
                    navController.navigate(action);
                }
            }
        });
    }


}