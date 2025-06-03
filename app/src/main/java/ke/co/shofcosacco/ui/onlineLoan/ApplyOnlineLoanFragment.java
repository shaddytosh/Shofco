package ke.co.shofcosacco.ui.onlineLoan;


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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentApplyLoanBinding;
import co.ke.shofcosacco.databinding.FragmentApplyOnlineLoanBinding;
import ke.co.shofcosacco.app.api.requests.LoanApplicationRequest;
import ke.co.shofcosacco.app.models.Eligibility;
import ke.co.shofcosacco.app.models.GuarantorNew;
import ke.co.shofcosacco.app.models.LoanProduct;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.auth.ValidateDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.loans.ApplyLoanFragmentArgs;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class ApplyOnlineLoanFragment extends BaseFragment {

    private FragmentApplyOnlineLoanBinding binding;
    private AuthViewModel authViewModel;
    LoanProduct loanProduct;

    String period, amount, message;

    private List<LoanApplicationRequest.Guarantors> guarantors;

    public ApplyOnlineLoanFragment() {
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
        binding = FragmentApplyOnlineLoanBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        loanProduct = ApplyOnlineLoanFragmentArgs.fromBundle(getArguments()).getLoanProduct();
        period = ApplyOnlineLoanFragmentArgs.fromBundle(getArguments()).getPeriod();
        amount = ApplyOnlineLoanFragmentArgs.fromBundle(getArguments()).getAmount();
        message = ApplyOnlineLoanFragmentArgs.fromBundle(getArguments()).getMessage();

        LoanApplicationRequest.Guarantors[] getGuarantorsList = ApplyOnlineLoanFragmentArgs.fromBundle(getArguments()).getGuarantors();
         guarantors = Arrays.asList(getGuarantorsList);

        binding.txtAmount.setText(amount);
        binding.txtPeriod.setText(period);
        binding.tvGuarantors.setText(message);

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



        binding.layoutOne.installmentPeriod.setText(String.format("Installment Period: %s Months", loanProduct.getInstallmentPeriod()));
        binding.layoutOne.maximumLoanAmt.setText(String.format("Maximum Amount: KES %s", loanProduct.getMaximumLoanAmt()));
        binding.layoutOne.repaymentMethod.setText(String.format("Repayment Method: %s", loanProduct.getRepaymentMethod()));
        binding.layoutOne.productDescription.setText(loanProduct.getProductDescription());
        binding.layoutOne.interest.setText(String.format("Interest: %s%%", loanProduct.getInterest()));
        binding.layoutOne.tvApply.setVisibility(View.GONE);

        binding.tvApplyLoan.setOnClickListener(view -> applyLoan());

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (ApplyOnlineLoanFragment.this.getParentFragment() != null && ApplyOnlineLoanFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) ApplyOnlineLoanFragment.this.getParentFragment()).navigateToHome();
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

    private void applyLoan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_apply_loan, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.one);
        TextView two =customView.findViewById(R.id.two);
        TextView three =customView.findViewById(R.id.three);
        TextView four =customView.findViewById(R.id.four);

        one.setText("Loan Product: "+loanProduct.getProductDescription());
        two.setText("Applied Amount: KES "+ amount);
        three.setText(String.format("Installment Period: %d Months", period));
        four.setText("Repayment Method: "+loanProduct.getRepaymentMethod()+"\n"+message);

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

    private void applyNow(String otp,  List<LoanApplicationRequest.Guarantors> guarantorsList){



        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Applying loan. Please wait...", true);

        authViewModel.loanApplication(loanProduct.getProductCode(), String.valueOf(period),
                String.valueOf(amount),otp,guarantorsList,true).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)){
                    successDialog(apiResponse.body().statusDesc,SUCCESS);
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
            navigate(ApplyOnlineLoanFragmentDirections.actionApplyOnlineLoanToLoanProducts());
        }else if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {

               applyNow(data.getStringExtra("otp"), guarantors);

        }

    }

}