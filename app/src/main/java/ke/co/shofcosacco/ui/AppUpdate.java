package ke.co.shofcosacco.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.ke.shofcosacco.R;

public class AppUpdate {

    private final Activity activity;
    private AlertDialog updateDialog;

    public AppUpdate(Activity activity) {
        this.activity = activity;
    }

    /**
     * Checks app version against provided play store version and optionally forces update.
     *
     * @param latestPlayStoreVersion Latest version code string from Play Store (e.g., from API or Firebase)
     * @param forceUpdate            Whether the update should be mandatory or optional
     */
    public void checkForUpdateAndProceed(String latestPlayStoreVersion, boolean forceUpdate) {
        String currentVersion = getCurrentAppVersion();
        double current = Double.parseDouble(currentVersion);
        double latest =  latestPlayStoreVersion != null ?  Double.parseDouble(latestPlayStoreVersion) : Double.parseDouble(getCurrentAppVersion());

        if (current < latest) {
            showUpdateDialog(
                    activity.getString(R.string.app_name)+" is getting better",
                    "You are using an old version of the app. Get the latest app version from the Google Play Store to continue enjoying services.",
                    forceUpdate
            );
        }
    }

    private String getCurrentAppVersion() {
        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            return String.valueOf(pInfo.getLongVersionCode());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "0";
        }
    }

    private void showUpdateDialog(String title, String message, boolean forceUpdate) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update, null);

        TextView titleView = view.findViewById(R.id.title);
        TextView messageView = view.findViewById(R.id.message);
        TextView btnUpdate = view.findViewById(R.id.update);
        TextView cancel = view.findViewById(R.id.cancel);

        titleView.setText(title);
        messageView.setText(message);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.RoundedCornersDialog);
        builder.setView(view);
        builder.setCancelable(!forceUpdate); // If forceUpdate = true, user cannot dismiss dialog

        updateDialog = builder.create();
        updateDialog.show();

        if (forceUpdate) {
            cancel.setVisibility(View.GONE);
        } else {
            cancel.setVisibility(View.VISIBLE);
        }


        btnUpdate.setOnClickListener(v -> {
            updateDialog.dismiss();
            openPlayStore();

        });

        cancel.setOnClickListener(v -> updateDialog.dismiss());
    }

    private void openPlayStore() {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName()));
        activity.startActivity(intent);
    }
}
