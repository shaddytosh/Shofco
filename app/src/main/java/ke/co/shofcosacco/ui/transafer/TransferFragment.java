package ke.co.shofcosacco.ui.transafer;



import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentTransferBinding;
import ke.co.shofcosacco.app.models.DestinationAccountCombined;
import ke.co.shofcosacco.app.models.DestinationBosaAccount;
import ke.co.shofcosacco.app.models.DestinationFosaAccount;
import ke.co.shofcosacco.app.models.SourceAccount;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class TransferFragment extends BaseFragment {

    private FragmentTransferBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;

    private List<DestinationBosaAccount> destinationBosaAccounts;
    private List<DestinationFosaAccount> destinationFosaAccounts;

    private ArrayAdapter<DestinationAccountCombined> destinationAccountAdapter;
    private List<DestinationAccountCombined> destinationAccountCombinedList;

    private List<SourceAccount> sourceAccounts;
    private  ArrayAdapter<SourceAccount> sourceAccountAdapter;

    private String sourceAccountCode;
    private String destinationAccountCode;

    private String sourceAccountName;

    private String destAccountName;
    private String sourceAccountType;
    private String destinationAccountType;
    private  ProgressDialog progressDialog;




    private String sourceBalance;
    private String destBalance;
    private String transactionLimit;

    private String finalAmount;


    public TransferFragment() {
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
        binding = FragmentTransferBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);



        binding.swipeToRefresh.setOnRefreshListener(() -> {
            getSourceAccount();
            binding.swipeToRefresh.setRefreshing(false);
        });

        transactionLimit = TransferFragmentArgs.fromBundle(getArguments()).getLimit().replace(",","");


        binding.txtAmount.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilAmount.setError(null);
                }
            }
        });

        getSourceAccount();

        binding.spinnerSourceAccount.setOnItemClickListener((parent, view, position, id) -> {
            if (sourceAccounts != null && sourceAccounts.size() > 0) {
                sourceAccountCode = sourceAccountAdapter.getItem(position).balCode;
                sourceBalance = sourceAccountAdapter.getItem(position).balance.replace(",","");
                sourceAccountName = sourceAccountAdapter.getItem(position).toString();
                sourceAccountType = "FOSA";
                setSummary();
            }
        });

        binding.spinnerDestinationAccount.setOnItemClickListener((parent, view, position, id) -> {
            if (destinationAccountCombinedList != null && destinationAccountCombinedList.size() > 0) {
                destinationAccountCode = destinationAccountAdapter.getItem(position).balCode;
                destBalance = destinationAccountAdapter.getItem(position).balance.replace(",","");
                destAccountName = destinationAccountAdapter.getItem(position).accountName;
                destinationAccountType = destinationAccountAdapter.getItem(position).type;
                setSummary();
            }
        });

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.tvTransfer.setOnClickListener(v -> transfer());

        return binding.getRoot();
    }

    private void getSourceAccount(){
        progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);
        authViewModel.getSourceAccount().observe(getViewLifecycleOwner(), listAPIResponse -> {
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    sourceAccounts = listAPIResponse.body().sourceAccounts;
                    Comparator<SourceAccount> byName = (o1, o2) -> o1.getAccountName().compareTo(o2.getAccountName());
                    Collections.sort(sourceAccounts, byName);
                    if (sourceAccounts != null && sourceAccounts.size() > 0){
                        sourceAccountAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sourceAccounts);

                        binding.spinnerSourceAccount.setAdapter(sourceAccountAdapter);

                        sourceAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        getDestinationAccount();

                    }else {
                        binding.llLoading.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                    }

                }else {
                    binding.llLoading.setVisibility(View.GONE);
                    binding.llData.setVisibility(View.GONE);
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();

                }
            }else {
                binding.llLoading.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.llEmpty.setVisibility(View.VISIBLE);
                notSuccessDialog(Constants.API_ERROR);
                progressDialog.dismiss();
            }
        });
    }

    private void getDestinationAccount(){
        authViewModel.getDestinationAccount().observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    destinationBosaAccounts = listAPIResponse.body().destinationBosaAccounts;
                    destinationFosaAccounts = listAPIResponse.body().destinationFosaAccounts;

                    destinationAccountCombinedList = new ArrayList<>();

                    for (DestinationBosaAccount destinationBosaAccount : destinationBosaAccounts){
                        destinationAccountCombinedList.add(new DestinationAccountCombined(destinationBosaAccount.accountNo,
                                destinationBosaAccount.accountName, destinationBosaAccount.balances,destinationBosaAccount.balCode,"BOSA"));

                    }

                    for (DestinationFosaAccount destinationFosaAccount : destinationFosaAccounts){
                        destinationAccountCombinedList.add(new DestinationAccountCombined(destinationFosaAccount.accountNo,
                                destinationFosaAccount.accountName, destinationFosaAccount.balance,destinationFosaAccount.balCode, "FOSA"));



                    }
                    Comparator<DestinationAccountCombined> byName = (o1, o2) -> o1.accountName.compareTo(o2.accountName);
                    Collections.sort(destinationAccountCombinedList, byName);

                    if (destinationAccountCombinedList != null && destinationAccountCombinedList.size() > 0){
                        destinationAccountAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, destinationAccountCombinedList);

                        binding.spinnerDestinationAccount.setAdapter(destinationAccountAdapter);

                        destinationAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        binding.llLoading.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.VISIBLE);
                        binding.llEmpty.setVisibility(View.GONE);


                    }else {
                        binding.llLoading.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);
                        notSuccessDialog("No destination accounts available");

                    }

                }else {
                    binding.llLoading.setVisibility(View.GONE);
                    binding.llData.setVisibility(View.GONE);
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    notSuccessDialog(listAPIResponse.body().statusDesc);
                }
            }else {
                binding.llLoading.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.llEmpty.setVisibility(View.VISIBLE);
                notSuccessDialog(Constants.API_ERROR);

            }
        });
    }

    private void setSummary() {
        if (destinationAccountCode != null && sourceAccountCode != null){
          if (destinationAccountCode.equals(sourceAccountCode)){
              notSuccessDialog("You cannot do Money Transfer within the same account");
          }else {
              binding.tilAmount.setVisibility(View.VISIBLE);
              binding.tvTransfer.setVisibility(View.VISIBLE);
          }
        }
    }

    private void transfer(){
        String amount = binding.txtAmount.getText().toString();
        amount = String.valueOf(Integer.parseInt(amount));

        if (TextValidator.isEmpty(amount)){
            binding.tilAmount.setError(getString(R.string.required));
        }else {
            double sBalance= Double.parseDouble(sourceBalance);
            double tAmount= Double.parseDouble(amount);
            double mTransactionLimit= Double.parseDouble(transactionLimit);

            if (tAmount >= 1 && tAmount <= mTransactionLimit) {
                if ((sBalance > tAmount)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                    View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                    builder.setView(customView);
                    TextView textView =customView.findViewById(R.id.dialog_message);
                    textView.setText(String.format("You are about to transfer Ksh %s from %s to %s", amount, sourceAccountName, destAccountName));

                    AlertDialog alertDialog = builder.create();
                    builder.setCancelable(false);

                    TextView positiveButton = customView.findViewById(R.id.positive_button);
                     finalAmount = amount;
                    positiveButton.setOnClickListener(v -> {

                        ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "",
                                "Requesting OTP. Please wait...", true);
                        authViewModel.sendOtp(authViewModel.getMemberNo(),"TRANSFER").observe(getViewLifecycleOwner(), apiResponse1 -> {
                            progressDialog1.dismiss();
                            if (apiResponse1 != null && apiResponse1.isSuccessful()) {
                                if (apiResponse1.body().success.equals(STATUS_CODE_SUCCESS)) {
                                    OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
                                    Bundle args = new Bundle();
                                    args.putString("mOTP", apiResponse1.body().otp);
                                    dialogFragment.setArguments(args);
                                    dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
                                }
                                else {
                                    notSuccessDialog(apiResponse1.body().description);
                                }
                            } else {

                                notSuccessDialog("An error occurred. Please try again later");

                            }
                        });

                        alertDialog.dismiss();
                    });

                    TextView negativeButton = customView.findViewById(R.id.negative_button);
                    negativeButton.setOnClickListener(v -> {
                        // perform negative action
                        alertDialog.dismiss();

                    });

                    alertDialog.show();

                }else {
                    notSuccessDialog("A/C "+sourceAccountName+" has insufficient funds to initiate a transfer of "+amount);
                }

            }else {
                notSuccessDialog("Amount should be between 1 and "+transactionLimit);

            }

        }
    }

    private void makeTransfer(String otp){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Making funds transfer. Please wait...", true);

        if (sourceAccountType.equals("FOSA") && destinationAccountType.equals("FOSA")){
            authViewModel.transfer(sourceAccountCode,destinationAccountCode, finalAmount,
                    "FOSA",otp).observe(getViewLifecycleOwner(), listAPIResponse -> {
                progressDialog.dismiss();
                if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                    if (listAPIResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                        successDialog(listAPIResponse.body().description);
                        liveDataViewModel.setAccountBalancesBosaLiveData(null);
                        liveDataViewModel.setAccountBalancesFosaLiveData(null);
                        liveDataViewModel.setStatementResponseLiveData(null);
                    }else {
                        notSuccessDialog(listAPIResponse.body().description);

                    }
                }else {
                    notSuccessDialog("Unable to process your request at this time. Please try again later");

                }
            });
        }else {
            authViewModel.transfer(sourceAccountCode,destinationAccountCode, finalAmount,
                    "BOSA",otp).observe(getViewLifecycleOwner(), listAPIResponse -> {
                progressDialog.dismiss();
                if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                    if (listAPIResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                        successDialog(listAPIResponse.body().description);
                    }else {
                        notSuccessDialog(listAPIResponse.body().description);

                    }
                }else {
                    notSuccessDialog("Unable to process your request at this time. Please try again later");

                }
            });
        }

    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",3000);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    private void successDialog(String message){
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DISMISS && resultCode == Activity.RESULT_OK && data != null) {
            navigateUp();
        }else if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {
            makeTransfer(data.getStringExtra("otp"));
        }
    }

}