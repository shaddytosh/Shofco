package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.COMING_SOON;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import co.ke.shofcosacco.databinding.FragmentMainLoanBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class MainLoansFragment extends BaseFragment {

    private FragmentMainLoanBinding binding;

    public MainLoansFragment() {
        // Required empty public constructor
    }

    public static MainLoansFragment newInstance() {
        return new MainLoansFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMainLoanBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.layoutOne.cvApplyLoan.setOnClickListener(v -> navigate(MainLoansFragmentDirections.actionMainLoanToLoanProduct()));
        binding.layoutOne.cvViewLoans.setOnClickListener(v -> navigate(MainLoansFragmentDirections.actionMainLoanToLoans()));
        binding.layoutTwo.cvRepayLoan.setOnClickListener(v -> navigate(MainLoansFragmentDirections.actionMainLoanToRepayLoan()));
        binding.layoutTwo.cvMemberIsLoanGuaranteed.setOnClickListener(v ->  navigate(MainLoansFragmentDirections.actionMainLoanToMemberIsLoanQuaranteed()));
        binding.layoutThree.cvLoansGuaranteed.setOnClickListener(v ->  navigate(MainLoansFragmentDirections.actionMainLoanToLoansGuaranteed()));
        binding.layoutThree.cvDetailedStatement.setOnClickListener(v -> navigate(MainLoansFragmentDirections.actionMainLoanToDetailedStatement()));


        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (MainLoansFragment.this.getParentFragment() != null && MainLoansFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) MainLoansFragment.this.getParentFragment()).navigateToHome();
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

    private void toast(){
        Toast.makeText(requireContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
    }

}