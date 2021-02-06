package com.openclassrooms.realestatemanager.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.ListFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding;
import com.openclassrooms.realestatemanager.utils.Utils;

public class MainActivity extends AppCompatActivity {

    // --- Attribute ---
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        configureToolBar();
        configureDrawerNavigation();

    }


    private void configureToolBar() {
        setSupportActionBar(binding.mainToolbar);
        binding.mainToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.mainToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
    }


    private void configureDrawerNavigation() {

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.mainDrawerLayout,
                binding.mainToolbar,
                R.string.navigation_drawer_menu_open,
                R.string.navigation_drawer_menu_close);
        binding.mainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.mainNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.menu_drawer_map) {
                    // TODO
                } else if (item.getItemId() == R.id.menu_drawer_simulator) {
                    // TODO
                }

                binding.mainDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void launchListFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(binding.mainFragment.getId(), new ListFragment())
                .commit();
        setTitle(R.string.app_name);
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

}
