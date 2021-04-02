package com.openclassrooms.realestatemanager.ui.fragment;

import android.app.Activity;
import android.content.Context;
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
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
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
import com.openclassrooms.realestatemanager.model.Photo;
import com.openclassrooms.realestatemanager.ui.adapter.EstateAdapter;
import com.openclassrooms.realestatemanager.utils.Constant;
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    // adapter
    private EstateAdapter adapter;

    // view model
    private EstateViewModel viewModel;

    // variables
    private List<Estate> estateList;
    private List<Photo> photoList;
    private Activity activity;
    private String origin;
    private int estateId;

    // views, binding
    FragmentListBinding binding;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            origin = ListFragmentArgs.fromBundle(getArguments()).getOrigin();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        setHasOptionsMenu(true);
        photoList = new ArrayList<>();
        estateList = new ArrayList<>();

        adapter = new EstateAdapter(getContext(), estateList, photoList);
        binding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.fragmentListRecyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        configureViewModel();

        if (origin.equals(Constant.SEARCH_FRAGMENT))
            getSearchedEstate();
        else
            getAllEstates();

        Toolbar toolbar = requireActivity().findViewById(R.id.main_toolbar);
        toolbar.setTitle("Real Estate Manager");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController controller = Navigation.findNavController(activity, R.id.main_nav_host_fragment);
        if (item.getItemId() == R.id.action_add) {
            Bundle args = new Bundle();
            args.putLong("estateID", estateId);
            controller.navigate(R.id.addOrEditFragment, args);
            return true;
        } else if (item.getItemId() == R.id.action_search) {
            controller.navigate(R.id.searchFragment);
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            controller.navigate(R.id.addOrEditFragment,null);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    private void configureViewModel() {
        ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(activity);
        viewModel = new ViewModelProvider((FragmentActivity) activity, viewModelFactory).get(EstateViewModel.class);
    }


    private void getAllEstates() {
        viewModel.getAllEstate().observe(getViewLifecycleOwner(), new Observer<List<Estate>>() {
            @Override
            public void onChanged(@Nullable List<Estate> estates) {
                initList(estates);
                Log.i("ListFragment", String.valueOf(estates.size()));
            }
        });
    }


    private void getSearchedEstate() {
        viewModel.getEstateList().observe(getViewLifecycleOwner(), new Observer<List<Estate>>() {
            @Override
            public void onChanged(@Nullable List<Estate> estates) {
                estateList = estates;
                initList(estateList);
            }
        });
    }

    private void initList(List<Estate> estates) {
        estateList.clear();
        estateList.addAll(estates);
        for (Estate estate : estateList) {
            getOnePicture(estate.getId());
        }
    }

    private void getOnePicture(int estateId) {
        viewModel.getOnePicture(estateId).observe(getViewLifecycleOwner(), new Observer<Photo>() {
            @Override
            public void onChanged(Photo photo) {
                initPictures(photo);
            }
        });
    }

    private void initPictures(Photo photo) {
        if (photo != null)
            photoList.add(photo);
        adapter.notifyDataSetChanged();
    }


    private void openDetailsFragment(int estateId) {
//        NavController mController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
//        PropertyListFragmentDirections.ActionPropertyListFragmentToDetailsFragment action = PropertyListFragmentDirections.actionPropertyListFragmentToDetailsFragment(realEstateId);
//        mController.navigate(action);
    }


    @Override
    public void onDestroyView() {
        binding.fragmentListRecyclerView.setAdapter(null);
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllEstates();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
