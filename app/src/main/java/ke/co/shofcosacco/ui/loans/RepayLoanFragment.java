package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.Collections;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRepayLoanBinding;
import ke.co.shofcosacco.app.models.LoanBalance;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;

public class RepayLoanFragment extends BaseFragment  implements LoanRepaymentAdapter.Listener{

    private FragmentRepayLoanBinding binding;
    private LoanRepaymentAdapter loanRepaymentAdapter;
    private AuthViewModel authViewModel;

    public RepayLoanFragment() {
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
        binding = FragmentRepayLoanBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        loanRepaymentAdapter = new LoanRepaymentAdapter(requireContext(),this);

        binding.loansRecyclerView.setAdapter(loanRepaymentAdapter);

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getLoans();
        });

        getLoans();


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

                List<LoanBalance> loanBalances = isSuccess ? apiResponse.body().loanBalances : Collections.emptyList();

                binding.llData.setVisibility(!loanBalances.isEmpty() ? View.VISIBLE : View.GONE);
                binding.llEmpty.setVisibility(loanBalances.isEmpty() ? View.VISIBLE : View.GONE);
                binding.loansRecyclerView.setVisibility(!loanBalances.isEmpty() ? View.VISIBLE : View.GONE);

                loanRepaymentAdapter.submitList(loanBalances);

            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }

        });
    }


    @Override
    public void onClick(LoanBalance item) {
        navigate(RepayLoanFragmentDirections.actionRepayLoanToRepayLoan2(item));
    }

}