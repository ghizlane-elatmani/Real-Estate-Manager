package com.openclassrooms.realestatemanager.ui.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
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
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding;
import com.openclassrooms.realestatemanager.injection.Injection;
import com.openclassrooms.realestatemanager.injection.ViewModelFactory;
import com.openclassrooms.realestatemanager.model.Estate;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.List;


public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private List<Estate> estates;
    private EstateViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);

        configureRecyclerView();

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        configureViewModel();
        Toolbar toolbar = getActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle("Real Estate Manager");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController controller = Navigation.findNavController(getActivity(), R.id.main_nav_host_fragment);
        if(item.getItemId() == R.id.action_add){
            controller.navigate(R.id.addFragment);
            return true;

        } else if (item.getItemId() == R.id.action_search){
            controller.navigate(R.id.searchFragment);
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity());
        viewModel = new ViewModelProvider(getActivity(), viewModelFactory).get(EstateViewModel.class);
    }

    private void configureRecyclerView() {
    }

}