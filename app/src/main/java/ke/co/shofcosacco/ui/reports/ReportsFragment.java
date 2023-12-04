package ke.co.shofcosacco.ui.reports;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import co.ke.shofcosacco.databinding.FragmentReportsBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class ReportsFragment extends BaseFragment {

    private FragmentReportsBinding binding;

    public ReportsFragment() {
        // Required empty public constructor
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.layoutTwo.cvSavingsStatement.setOnClickListener(view -> navigate(ReportsFragmentDirections.actionReportsToSavingsStatements()));
        binding.layoutTwo.cvDetailedStatement.setOnClickListener(v ->  navigate(ReportsFragmentDirections.actionMainToMemberIsLoanQuaranteed()));
        binding.layoutThree.cvLoansGuaranteed.setOnClickListener(v ->  navigate(ReportsFragmentDirections.actionMainToLoansGuaranteed()));
        binding.layoutThree.cvMemberIsLoanGuaranteed.setOnClickListener(v -> navigate(ReportsFragmentDirections.actionMainToDetailedStatement()));

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (ReportsFragment.this.getParentFragment() != null && ReportsFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) ReportsFragment.this.getParentFragment()).navigateToHome();
                }else {
                    navigateUp();
                }
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {
                // Dismiss the dialog if "No" is clicked
                dialogInterface.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();

    }


}