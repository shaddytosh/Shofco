package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_EXPIRED_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentLoansBinding;
import ke.co.shofcosacco.app.models.LoanBalance;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class LoansFragment extends BaseFragment  implements LoanBalanceAdapter.Listener{

    private FragmentLoansBinding binding;
    private LoanBalanceAdapter loanBalanceAdapter;
    private AuthViewModel authViewModel;

    public LoansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoansBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        loanBalanceAdapter = new LoanBalanceAdapter(requireContext(),this);

        binding.loansRecyclerView.setAdapter(loanBalanceAdapter);

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getLoans();
        });

        getLoans();

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (LoansFragment.this.getParentFragment() != null && LoansFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) LoansFragment.this.getParentFragment()).navigateToHome();
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

    private void getLoans() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting Loans. Please wait...", true);

        authViewModel.getLoanBalance().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);

            if (apiResponse != null && apiResponse.isSuccessful()) {

                boolean isSuccess = apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS);

                if (isSuccess) {

                    List<LoanBalance> loanBalances = apiResponse.body().loanBalances;

                    loanBalanceAdapter.submitList(loanBalances);

                    binding.llData.setVisibility(!loanBalances.isEmpty() ? View.VISIBLE : View.GONE);
                    binding.llEmpty.setVisibility(loanBalances.isEmpty() ? View.VISIBLE : View.GONE);
                    binding.loansRecyclerView.setVisibility(!loanBalances.isEmpty() ? View.VISIBLE : View.GONE);
                }else if (apiResponse.body().statusCode.equals(STATUS_CODE_INVALID_TOKEN) ||
                        apiResponse.body().statusCode.equals(STATUS_CODE_INVALID_EXPIRED_TOKEN)) {

                    try {
                        authViewModel.removeLoggedInUser();
                        Toast.makeText(requireContext(), "Session expired! Please login again", Toast.LENGTH_SHORT).show();
                        Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (GeneralSecurityException | IOException e) {
                        throw new RuntimeException(e);
                    }

                }else {
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    binding.llData.setVisibility(View.GONE);
                }
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }

        });
    }


    @Override
    public void onClick(LoanBalance item) {
        navigate(LoansFragmentDirections.actionLoansToLoanStatements(item.getLoanNo(), item.getLoanName()));
    }

}