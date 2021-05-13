package com.openclassrooms.realestatemanager.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivitySignInBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Agent;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.openclassrooms.realestatemanager.utils.Constant.AGENT_ID;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private EstateViewModel viewModel;
    private List<Agent> agentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        this.configureViewModel();
        this.getAllAgents();

    }

    private void configureViewModel(){
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(this);
        this.viewModel = new ViewModelProvider(this, viewModelFactory).get(EstateViewModel.class);
    }

    // --- Fetch All Agents in database ---
    private void getAllAgents() {
        viewModel.getAllAgent().observe(this, new Observer<List<Agent>>() {
            @Override
            public void onChanged(@Nullable List<Agent> agents) {
                agentList.addAll(agents);
                configure();
            }
        });
    }

    // --- The user has to choose a agent in the spinner ---
    private void configure() {
        ArrayAdapter<Agent> arrayAdapter = new ArrayAdapter(this, R.layout.row_options, agentList);
        binding.signInAutocompleteTextView.setAdapter(arrayAdapter);
        binding.signInAutocompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agent agent = (Agent)parent.getItemAtPosition(position);
                Toast.makeText(SignInActivity.this, parent.getContext().getString(R.string.welcome) + " " + agent.toString() + "!", Toast.LENGTH_SHORT).show();
                navigateToMainActivity(agent);
            }
        });
    }

    // --- When the user had chosen the agent, it will be redirected to MainActivity ---
    private void navigateToMainActivity(Agent agent) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AGENT_ID, agent.getId());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

}