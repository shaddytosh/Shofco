package ke.co.shofcosacco.ui.settings;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentSettingsBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.PreventDoubleClick;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.MainFragmentDirections;


public class SettingsFragment extends BaseFragment {

    private FragmentSettingsBinding binding;
    private AuthViewModel authViewModel;


    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            if (SettingsFragment.this.getParentFragment() != null && SettingsFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) SettingsFragment.this.getParentFragment()).navigateToHome();
            }else {
                navigateUp();
            }
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
        });

        binding.tvLogOut.setOnClickListener(v -> logOut());

        binding.tvChangePin.setOnClickListener(v -> changePin());

        binding.tvInvite.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = getString(R.string.share_message);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });


        binding.tvMyProfile.setOnClickListener(v -> navigate(MainFragmentDirections.actionMainToProfile()));


        return binding.getRoot();

    }



    private void logOut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView textView =customView.findViewById(R.id.dialog_message);
        textView.setText(getString(R.string.log_out_text));

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            try {
                authViewModel.removeLoggedInUser();
                Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });

        alertDialog.show();
    }

    private void changePin(){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView textView =customView.findViewById(R.id.dialog_message);
        textView.setText("Do you want to change pin?");

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            navigate(MainFragmentDirections.actionMainToChangePin());

            alertDialog.dismiss();

        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });

        alertDialog.show();
    }

    public void onBackPressed() {
        if (SettingsFragment.this.getParentFragment() != null && SettingsFragment.this.getParentFragment() instanceof MainFragment) {
            ((MainFragment) SettingsFragment.this.getParentFragment()).navigateToHome();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPressed();
            }
        });

    }

}