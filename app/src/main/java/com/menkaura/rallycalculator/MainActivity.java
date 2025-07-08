package com.menkaura.rallycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.menkaura.rallycalculator.databinding.ActivityMainBinding;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Antes que nada se carga el idioma
        loadLocale();

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = NavHostFragment.findNavController(navHostFragment);
            NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
        }

        // Listener para los botones del menu
        binding.bottomNavigationView.setOnItemSelectedListener(this::onMenuSelected);
    }


    /**
     * Listener para los botones del menu
     * @param menuItem El item seleccionado
     * @return true si el item ha sido seleccionado
     */
    private boolean onMenuSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.calc_menu) {
            navController.navigate(R.id.calcFragment);
        } else if (menuItem.getItemId() == R.id.config_menu) {
            navController.navigate(R.id.configFragment);
        }
        return true;
    }


    /**
     * Metodo que carga el idioma guardado en SharedPreferences
     */
    private void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.equals("")) {
            Locale locale = new Locale(language);
            Locale.setDefault(locale);

            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
    }
}