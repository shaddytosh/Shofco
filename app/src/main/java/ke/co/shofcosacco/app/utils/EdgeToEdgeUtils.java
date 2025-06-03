package ke.co.shofcosacco.app.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsetsController;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import co.ke.shofcosacco.R;


public class EdgeToEdgeUtils {

    public static void enableEdgeToEdgeScreen(Activity activity) {
        Window window = activity.getWindow();
        int color = activity.getColor(R.color.colorPrimary); // your dark primary color

        // Allows content to draw behind system bars (status/nav bar)
        WindowCompat.setDecorFitsSystemWindows(window, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = window.getInsetsController();
            if (controller != null) {
                // Ensure white icons (by NOT setting LIGHT flag)
                controller.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS |
                                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
                );
            }

            // Set both bar colors to your primary color

        } else {
            View decorView = window.getDecorView();
            int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;

            // Do NOT set SYSTEM_UI_FLAG_LIGHT_STATUS_BAR — keeps icons white
            decorView.setSystemUiVisibility(flags);

            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.primary)); // e.g., R.color.black


        }
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);

    }

    public static void enableEdgeToEdge(Activity activity) {

        enableEdgeToEdgeScreen(activity);

    }

    public static void enableEdgeToEdge(Activity activity, View view) {

        enableEdgeToEdgeScreen(activity);

        if (view != null) {
            applySystemBarInsets(view);
        }


    }
    public static void applySystemBarInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
            );
            return WindowInsetsCompat.CONSUMED;
        });
    }

}
