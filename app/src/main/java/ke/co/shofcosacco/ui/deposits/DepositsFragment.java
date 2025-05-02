package ke.co.shofcosacco.ui.deposits;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentDepositsBinding;
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.models.AccountBalanceBosa;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class DepositsFragment extends BaseFragment implements DepositAccountBalanceBosaAdapter.Listener, DepositAccountBalanceFosaAdapter.Listener {

    private FragmentDepositsBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;
    private DepositAccountBalanceBosaAdapter accountBalanceBosaAdapter;
    private DepositAccountBalanceFosaAdapter accountBalanceFosaAdapter;
    private  ProgressDialog progressDialog;


    public DepositsFragment() {
        // Required empty public constructor
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
        binding = FragmentDepositsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });
        binding.btnGoBack.setOnClickListener(v -> {
            navigateUp();
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        accountBalanceBosaAdapter = new DepositAccountBalanceBosaAdapter(requireContext(),this);

        accountBalanceFosaAdapter = new DepositAccountBalanceFosaAdapter(requireContext(),this);

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

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (DepositsFragment.this.getParentFragment() != null && DepositsFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) DepositsFragment.this.getParentFragment()).navigateToHome();
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


    private void getAccountBalancesBosaFree() {
        progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);

        authViewModel.getAccountBalancesBosaFree().observe(getViewLifecycleOwner(), apiResponse -> {
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                AccountBalanceBosaResponse accountBalanceBosaResponse = apiResponse.body();
                getAccountBalancesFosaFree(accountBalanceBosaResponse.accountBalanceBosa);
                liveDataViewModel.setAccountBalancesBosaLiveData(accountBalanceBosaResponse);
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
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

        List<AccountBalanceBosa> safeAccountBalanceBosa = accountBalanceBosa == null ? new ArrayList<>() : accountBalanceBosa;

        boolean isBosaEmpty = safeAccountBalanceBosa.isEmpty();

        List<AccountBalanceFosa> safeAccountBalanceFosa = accountBalanceFosa == null ? new ArrayList<>() : accountBalanceFosa;

        boolean isFosaEmpty = safeAccountBalanceFosa.isEmpty();

        binding.llEmpty.setVisibility(isBosaEmpty && isFosaEmpty ? View.VISIBLE : View.GONE);
        binding.llLoading.setVisibility(View.GONE);
        binding.llData.setVisibility(!isBosaEmpty || !isFosaEmpty ? View.VISIBLE : View.GONE);

        accountBalanceBosaAdapter.submitList(accountBalanceBosa);
        accountBalanceFosaAdapter.submitList(accountBalanceFosa);

        if (accountBalanceBosa != null) {
            binding.bosarecyclerView.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        }
        if (accountBalanceFosa != null) {
            binding.fosarecyclerView.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
        }
        if (accountBalanceBosa != null) {
            binding.tvBosa.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        }
        if (accountBalanceFosa != null) {
            binding.tvFosa.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(AccountBalanceBosa item) {
        navigate(DepositsFragmentDirections.actionDepositToDepositFromMobile(item.getBalCode(),item.getAccountName()));
    }

    @Override
    public void onClick(AccountBalanceFosa item) {
        navigate(DepositsFragmentDirections.actionDepositToDepositFromMobile(item.getBalCode(),item.toString()));
    }


    public void onBackPressed() {
        navigateUp();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DISMISS && resultCode == Activity.RESULT_OK && data != null) {
            getAccountBalancesBosaFree();
        }else if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            notSuccessDialog(data.getStringExtra("message"));
        }
    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type", 111111);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
}