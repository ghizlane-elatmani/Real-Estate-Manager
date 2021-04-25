package com.openclassrooms.realestatemanager.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.databinding.ActivitySimulatorBinding;

import java.text.DecimalFormat;

public class SimulatorActivity extends AppCompatActivity {

    private ActivitySimulatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySimulatorBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.simulatorCalculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loanPaymentCalculation();
            }
        });
    }

    private void loanPaymentCalculation() {
        double loanAmount = Integer.parseInt(binding.simulatorLoanAmountTextInputEditText.getText().toString());
        double interestRate = (Integer.parseInt(binding.simulatorInterestedRateTextInputEditText.getText().toString()));
        double loanPeriod = Integer.parseInt(binding.simulatorYearsTextInputEditText.getText().toString()) * 12;
        double r = interestRate/1200;
        double r1 = Math.pow(r+1,loanPeriod);

        double monthlyPayment = (double) ((r+(r/(r1-1))) * loanAmount);
        double totalPayment = monthlyPayment * loanPeriod;

        binding.simulatorMonthlyPaymentTextView.setText(new DecimalFormat("##.##").format(monthlyPayment));
        binding.simulatorTotalAmountTextView.setText(new DecimalFormat("##.##").format(totalPayment));
    }

}