package ke.co.shofcosacco.ui.statements;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.Collections;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentStatementsBinding;
import ke.co.shofcosacco.app.api.responses.StatementResponse;
import ke.co.shofcosacco.app.models.MiniStatement;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class StatementsFragment extends BaseFragment implements StatementAdapter.Listener {

    private FragmentStatementsBinding binding;
    private StatementAdapter statementAdapter;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;

    public StatementsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(this).get(LiveDataViewModel.class);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatementsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        statementAdapter = new StatementAdapter(requireContext(),this);
        binding.statementRecyclerView.setAdapter(statementAdapter);

        assert getArguments() != null;
        String balCode = StatementsFragmentArgs.fromBundle(getArguments()).getBalCode();
        String accountName = StatementsFragmentArgs.fromBundle(getArguments()).getAccountName();

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            transactionCost(balCode, accountName);
        });

        transactionCost(balCode, accountName);

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (StatementsFragment.this.getParentFragment() != null && StatementsFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) StatementsFragment.this.getParentFragment()).navigateToHome();
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


    private void transactionCost(String balCode, String accountName){

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);
        authViewModel.transactionCost(Constants.MINISTATEMENT,"0").observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess1 = apiResponse != null && apiResponse.isSuccessful() ;
            if (isSuccess1) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                    View customView = getLayoutInflater().inflate(R.layout.custom_dialog_transaction_cost, null);
                    builder.setView(customView);
                    TextView one =customView.findViewById(R.id.one);
                    TextView two =customView.findViewById(R.id.two);
                    one.setText(getString(R.string.you_are_about_to_view_account_statement));
                    two.setText(String.format("Transaction cost, KES %s", apiResponse.body().charges));
                    AlertDialog alertDialog = builder.create();
                    builder.setCancelable(false);

                    TextView positiveButton = customView.findViewById(R.id.positive_button);
                    positiveButton.setOnClickListener(v -> {
                        alertDialog.dismiss();
                        getStatement(balCode,accountName);
                    });

                    TextView negativeButton = customView.findViewById(R.id.negative_button);
                    negativeButton.setOnClickListener(v -> {
                        // perform negative action
                        alertDialog.dismiss();
                        showEmptyState();


                    });

                    alertDialog.show();
                }else {
                    notSuccessDialog(apiResponse.body().statusDesc);
                    showEmptyState();
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
                showEmptyState();
            }
        });
    }

    private void showEmptyState() {
        binding.llEmpty.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.GONE);
        binding.llLoading.setVisibility(View.GONE);
    }

    private void getStatement(String balCode, String accountName) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting statement. Please wait...", true);
        authViewModel.getAccountStatement(balCode).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess){
                displayData(apiResponse.body(),accountName);
                liveDataViewModel.setStatementResponseLiveData(apiResponse.body());
            }else {
                showEmptyState();
            }

        });
    }

    private void displayData(StatementResponse statementResponse, String accountName){
        List<MiniStatement> miniStatements = statementResponse.statusCode.equals(STATUS_CODE_SUCCESS) ? statementResponse.miniStatement : Collections.emptyList();
        binding.llData.setVisibility(!miniStatements.isEmpty() ? View.VISIBLE : View.GONE);
        binding.llEmpty.setVisibility(miniStatements.isEmpty() ? View.VISIBLE : View.GONE);
        binding.tvTitle.setVisibility(!miniStatements.isEmpty() ? View.VISIBLE : View.GONE);

        binding.tvTitle.setText(String.format("Showing statement for: %s", accountName));

        statementAdapter.submitList(miniStatements);
        binding.statementRecyclerView.setVisibility(!miniStatements.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(MiniStatement item) {

    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",3000);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
}