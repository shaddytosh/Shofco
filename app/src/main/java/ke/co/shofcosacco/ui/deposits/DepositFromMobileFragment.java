package ke.co.shofcosacco.ui.deposits;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentDepositFromMobileBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class DepositFromMobileFragment extends BaseFragment {

    private FragmentDepositFromMobileBinding binding;
    private AuthViewModel authViewModel;
    private String transactionLimit;
    private Handler handler;

    private String accountType = "1";
    private String accountTypeName;
    private String accountName;
    private String accountNo;




    public DepositFromMobileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDepositFromMobileBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());


        handler = new Handler(Looper.getMainLooper());
        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
        });

        transactionLimit = authViewModel.getTransactionLimit();

        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        setupValidationListeners();

        binding.txtAmount.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilAmount.setError(null);
                }
            }
        });


        accountName = DepositFromMobileFragmentArgs.fromBundle(getArguments()).getAccountName();
        accountNo = DepositFromMobileFragmentArgs.fromBundle(getArguments()).getAccountNo();

        binding.toolbar.setTitle("Deposit to "+accountNo);

        binding.cvMpesa.setOnClickListener(v->{
            binding.cvMpesa.setBackgroundColor(getResources().getColor(R.color.primary));
            binding.cvAirtel.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cvTelkom.setBackgroundColor(getResources().getColor(R.color.white));
            binding.tvMpesa.setTextColor(getResources().getColor(R.color.white));
            binding.tvAirtel.setTextColor(getResources().getColor(R.color.primary));
            binding.tvTelkom.setTextColor(getResources().getColor(R.color.primary));
            Vibrator vb = (Vibrator)   requireContext().getSystemService(Context.VIBRATOR_SERVICE);
            vb.vibrate(50);
            accountType = "1";
        });

//        binding.cvAirtel.setOnClickListener(v->{
//            binding.cvMpesa.setBackgroundColor(getResources().getColor(R.color.white));
//            binding.cvAirtel.setBackgroundColor(getResources().getColor(R.color.primary));
//            binding.cvTelkom.setBackgroundColor(getResources().getColor(R.color.white));
//            binding.tvMpesa.setTextColor(getResources().getColor(R.color.primary));
//            binding.tvAirtel.setTextColor(getResources().getColor(R.color.white));
//            binding.tvTelkom.setTextColor(getResources().getColor(R.color.primary));
//            Vibrator vb = (Vibrator)   requireContext().getSystemService(Context.VIBRATOR_SERVICE);
//            vb.vibrate(50);
//            accountType = "2";
//        });

//        binding.cvTelkom.setOnClickListener(v->{
//            binding.cvMpesa.setBackgroundColor(getResources().getColor(R.color.white));
//            binding.cvAirtel.setBackgroundColor(getResources().getColor(R.color.white));
//            binding.cvTelkom.setBackgroundColor(getResources().getColor(R.color.primary));
//            binding.tvMpesa.setTextColor(getResources().getColor(R.color.primary));
//            binding.tvAirtel.setTextColor(getResources().getColor(R.color.primary));
//            binding.tvTelkom.setTextColor(getResources().getColor(R.color.white));
//            Vibrator vb = (Vibrator)   requireContext().getSystemService(Context.VIBRATOR_SERVICE);
//            vb.vibrate(50);
//            accountType = "3";
//        });

        binding.radioSelf.setChecked(true);
        binding.phoneNumberEditTextForgotPassword.setText(String.format("0%s", authViewModel.getPhone().substring(authViewModel.getPhone().length() - 9)));
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioSelf){
                binding.phoneNumberEditTextForgotPassword.setText(String.format("0%s", authViewModel.getPhone().substring(authViewModel.getPhone().length() - 9)));
                validatesInput();
            }else  if (checkedId == R.id.radioOther){
                binding.phoneNumberEditTextForgotPassword.setText(null);
                binding.phoneNumberEditTextForgotPassword.setError(null);
            }
        });

        binding.tilSpinnerSourceAccount.setHint(accountNo);
        binding.spinnerSourceAccount.setText(accountName);
        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.tvSubmit.setOnClickListener(v -> validate());

        binding.phoneNumberInputLayoutForgotPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsDialogFragment dialogFragment= new ContactsDialogFragment();
                dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());

            }
        });

        return binding.getRoot();
    }

    private void validate(){
        String amount = binding.txtAmount.getText().toString();
        validatesInput();

        String formattedPhoneNumber =   binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","").replace("+","");

        if (TextValidator.isEmpty(amount)){
            binding.tilAmount.setError(getString(R.string.required));
        }else {
            switch (accountType) {
                case "1":
                    accountTypeName = "MPESA";
                    break;
                case "2":
                    accountTypeName = "Airtel Money";
                    break;
                case "3":
                    accountTypeName = "Telkom";
                    break;
            }

            amount = String.valueOf(Integer.parseInt(amount));

            double tAmount= Double.parseDouble(amount);
            double mTransactionLimit= Double.parseDouble(transactionLimit);

            if (tAmount >= 1 && tAmount <= mTransactionLimit) {
                String message = "You are about to deposit KES "+amount+" " +
                        "into "+accountName+"("+accountNo+") from "+accountTypeName+" number "+formattedPhoneNumber+"";

                submit(message, amount,formattedPhoneNumber);

            }else {
                notSuccessDialog("Amount should be between 1 and "+transactionLimit);

            }

        }
    }

    private void submit(String message, String amount, String formattedPhoneNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        builder.setCancelable(false);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView one =customView.findViewById(R.id.dialog_message);
        one.setText(message);
        AlertDialog alertDialog = builder.create();
        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                    "Initiating transaction. Please wait...", true);
            authViewModel.stkPush(accountNo,"+"+formattedPhoneNumber,amount).observe(getViewLifecycleOwner(), apiResponse -> {
                progressDialog.dismiss();
                if (apiResponse != null && apiResponse.isSuccessful()) {
                    if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                        successDialog(apiResponse.body().description);
                    } else {
                        notSuccessDialog(apiResponse.body().description);

                    }
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
    }


    private void setupValidationListeners() {
        binding.phoneNumberEditTextForgotPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                EditTextValidator.isValid(
                        binding.phoneNumberEditTextForgotPassword,
                        handler,
                        binding.phoneNumberInputLayoutForgotPassword,
                        s -> binding.ccpForgotPassword.isValidFullNumber(),
                        getString(R.string.error_phone_number)
                );
            }
        });
    }


    private boolean validatesInput() {
        return !EditTextValidator.isValid(
                binding.phoneNumberEditTextForgotPassword,
                s -> binding.ccpForgotPassword.isValidFullNumber(),
                binding.phoneNumberInputLayoutForgotPassword,
                getString(R.string.error_phone_number)
        );
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
        if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            String phone = data.getStringExtra("phoneNumber");

            if (phone != null && phone.length() > 9){
                binding.phoneNumberEditTextForgotPassword.setText(String.format("0%s", phone.substring(phone.length()  -9)));
            }
        }
    }



}