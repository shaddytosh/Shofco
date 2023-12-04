package ke.co.shofcosacco.ui.statements;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentAccountBalanceStatementBinding;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class SavingsStatementFragment extends BaseFragment implements AccountBalanceFosaStatementAdapter.Listener {

    private FragmentAccountBalanceStatementBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;
    private AccountBalanceFosaStatementAdapter accountBalanceFosaAdapter;
    private ProgressDialog progressDialog;


    public SavingsStatementFragment() {
        // Required empty public constructor
    }

    public static SavingsStatementFragment newInstance() {
        return new SavingsStatementFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(requireActivity()).get(LiveDataViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBalanceStatementBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });
        binding.btnGoBack.setOnClickListener(v -> {
            navigateUp();
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);


        accountBalanceFosaAdapter = new AccountBalanceFosaStatementAdapter(requireContext(),this);

        binding.fosarecyclerView.setAdapter(accountBalanceFosaAdapter);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getAccountBalancesFosaFree();
        });

        if (liveDataViewModel.getAccountBalancesFosaLiveData().getValue() == null) {
            getAccountBalancesFosaFree();
        } else {
            displayData(liveDataViewModel.getAccountBalancesFosaLiveData().getValue().accountBalanceFosa);
        }

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (SavingsStatementFragment.this.getParentFragment() != null && SavingsStatementFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) SavingsStatementFragment.this.getParentFragment()).navigateToHome();
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



    private void getAccountBalancesFosaFree() {
        progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);
        authViewModel.getAccountBalancesFosaFree().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                AccountBalanceFosaResponse accountBalanceFosaResponse = apiResponse.body();
                displayData(accountBalanceFosaResponse.accountBalanceFosa);
                liveDataViewModel.setAccountBalancesFosaLiveData(accountBalanceFosaResponse);
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }
        });
    }

    private void displayData(List<AccountBalanceFosa> accountBalanceFosa){

        boolean isFosaEmpty = accountBalanceFosa.isEmpty();

        binding.llEmpty.setVisibility(isFosaEmpty ? View.VISIBLE : View.GONE);
        binding.llLoading.setVisibility(isFosaEmpty ? View.VISIBLE : View.GONE);
        binding.llData.setVisibility(!isFosaEmpty ? View.VISIBLE : View.GONE);
        accountBalanceFosaAdapter.submitList(accountBalanceFosa);
        binding.fosarecyclerView.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.tvFosa.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(AccountBalanceFosa item) {
        navigate(SavingsStatementFragmentDirections.actionSavingsStatementsToStatements(item.getBalCode(),item.toString()));
    }

}