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
import ke.co.shofcosacco.ui.main.MainFragment;
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
        binding.layoutOne.tvApply.setVisibility(View.GONE);

        binding.tvApplyLoan.setOnClickListener(view -> view(loanProduct));

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (ApplyLoanFragment.this.getParentFragment() != null && ApplyLoanFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) ApplyLoanFragment.this.getParentFragment()).navigateToHome();
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

        double mPeriod= Double.parseDouble(loanProduct.getInstallmentPeriod());

        if (mPeriod == 1){
            binding.txtPeriod.setText("1");
            binding.txtPeriod.setEnabled(false);
        }

        binding.tvCheckEligibility.setOnClickListener(view -> viewNew(loanProduct));
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


    private void viewNew(LoanProduct loanProduct) {
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
                    checkEligibilityNew(loanProduct, enteredAmount, _period);
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
                "Processing. Please wait...", true);
        int integerPeriod = (int) period;
        authViewModel.loanEligibility(loanProduct.getProductCode(), String.valueOf(integerPeriod), String.valueOf(mEnteredAmount)).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                Eligibility eligibility = apiResponse.body();
                if (eligibility.status.equals("true")) {
                    double qualifiedAmount= Double.parseDouble(eligibility.amount);
                    int integerQualifiedAmount = (int) qualifiedAmount;
                    int integerEnteredAmount = (int) enteredAmount;

                    if (eligibility.reguireGuarantors.equals("true")) {
                        navigate(ApplyLoanFragmentDirections.actionApplyLoanToAddGuarantors(loanProduct,
                                String.valueOf(enteredAmount).replace(".0",""),String.valueOf(mPeriod).replace(".0","")));

                    }else {
                        if (enteredAmount <= qualifiedAmount){

                            applyLoan(loanProduct, integerEnteredAmount, integerPeriod, eligibility.description);

                        }else {
                            notSuccessDialog("You qualify up to KES "+integerQualifiedAmount);
                        }
                    }


                }else {
                    notSuccessDialog(apiResponse.body().description);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }


    private void checkEligibilityNew(LoanProduct loanProduct, double enteredAmount, double period) {
        mLoanProduct = loanProduct;
        mEnteredAmount = enteredAmount;
        mPeriod =period;
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Checking eligibility. Please wait...", true);
        int integerPeriod = (int) period;
        authViewModel.loanEligibility(loanProduct.getProductCode(), String.valueOf(integerPeriod), String.valueOf(mEnteredAmount)).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                Eligibility eligibility = apiResponse.body();
                if (eligibility.status.equals("true")) {
                    binding.tvApplyLoan.setVisibility(View.VISIBLE);

                    if (eligibility.reguireGuarantors.equals("true")){
                        notSuccessDialog("Loan Product: "+loanProduct.getProductDescription()+"\n\nQualification: "+eligibility.description+"" +
                                "\nApplied Amount: KES "+(int) enteredAmount+"" +
                                ""+String.format("\nInstallment Period: %d Months", (int) period)+"" +
                                ""+"\nRepayment Method: "+loanProduct.getRepaymentMethod());

                    }else {
                        successDialog("Loan Product: "+loanProduct.getProductDescription()+"\n\nQualification: "+eligibility.description+"" +
                                "\nApplied Amount: KES "+(int) enteredAmount+"" +
                                ""+String.format("\nInstallment Period: %d Months", (int) period)+"" +
                                ""+"\nRepayment Method: "+loanProduct.getRepaymentMethod(),9000);

                    }


                }else {
                    notSuccessDialog(apiResponse.body().description);
                    binding.tvApplyLoan.setVisibility(View.GONE);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }
    private void applyLoan(LoanProduct loanProduct, double enteredAmount, double period,String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_apply_loan, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.one);
        TextView two =customView.findViewById(R.id.two);
        TextView three =customView.findViewById(R.id.three);
        TextView four =customView.findViewById(R.id.four);

        one.setText("Loan Product: "+loanProduct.getProductDescription()+"\n\nQualification: "+description);
        two.setText("Applied Amount: KES "+(int) enteredAmount);
        three.setText(String.format("Installment Period: %d Months", (int) period));
        four.setText("Repayment Method: "+loanProduct.getRepaymentMethod());

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {

            OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putString("mOTP", authViewModel.getMPIN());
            dialogFragment.setArguments(args);
            dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());

            alertDialog.dismiss();

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

        int integerPeriod = (int) mPeriod;
        authViewModel.loanApplication(mLoanProduct.getProductCode(), String.valueOf(integerPeriod),
                String.valueOf(mEnteredAmount),otp, null, false).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)){
                    if (apiResponse.body().status.equals(STATUS_CODE_SUCCESS)) {
                        successDialog(apiResponse.body().statusDesc,SUCCESS);
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

    private void successDialog(String message, int type){
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",type);
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