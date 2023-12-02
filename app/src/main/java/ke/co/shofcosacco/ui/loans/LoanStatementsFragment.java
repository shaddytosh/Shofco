package ke.co.shofcosacco.ui.loans;


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
import co.ke.shofcosacco.databinding.FragmentLoanStatementsBinding;
import ke.co.shofcosacco.app.models.LoanStatement;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;

public class LoanStatementsFragment extends BaseFragment implements LoanStatementAdapter.Listener {

    private FragmentLoanStatementsBinding binding;
    private LoanStatementAdapter loanStatementAdapter;
    private AuthViewModel authViewModel;

    public LoanStatementsFragment() {
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
        binding = FragmentLoanStatementsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        loanStatementAdapter = new LoanStatementAdapter(requireContext(),this);
        binding.statementRecyclerView.setAdapter(loanStatementAdapter);

        assert getArguments() != null;
        String loanNo = LoanStatementsFragmentArgs.fromBundle(getArguments()).getLoanNo();
        String loanName = LoanStatementsFragmentArgs.fromBundle(getArguments()).getLoanName();

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getStatement(loanNo, loanName);
        });

        getStatement(loanNo, loanName);

        return binding.getRoot();

    }

    private void getStatement(String loanNo, String loanName) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting loan statement. Please wait...", true);
        authViewModel.getLoanStatement(loanNo).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);

            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            List<LoanStatement> loanStatements = isSuccess ? apiResponse.body().loanStatementList : Collections.emptyList();
            binding.llData.setVisibility(isSuccess && !loanStatements.isEmpty() ? View.VISIBLE : View.GONE);
            binding.llEmpty.setVisibility(isSuccess && loanStatements.isEmpty() ? View.VISIBLE : View.GONE);
            binding.tvTitle.setVisibility(isSuccess && !loanStatements.isEmpty() ? View.VISIBLE : View.GONE);


            binding.tvTitle.setText(String.format("Showing loan statement for: %s", loanName));

            loanStatementAdapter.submitList(loanStatements);
            binding.statementRecyclerView.setVisibility(isSuccess && !loanStatements.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public void onClick(LoanStatement item) {

    }
}