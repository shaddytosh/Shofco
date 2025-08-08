package ke.co.shofcosacco;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;


import java.io.IOException;
import java.security.GeneralSecurityException;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.ActivityMainBinding;
import ke.co.shofcosacco.app.utils.EdgeToEdgeUtils;
import ke.co.shofcosacco.ui.auth.AuthViewModel;


public class MainActivity extends AppCompatActivity {

    private AuthViewModel authViewModel;
    private static final String LAST_PAUSE_TIME_KEY = "last_pause_time";
    private SharedPreferences preferences;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View root = binding.root;
        getWindow().getDecorView().post(() -> EdgeToEdgeUtils.enableEdgeToEdge(this,root));

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
        if (difference > 10 * 60 * 1000) { // 3 minutes in milliseconds
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