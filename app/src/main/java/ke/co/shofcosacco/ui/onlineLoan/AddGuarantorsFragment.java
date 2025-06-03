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
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentAddGuarantorsBinding;
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
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class AddGuarantorsFragment extends BaseFragment {

    private FragmentAddGuarantorsBinding binding;
    private AuthViewModel authViewModel;
    LoanProduct loanProduct;
    String period, amount;
    boolean requireGuarantors = false;

    double totalGuarantorAmount = 0.0;
    private GuarantorsAdapter guarantorsAdapter;
    private List<GuarantorNew> guarantorNewList = new ArrayList<>();

    public AddGuarantorsFragment() {
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
        binding = FragmentAddGuarantorsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        loanProduct = AddGuarantorsFragmentArgs.fromBundle(getArguments()).getLoanProduct();
        amount = AddGuarantorsFragmentArgs.fromBundle(getArguments()).getAmount();
        period = AddGuarantorsFragmentArgs.fromBundle(getArguments()).getPeriod();

        binding.tvTotalAmount.setText("Total amount required is KES "+amount);

        binding.tvAddGuarantors.setOnClickListener(v -> {
            ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
            Bundle args = new Bundle();
            args.putBoolean("isResetPin", false);
            args.putBoolean("isValidateGuarantor", true);
            dialogFragment.setArguments(args);
            dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());

        });

        binding.tvProceed.setOnClickListener(view -> validate(loanProduct));


        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (AddGuarantorsFragment.this.getParentFragment() != null && AddGuarantorsFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) AddGuarantorsFragment.this.getParentFragment()).navigateToHome();
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


        guarantorsAdapter = new GuarantorsAdapter(requireContext(), guarantorNewList, position -> {
            // Remove image at the given position
            guarantorNewList.remove(position);
            guarantorsAdapter.notifyItemRemoved(position);
        });

        binding.recyclerGuarantors.setAdapter(guarantorsAdapter);
        binding.recyclerGuarantors.setLayoutManager(new LinearLayoutManager(requireContext()));


        return binding.getRoot();

    }

    private void validate(LoanProduct loanProduct) {

        getGuarantors();

        double mTotalGuarantorAmount = Double.parseDouble(totalGuarantorAmount + amount);

        LoanApplicationRequest.Guarantors[] guarantors = getGuarantors().toArray(new LoanApplicationRequest.Guarantors[0]);

        if (mTotalGuarantorAmount >=  Double.parseDouble(amount)) {

            String message = "You have added "+guarantors.length+" with total free deposits of "+totalGuarantorAmount;

          navigate(AddGuarantorsFragmentDirections.actionAddGuarantorsToApplyOnlineLoan(loanProduct,guarantors,message,period,amount));
        }else {
            notSuccessDialog("Total amount covered by your guarantors is less than the applied amount. Please add more guarantors");
        }

    }


    private List<LoanApplicationRequest.Guarantors> getGuarantors() {
        List<LoanApplicationRequest.Guarantors> guarantorsList = new ArrayList<>();


        for (GuarantorNew guarantorNew : guarantorNewList) {
            // Parse the free deposit string to double
            double freeDeposit = 0.0;
            try {
                String cleanValue = guarantorNew.getFreeDeposits().replace(",", "").trim();
                freeDeposit = Double.parseDouble(cleanValue);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // log error or handle invalid input
            }

            // Create the guarantor object
            LoanApplicationRequest.Guarantors guarantors = new LoanApplicationRequest.Guarantors(
                    guarantorNew.getMemberNo(),
                    guarantorNew.getFreeDeposits().replace(",", "")
            );

            totalGuarantorAmount += freeDeposit;
            guarantorsList.add(guarantors);
        }


        binding.tvTotalAmount.setText("Total amount required is KES "+amount+"\nFree Deposits: "+totalGuarantorAmount);

        return guarantorsList; // Return the list
    }


    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",3000);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SUCCESS && resultCode == Activity.RESULT_OK && data != null) {
            navigateUp();
        }else if (requestCode == 2000 && resultCode == Activity.RESULT_OK && data != null) {

            String newMemberNo = data.getStringExtra("member_no");

            boolean alreadyExists = false;
            for (GuarantorNew g : guarantorNewList) {
                if (g.getMemberNo().equals(newMemberNo)) {
                    alreadyExists = true;
                    break;
                }
            }

            if (!alreadyExists) {
                guarantorNewList.add(new GuarantorNew(
                        data.getStringExtra("member_name"),
                        newMemberNo,
                        data.getStringExtra("free_deposits")
                ));
                guarantorsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(requireContext(), "Guarantor already added", Toast.LENGTH_SHORT).show();
            }

        }

    }

}