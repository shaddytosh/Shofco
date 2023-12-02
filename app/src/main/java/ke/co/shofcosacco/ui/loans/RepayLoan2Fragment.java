package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.SUCCESS;
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

import com.google.android.material.tabs.TabLayout;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRepayLoan2Binding;
import ke.co.shofcosacco.app.models.LoanBalance;
import ke.co.shofcosacco.app.models.SourceAccount;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class RepayLoan2Fragment extends BaseFragment {

    private FragmentRepayLoan2Binding binding;
    private AuthViewModel authViewModel;
    private String paymentType = "WALLET";
    private boolean isUpdatingText = false;

    private String sourceAccountNumber;

    private List<SourceAccount> sourceAccounts;
    private  ArrayAdapter<SourceAccount> sourceAccountAdapter;

    private LoanBalance loanBalance;

    private String finalAmount;



    public RepayLoan2Fragment() {
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
        binding = FragmentRepayLoan2Binding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        loanBalance = RepayLoan2FragmentArgs.fromBundle(getArguments()).getLoanBalance();

        // Set up the CheckBox listener
        binding.checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (!isUpdatingText) {
                if (isChecked) {
                    String cleanLoanAmountStr = loanBalance.getLoanBalance().replace(",", "");
                    double loanAmount = Double.parseDouble(cleanLoanAmountStr);
                    binding.txtAmount.setText(String.valueOf(loanAmount));
                } else {
                    binding.txtAmount.setText(null);
                }
            }
        });

        // Set up the EditText text change listener
        binding.txtAmount.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (!isUpdatingText && s != null) {
                    binding.tilAmount.setError(null);
                    isUpdatingText = true;
                    binding.checkBox.setChecked(s.equals(loanBalance.getLoanBalance().replace(",", "")));
                    isUpdatingText = false;
                }
            }
        });
        binding.txtPhone.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilPhone.setError(null);
                }
            }
        });

        binding.layoutOne.tvRepay.setVisibility(View.GONE);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FROM WALLET"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FROM MPESA"));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Do something when a tab is selected
                if (tab.getPosition() == 0){
                    paymentType = "WALLET";
                    binding.tilPhone.setVisibility(View.GONE);
                    binding.txtPhone.setText(null);
                    binding.tilSpinnerSourceAccount.setVisibility(View.VISIBLE);
                }else {
                    paymentType = "MPESA";
                    binding.tilPhone.setVisibility(View.VISIBLE);
                    binding.txtPhone.setText(String.format("0%s", authViewModel.getPhone().substring(authViewModel.getPhone().length() - 9)));
                    binding.tilSpinnerSourceAccount.setVisibility(View.GONE);

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Do something when a tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Do something when a tab is reselected
            }
        });



        binding.layoutOne.tvLoanName.setText(loanBalance.getLoanName());
        binding.layoutOne.tvLoanBalance.setText(String.format("%s %s %s", "Balance: ",Constants.CURRENCY, loanBalance.getLoanBalance()));
        binding.layoutOne.tvLoanInstallments.setText(String.format("%s %s %s", "Installments: ",Constants.CURRENCY, loanBalance.getLoanInstallment()));
        binding.layoutOne.tvLoanApplicationDate.setText(String.format("Application Date: %s", loanBalance.getLoanApplicationDate()));
        binding.layoutOne.tvLoanIssueDate.setText(String.format("Issue Date: %s", loanBalance.getLoanApplicationDate()));

        getSourceAccount();

        binding.tvRepayLoan.setOnClickListener(view -> view(loanBalance));



        binding.spinnerSourceAccount.setOnItemClickListener((parent, view, position, id) -> {
            if (sourceAccounts != null && sourceAccounts.size() > 0) {
                sourceAccountNumber = sourceAccountAdapter.getItem(position).getAccountNo();
                binding.tilSpinnerSourceAccount.setHint(sourceAccounts.get(0).toString());
            }
        });

        return binding.getRoot();

    }

    private void view(LoanBalance loanBalance) {
        String amount= binding.txtAmount.getText().toString().trim();
        String phone= binding.txtPhone.getText().toString().trim();

        String cleanLoanAmountStr = loanBalance.getLoanBalance().replace(",", "");

        double loanAmount = Double.parseDouble(cleanLoanAmountStr);

        // Create a DecimalFormat object to round off to 2 decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String roundedLoanAmountStr = decimalFormat.format(loanAmount);

        double maxLimit = Double.parseDouble(roundedLoanAmountStr);

        if (paymentType.equals("MPESA")){

            if (TextValidator.isEmpty(amount)) {
                binding.tilAmount.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(phone)) {
                binding.tilPhone.setError(getString(R.string.required));
            }else {
                if (phone.startsWith("07") || phone.startsWith("01") && phone.length() == 10){
                    double enteredAmount= Double.parseDouble(amount);

                    if (enteredAmount >= 1 && enteredAmount <= maxLimit) {

                        transactionCost(loanBalance, String.valueOf(enteredAmount), phone, paymentType);
                    }else {
                        notSuccessDialog("Amount should be between KES 1 and "+maxLimit);
                    }
                }else {
                    binding.tilPhone.setError("Invalid MPESA number. Start with 07... or 01...");
                }
            }
        }else {
            if (TextValidator.isEmpty(amount)) {
                binding.tilAmount.setError(getString(R.string.required));
            }else {
                double enteredAmount= Double.parseDouble(amount);

                if (enteredAmount >= 1 && enteredAmount <= maxLimit) {

                   if (sourceAccountNumber != null){
                       transactionCost(loanBalance, String.valueOf(enteredAmount), phone, paymentType);
                       binding.tilSpinnerSourceAccount.setError(null);
                   }else {
                       binding.tilSpinnerSourceAccount.setError("Please select source account");
                   }
                }else {
                    notSuccessDialog("Amount should be between KES 1 and "+maxLimit);
                }
            }
        }

    }

    private void transactionCost(LoanBalance loanBalance, String amount, String phone, String paymentType){

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Fetching transaction cost. Please wait...", true);
        authViewModel.transactionCost(paymentType.equals("WALLET") ? Constants.LOAN_REPAYMENT : Constants.MPESA,amount).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess1 = apiResponse != null && apiResponse.isSuccessful() ;
            if (isSuccess1) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS) ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                    View customView = getLayoutInflater().inflate(R.layout.custom_dialog_transaction_cost, null);
                    builder.setView(customView);
                    TextView one =customView.findViewById(R.id.one);
                    TextView two =customView.findViewById(R.id.two);
                    if (paymentType.equals("WALLET")) {
                        one.setText(getString(R.string.you_are_about_to_repay_your_loan_from_wallet));
                    }else {
                        one.setText(getString(R.string.you_are_about_to_repay_your_loan_from_mpesa));
                    }

                    two.setText(String.format("Transaction cost, KES %s", apiResponse.body().charges));
                    AlertDialog alertDialog = builder.create();
                    builder.setCancelable(false);

                    TextView positiveButton = customView.findViewById(R.id.positive_button);
                    positiveButton.setOnClickListener(v -> {
                        alertDialog.dismiss();
                        if (paymentType.equals("WALLET")){
                            repayFromWallet(loanBalance, amount);
                        }else {
                            repayFromMpesa(loanBalance, amount, phone);
                        }
                    });

                    TextView negativeButton = customView.findViewById(R.id.negative_button);
                    negativeButton.setOnClickListener(v -> {
                        // perform negative action
                        alertDialog.dismiss();
                        navigateUp();


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
    }


    private void repayFromWallet(LoanBalance loanBalance, String amount){

        finalAmount = amount;
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.dialog_message);

        one.setText(MessageFormat.format("You are about to repay KES {0} of your {1} loan from Your Wallet", amount, loanBalance.getLoanName()));
        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            alertDialog.dismiss();

            ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "",
                    "Requesting OTP. Please wait...", true);
            authViewModel.sendOtp(authViewModel.getMemberNo(),"REPAY LOAN").observe(getViewLifecycleOwner(), apiResponse1 -> {
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

        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });

        alertDialog.show();
    }

    private void makeRepaymentFromWallet(String otp) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Repaying loan. Please wait...", true);
        authViewModel.repayLoan(sourceAccountNumber,finalAmount, loanBalance.getLoanNo(),otp).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    successDialog(apiResponse.body().statusDesc);
                }else {
                    notSuccessDialog(apiResponse.body().statusDesc);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }
    private void repayFromMpesa(LoanBalance loanBalance, String amount, String phone){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.dialog_message);

        one.setText(String.format("You are about to repay KES %s of your %s loan from MPESA %s", amount, loanBalance.getLoanName(), phone));
        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                    "Repaying loan. Please wait...", true);
            authViewModel.stkPush(loanBalance.getLoanNo(),"+254"+phone.substring(1),amount).observe(getViewLifecycleOwner(), apiResponse -> {
                progressDialog.dismiss();
                boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
                if (isSuccess) {
                    if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                        successDialog(apiResponse.body().description);
                    }else {
                        successDialog(apiResponse.body().description);

                    }
                }else {
                    notSuccessDialog(Constants.API_ERROR);
                }
            });
        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });

        alertDialog.show();
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
        args.putInt("type",SUCCESS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUCCESS && resultCode == Activity.RESULT_OK && data != null) {
            navigateUp();
        }else if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {
            makeRepaymentFromWallet(data.getStringExtra("otp"));
        }
    }


    private void getSourceAccount(){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);
        authViewModel.getSourceAccount().observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    sourceAccounts = listAPIResponse.body().sourceAccounts;
                    if (sourceAccounts != null && sourceAccounts.size() > 0){
                        binding.tilSpinnerSourceAccount.setHint(sourceAccounts.get(0).getAccountNo());
                        binding.spinnerSourceAccount.setText(sourceAccounts.get(0).toString());
                        sourceAccountNumber = sourceAccounts.get(0).getAccountNo();
                        sourceAccountAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sourceAccounts);

                        binding.spinnerSourceAccount.setAdapter(sourceAccountAdapter);

                        sourceAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }

                }
            }
        });
    }


}