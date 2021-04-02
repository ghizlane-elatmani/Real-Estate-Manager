package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openclassrooms.realestatemanager.databinding.FragmentSimulatorBinding;

public class SimulatorFragment extends Fragment {


    private FragmentSimulatorBinding binding;
    private double monthlyMortgagePayment = 0;
    private double totalPayment = 0;
    private double totalInterest = 0;
    private int numberYears = 0;


    public SimulatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSimulatorBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearAllFields();

        binding.simulatorCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loanPaymentCalculation();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loanPaymentCalculation() {
        double loanAmount = Double.parseDouble(binding.simulatorLoanAmountTextInputEditText.getText().toString());
        double interestedRate = Double.parseDouble(binding.simulatorInterestedRateTextInputEditText.getText().toString());
        numberYears = Integer.parseInt(binding.simulatorYearsTextInputEditText.getText().toString());

        double interestedRateYear = interestedRate / 100;
        double interestedRateMonth = interestedRateYear / 12;
        int numbersMonths = numberYears * 12;

        monthlyMortgagePayment = (Math.pow((loanAmount * interestedRateMonth) * (1 + interestedRateMonth), (numbersMonths))
                        / (Math.pow((1 + interestedRateMonth), (numbersMonths))) - 1);

        totalPayment = monthlyMortgagePayment * numbersMonths;
        totalInterest = totalPayment - loanAmount;

        displayResults(monthlyMortgagePayment, totalPayment, totalInterest);
    }



    private void displayResults(double monthlyPayment, double paymentTotal, double interestTotal) {
        binding.simulatorMonthlyPaymentTextView.setText(String.valueOf(monthlyPayment));
        binding.simulatorTotalAmountTextView.setText(String.valueOf(paymentTotal));
        binding.simulatorTotalInterestTextView.setText(String.valueOf(interestTotal));
    }


    private void clearAllFields() {
        // TODO: clear field
    }
}