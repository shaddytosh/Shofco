package ke.co.shofcosacco.ui.loyaltyPoints;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentLoyaltyPointsBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.main.MainFragment;

public class LoyaltyPointsFragment extends BaseFragment {

    private FragmentLoyaltyPointsBinding binding;
    private AuthViewModel authViewModel;

    public LoyaltyPointsFragment() {
        // Required empty public constructor
    }

    public static LoyaltyPointsFragment newInstance() {
        return new LoyaltyPointsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoyaltyPointsBinding.inflate(inflater, container, false);

        binding.toolbar.setOnClickListener(v -> {
            if (LoyaltyPointsFragment.this.getParentFragment() != null && LoyaltyPointsFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) LoyaltyPointsFragment.this.getParentFragment()).navigateToHome();
            }
        });

        binding.tvRedeem.setOnClickListener(view -> Toast.makeText(requireContext(), "You have 0 points to redeem", Toast.LENGTH_SHORT).show());

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getLoyalty();
        });

        getLoyalty();

        return binding.getRoot();

    }

    private void getLoyalty() {
        binding.tvLoyaltyAccount.setText(authViewModel.getMemberNo());
    }

    public void onBackPressed() {
        if (LoyaltyPointsFragment.this.getParentFragment() != null && LoyaltyPointsFragment.this.getParentFragment() instanceof MainFragment) {
            ((MainFragment) LoyaltyPointsFragment.this.getParentFragment()).navigateToHome();
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