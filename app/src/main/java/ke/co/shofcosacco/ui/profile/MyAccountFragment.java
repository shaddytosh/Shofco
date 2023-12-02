package ke.co.shofcosacco.ui.profile;


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

import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentMyAccountBinding;
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.models.AccountBalanceBosa;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;

public class MyAccountFragment extends BaseFragment implements AccountBalanceBosaAdapter.Listener, AccountBalanceFosaAdapter.Listener {

    private FragmentMyAccountBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;

    ProgressDialog progressDialog;


    private AccountBalanceBosaAdapter accountBalanceBosaAdapter;
    private AccountBalanceFosaAdapter accountBalanceFosaAdapter;


    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(requireActivity()).get(LiveDataViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyAccountBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        accountBalanceBosaAdapter = new AccountBalanceBosaAdapter(requireContext(),this);

        accountBalanceFosaAdapter = new AccountBalanceFosaAdapter(requireContext(),this);

        binding.bosarecyclerView.setAdapter(accountBalanceBosaAdapter);

        binding.fosarecyclerView.setAdapter(accountBalanceFosaAdapter);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
           transactionCost();
        });

        if (liveDataViewModel.getAccountBalancesBosaLiveData().getValue() == null ||
                liveDataViewModel.getAccountBalancesFosaLiveData().getValue() == null) {
            transactionCost();
        } else {
            displayData(liveDataViewModel.getAccountBalancesBosaLiveData().getValue().accountBalanceBosa,
                    liveDataViewModel.getAccountBalancesFosaLiveData().getValue().accountBalanceFosa);
        }


        return binding.getRoot();

    }


    private void transactionCost(){

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);
        authViewModel.transactionCost(Constants.ACCOUNT_BALANCE,"0").observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess1 = apiResponse != null && apiResponse.isSuccessful() ;
            if (isSuccess1) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                    View customView = getLayoutInflater().inflate(R.layout.custom_dialog_transaction_cost, null);
                    builder.setView(customView);
                    TextView one =customView.findViewById(R.id.one);
                    TextView two =customView.findViewById(R.id.two);
                    one.setText(getString(R.string.you_are_about_to_view_account_balances));
                    two.setText(String.format("Transaction cost, KES %s", apiResponse.body().charges));
                    AlertDialog alertDialog = builder.create();
                    builder.setCancelable(false);

                    TextView positiveButton = customView.findViewById(R.id.positive_button);
                    positiveButton.setOnClickListener(v -> {
                        alertDialog.dismiss();
                        getAccountBalancesBosa();
                    });

                    TextView negativeButton = customView.findViewById(R.id.negative_button);
                    negativeButton.setOnClickListener(v -> {
                        // perform negative action
                        alertDialog.dismiss();
                        showEmptyState();


                    });

                    alertDialog.show();
                }else {
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

    private void getAccountBalancesBosa() {
        progressDialog = ProgressDialog.show(getContext(), "",
                "Getting account balances. Please wait...", true);

        authViewModel.getAccountBalancesBosa().observe(getViewLifecycleOwner(), apiResponse -> {
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                AccountBalanceBosaResponse accountBalanceBosaResponse = apiResponse.body();
                getAccountBalancesFosa(accountBalanceBosaResponse.accountBalanceBosa);
                liveDataViewModel.setAccountBalancesBosaLiveData(accountBalanceBosaResponse);
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }
        });
    }

    private void getAccountBalancesFosa(List<AccountBalanceBosa> accountBalanceBosa) {
        authViewModel.getAccountBalancesFosa().observe(getViewLifecycleOwner(), apiResponse -> {
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
        binding.fosarecyclerView.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.tvBosa.setVisibility(accountBalanceBosa.size() > 0 ? View.VISIBLE : View.GONE);
        binding.tvFosa.setVisibility(accountBalanceFosa.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(AccountBalanceBosa item) {
        navigate(MyAccountFragmentDirections.actionMyAccountToStatements(item.getBalCode(),item.getAccountName()));
    }

    @Override
    public void onClick(AccountBalanceFosa item) {
        navigate(MyAccountFragmentDirections.actionMyAccountToStatements(item.getBalCode(),item.getAccountName()));
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