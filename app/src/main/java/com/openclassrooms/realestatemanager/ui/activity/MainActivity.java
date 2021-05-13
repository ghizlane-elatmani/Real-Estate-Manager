package com.openclassrooms.realestatemanager.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.openclassrooms.realestatemanager.utils.Constant.ACCESS_LOCATION;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // --- Attribute ---
    private ActivityMainBinding binding;
    private NavController navController;
    private boolean isNetworkAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureNavigationUI();

    }

    // --- Configure the navigation ---
    private void configureNavigationUI() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_nav_host_fragment);
        if(navHostFragment != null){
            navController = navHostFragment.getNavController();
        }
        setSupportActionBar(binding.mainToolbar.getRoot());
        NavigationUI.setupActionBarWithNavController(this, navController, binding.mainDrawerLayout);
        NavigationUI.setupWithNavController(binding.mainToolbar.getRoot(), navController, binding.mainDrawerLayout);
        NavigationUI.setupWithNavController(binding.mainNavigationView, navController);

        binding.mainNavigationView.setNavigationItemSelectedListener(this);
    }


    // --- onBackPress for DrawerMenu ---
    @Override
    public void onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    // --- onNavigationItemSelected for DrawerMenu ---
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_drawer_map) {
            checkIfNetworkAvailable();
            if (isNetworkAvailable) {
                getPermissionsAccessLocation();
            }
        } else if (itemId == R.id.menu_drawer_simulator) {
            startActivity(new Intent(MainActivity.this, SimulatorActivity.class));
        }
        return true;
    }

    // --- check if NetWork is available, if not ask the user to allow it ---
    private void checkIfNetworkAvailable() {
        isNetworkAvailable = Utils.isNetworkAvailable(this);
        if (!isNetworkAvailable) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.network_title))
                    .setMessage(getString(R.string.network_to_access_info))
                    .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MainActivity.this.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show();
        }
    }

    // --- When the app has the permission, the user will be redirected to MapViewFragment ---
    @AfterPermissionGranted(ACCESS_LOCATION)
    private void getPermissionsAccessLocation() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            navController.navigate(R.id.mapViewFragment, null);
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_location), ACCESS_LOCATION, perms);
        }
    }

}
