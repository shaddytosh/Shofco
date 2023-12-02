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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentApplyLoanBinding;
import ke.co.shofcosacco.app.models.Eligibility;
import ke.co.shofcosacco.app.models.LoanApplication;
import ke.co.shofcosacco.app.models.LoanProduct;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class ApplyLoanFragment extends BaseFragment {

    private FragmentApplyLoanBinding binding;
    private AuthViewModel authViewModel;
    LoanProduct mLoanProduct;
    double mEnteredAmount;
    double mPeriod;

    public ApplyLoanFragment() {
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
        binding = FragmentApplyLoanBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.txtAmount.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilAmount.setError(null);
                }
            }
        });

        binding.txtPeriod.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilPeriod.setError(null);
                }
            }
        });

        LoanProduct loanProduct = ApplyLoanFragmentArgs.fromBundle(getArguments()).getLoanProduct();

        binding.layoutOne.installmentPeriod.setText(String.format("Installment Period: %s Months", loanProduct.getInstallmentPeriod()));
        binding.layoutOne.maximumLoanAmt.setText(String.format("Maximum Amount: KES %s", loanProduct.getMaximumLoanAmt()));
        binding.layoutOne.repaymentMethod.setText(String.format("Repayment Method: %s", loanProduct.getRepaymentMethod()));
        binding.layoutOne.productDescription.setText(loanProduct.getProductDescription());
        binding.layoutOne.interest.setText(String.format("Interest: %s%%", loanProduct.getInterest()));

        binding.tvApplyLoan.setOnClickListener(view -> view(loanProduct));

        return binding.getRoot();

    }

    private void view(LoanProduct loanProduct) {
        String amount= binding.txtAmount.getText().toString().trim();
        String period= binding.txtPeriod.getText().toString().trim();

        if (TextValidator.isEmpty(amount)) {
            binding.tilAmount.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(period)) {
            binding.tilPeriod.setError(getString(R.string.required));
        }else {
            double enteredAmount= Double.parseDouble(amount);
            double maxLimit= Double.parseDouble(loanProduct.getMaximumLoanAmt());
            double _period= Double.parseDouble(period);
            double mPeriod= Double.parseDouble(loanProduct.getInstallmentPeriod());
            int integerPeriod = (int) mPeriod;
            int integerMaxLimit = (int) maxLimit;

            if (enteredAmount >= 1 && enteredAmount <= maxLimit) {

                if (_period >= 1 && _period <= mPeriod ) {
                    checkEligibility(loanProduct, enteredAmount, _period);
                }else {
                    notSuccessDialog("Period should be between 1 and "+integerPeriod+" Months");
                }
            }else {
                notSuccessDialog("Amount should be between KES 1 and "+integerMaxLimit);
            }
        }
    }

    private void checkEligibility(LoanProduct loanProduct, double enteredAmount, double period) {
         mLoanProduct = loanProduct;
         mEnteredAmount = enteredAmount;
         mPeriod =period;
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Checking eligibility. Please wait...", true);
        int integerPeriod = (int) period;
        authViewModel.loanEligibility(loanProduct.getProductCode(), String.valueOf(integerPeriod)).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                Eligibility eligibility = apiResponse.body();
                if (eligibility.status.equals("true")) {
                    double qualifiedAmount= Double.parseDouble(eligibility.amount);
                    int integerQualifiedAmount = (int) qualifiedAmount;

                    if (enteredAmount <= qualifiedAmount){
                        int integerEnteredAmount = (int) enteredAmount;
                        applyLoan(loanProduct, integerEnteredAmount, integerPeriod);
                    }else {
                        notSuccessDialog("You qualify up to KES "+integerQualifiedAmount);
                    }
                }else {
                    notSuccessDialog(apiResponse.body().description);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }

    private void applyLoan(LoanProduct loanProduct, double enteredAmount, double period) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_apply_loan, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.one);
        TextView two =customView.findViewById(R.id.two);
        TextView three =customView.findViewById(R.id.three);
        TextView four =customView.findViewById(R.id.four);

        one.setText(String.format("Loan Product: %s", loanProduct.getProductDescription()));
        two.setText("Applied Amount: KES "+(int) enteredAmount);
        three.setText("Installment Period: "+ (int) period +" Months");
        four.setText(String.format("Repayment Method: %s", loanProduct.getRepaymentMethod()));

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

    private void applyNow(String otp){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Applying loan. Please wait...", true);
        authViewModel.loanApplication(mLoanProduct.getProductCode(), String.valueOf(mPeriod),
                String.valueOf(mEnteredAmount),otp).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)){
                    LoanApplication loanApplication = apiResponse.body().loanApplication;
                    if (loanApplication.status.equals("true")) {
                        successDialog(apiResponse.body().statusDesc);
                    }else {
                        notSuccessDialog(apiResponse.body().statusDesc);
                    }
                }else {
                    notSuccessDialog(apiResponse.body().statusDesc);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
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
            applyNow(data.getStringExtra("otp"));
        }
    }
}