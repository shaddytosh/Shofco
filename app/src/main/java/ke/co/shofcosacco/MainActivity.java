package ke.co.shofcosacco;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import co.ke.shofcosacco.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the default night mode based on the user's preference
        int nightMode = AppCompatDelegate.MODE_NIGHT_NO; // or MODE_NIGHT_NO or MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(nightMode);

    }
}