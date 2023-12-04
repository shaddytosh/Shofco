package ke.co.shofcosacco;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import java.io.IOException;
import java.security.GeneralSecurityException;

import co.ke.shofcosacco.databinding.ActivityMainBinding;
import ke.co.shofcosacco.ui.auth.AuthViewModel;


public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private static final String LAST_PAUSE_TIME_KEY = "last_pause_time";
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the default night mode based on the user's preference
        int nightMode = AppCompatDelegate.MODE_NIGHT_NO; // or MODE_NIGHT_NO or MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(nightMode);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


    }


    @Override
    protected void onPause() {
        super.onPause();

        long lastPauseTime = System.currentTimeMillis();
        saveLastPauseTime(lastPauseTime);
    }

    @Override
    protected void onResume() {
        super.onResume();

        long lastPauseTime = getLastPauseTime();
        long currentTime = System.currentTimeMillis();
        long difference = currentTime - lastPauseTime;

        // Assuming you want to check if the difference is greater than 5 minutes
        if (difference >  3000) {
            try {
                authViewModel.removeLoggedInUser();
                Intent intent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveLastPauseTime(long lastPauseTime) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(LAST_PAUSE_TIME_KEY, lastPauseTime);
        editor.apply();
    }

    private long getLastPauseTime() {
        return preferences.getLong(LAST_PAUSE_TIME_KEY, System.currentTimeMillis());
    }

}