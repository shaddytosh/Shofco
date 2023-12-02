package ke.co.shofcosacco.ui.statements;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentAccountBalanceStatementBinding;
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.models.AccountBalanceBosa;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.MainFragmentDirections;

public class AccountBalanceStatementFragment extends BaseFragment implements AccountBalanceBosaStatementAdapter.Listener, AccountBalanceFosaStatementAdapter.Listener {

    private FragmentAccountBalanceStatementBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;
    private AccountBalanceBosaStatementAdapter accountBalanceBosaAdapter;
    private AccountBalanceFosaStatementAdapter accountBalanceFosaAdapter;
    private ProgressDialog progressDialog;


    public AccountBalanceStatementFragment() {
        // Required empty public constructor
    }

    public static AccountBalanceStatementFragment newInstance() {
        return new AccountBalanceStatementFragment();
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
            if (AccountBalanceStatementFragment.this.getParentFragment() != null && AccountBalanceStatementFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) AccountBalanceStatementFragment.this.getParentFragment()).navigateToHome();
            }
        });
        binding.btnGoBack.setOnClickListener(v -> {
            if (AccountBalanceStatementFragment.this.getParentFragment() != null && AccountBalanceStatementFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) AccountBalanceStatementFragment.this.getParentFragment()).navigateToHome();
            }
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        accountBalanceBosaAdapter = new AccountBalanceBosaStatementAdapter(requireContext(),this);

        accountBalanceFosaAdapter = new AccountBalanceFosaStatementAdapter(requireContext(),this);

        binding.bosarecyclerView.setAdapter(accountBalanceBosaAdapter);

        binding.fosarecyclerView.setAdapter(accountBalanceFosaAdapter);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getAccountBalancesBosaFree();
        });

        if (liveDataViewModel.getAccountBalancesBosaLiveData().getValue() == null ||
                liveDataViewModel.getAccountBalancesFosaLiveData().getValue() == null) {
            getAccountBalancesBosaFree();
        } else {
            displayData(liveDataViewModel.getAccountBalancesBosaLiveData().getValue().accountBalanceBosa,
                    liveDataViewModel.getAccountBalancesFosaLiveData().getValue().accountBalanceFosa);
        }
        return binding.getRoot();

    }


    private void getAccountBalancesBosaFree() {
        progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);

        authViewModel.getAccountBalancesBosaFree().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.isSuccessful()) {
                AccountBalanceBosaResponse accountBalanceBosaResponse = apiResponse.body();
                getAccountBalancesFosaFree(accountBalanceBosaResponse.accountBalanceBosa);
                liveDataViewModel.setAccountBalancesBosaLiveData(accountBalanceBosaResponse);
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
                binding.llLoading.setVisibility(View.GONE);
                progressDialog.dismiss();

            }
        });
    }

    private void getAccountBalancesFosaFree(List<AccountBalanceBosa> accountBalanceBosa) {
        authViewModel.getAccountBalancesFosaFree().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                AccountBalanceFosaResponse accountBalanceFosaResponse = apiResponse.body();
                displayData(accountBalanceBosa, accountBalanceFosaResponse.accountBalanceFosa);
                liveDataViewModel.setAccountBalancesFosaLiveData(accountBalanceFosaResponse);
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }
        });
    }

    private void displayData(List<AccountBalanceBosa> accountBalanceBosa,List<AccountBalanceFosa> accountBalanceFosa){

        boolean isBosaEmpty = accountBalanceBosa.isEmpty();
        boolean isFosaEmpty = accountBalanceFosa.isEmpty();

        binding.llEmpty.setVisibility(isBosaEmpty && isFosaEmpty ? View.VISIBLE : View.GONE);
        binding.llLoading.setVisibility(isBosaEmpty && isFosaEmpty ? View.VISIBLE : View.GONE);
        binding.llData.setVisibility(!isBosaEmpty && !isFosaEmpty ? View.VISIBLE : View.GONE);



        accountBalanceBosaAdapter.submitList(accountBalanceBosa);
        accountBalanceFosaAdapter.submitList(accountBalanceFosa);

        binding.bosarecyclerView.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.fosarecyclerView.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.tvBosa.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.tvFosa.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(AccountBalanceBosa item) {
        navigate(MainFragmentDirections.actionMainToStatements(item.getBalCode(),item.getAccountName()));
    }

    @Override
    public void onClick(AccountBalanceFosa item) {
        navigate(MainFragmentDirections.actionMainToStatements(item.getBalCode(),item.toString()));
    }

    public void onBackPressed() {
        if (AccountBalanceStatementFragment.this.getParentFragment() != null && AccountBalanceStatementFragment.this.getParentFragment() instanceof MainFragment) {
            ((MainFragment) AccountBalanceStatementFragment.this.getParentFragment()).navigateToHome();
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