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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private long estateId;

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

        configureRecyclerView();

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
        if (item.getItemId() == R.id.action_edit) {
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

    private void configureRecyclerView() {
        adapter = new EstateAdapter(estateList, photoList);
        adapter.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EstateAdapter.EstateViewHolder holder = (EstateAdapter.EstateViewHolder) view.getTag();
                int position = holder.getAdapterPosition();
                long id = estateList.get(position).getId();
                estateId = id;
                viewModel.addSelectedEstateId(estateId);
                if (!ListFragment.this.getResources().getBoolean(R.bool.isTabletLand)) {
                    ListFragment.this.openDetailsFragment(id);
                } else {
                    ListFragment.this.openDetailsFragmentLand(id);
                }
            }
        });
        if (getResources().getBoolean(R.bool.isTablet) && !getResources().getBoolean(R.bool.isTabletLand)) {
            binding.fragmentListRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false));
        } else {
            binding.fragmentListRecyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));
        }
        binding.fragmentListRecyclerView.setAdapter(adapter);
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

    private void getSelectedEstateId() {
        viewModel.getSelectedEstateId().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long estateID) {
                estateId = estateID;
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

    private void getOnePicture(long estateId) {
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


    private void openDetailsFragment(long estate_id) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment);
        ListFragmentDirections.ActionListFragmentToDetailFragment action = ListFragmentDirections.actionListFragmentToDetailFragment(estate_id);
        navController.navigate(action);
    }

    private void openDetailsFragmentLand(long realEstateId) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.main_nav_host_fragment2);
        navController.popBackStack();
        Bundle args = new Bundle();
        args.putLong("estateID", realEstateId);
        navController.navigate(R.id.detailsFragment, args);
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
