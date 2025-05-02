package ke.co.shofcosacco.ui.transafer;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_EXPIRED_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentSendToMobileBinding;
import ke.co.shofcosacco.app.models.SourceAccount;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.deposits.ContactsDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class SendToMobileFragment extends BaseFragment {

    private FragmentSendToMobileBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;


    private List<SourceAccount> sourceAccounts;
    private  ArrayAdapter<SourceAccount> sourceAccountAdapter;

    private String sourceAccountNumber;
    private String sourceAccountName;
    private String sourceBalance;
    private String transactionLimit;
    private Handler handler;

    private String accountType = "1";
    private String accountTypeName;




    public SendToMobileFragment() {
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
        binding = FragmentSendToMobileBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());


        handler = new Handler(Looper.getMainLooper());
        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            getSourceAccount();
            binding.swipeToRefresh.setRefreshing(false);
        });

        transactionLimit = SendToMobileFragmentArgs.fromBundle(getArguments()).getLimit().replace(",","");

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

        getSourceAccount();

        binding.spinnerSourceAccount.setOnItemClickListener((parent, view, position, id) -> {
            if (sourceAccounts != null && sourceAccounts.size() > 0) {
                sourceAccountNumber = sourceAccountAdapter.getItem(position).getAccountNo();
                sourceBalance = sourceAccountAdapter.getItem(position).balance.replace(",","");
                sourceAccountName = sourceAccountAdapter.getItem(position).toString();
                binding.tilSpinnerSourceAccount.setHint(sourceAccounts.get(0).getAccountNo());
            }
        });

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
//
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
        binding.phoneNumberEditTextForgotPassword.setText(authViewModel.getPhone() != null ? String.format("0%s", authViewModel.getPhone().substring( authViewModel.getPhone().length()  -9)) : null);
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if (checkedId == R.id.radioSelf){
                binding.phoneNumberEditTextForgotPassword.setText(authViewModel.getPhone() != null ? String.format("0%s", authViewModel.getPhone().substring( authViewModel.getPhone().length()  -9)) : null);
                validatesInput();
            }else  if (checkedId == R.id.radioOther){
                binding.phoneNumberEditTextForgotPassword.setText(null);
                binding.phoneNumberEditTextForgotPassword.setError(null);
            }
        });

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        binding.tvSubmit.setOnClickListener(v -> submit());

        binding.phoneNumberInputLayoutForgotPassword.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsDialogFragment dialogFragment= new ContactsDialogFragment();
                dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());

            }
        });

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> navigateUp());
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Dismiss the dialog if "No" is clicked
                    dialogInterface.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();
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
                        sourceBalance = sourceAccounts.get(0).getBalance().replace(",","");
                        sourceAccountName = sourceAccounts.get(0).toString();

                        sourceAccountAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sourceAccounts);

                        binding.spinnerSourceAccount.setAdapter(sourceAccountAdapter);

                        sourceAccountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        binding.llLoading.setVisibility(View.GONE);
                        binding.llData.setVisibility(View.GONE);
                        binding.llEmpty.setVisibility(View.VISIBLE);

                    }

                }else if (listAPIResponse.body().statusCode.equals(STATUS_CODE_INVALID_TOKEN) ||
                        listAPIResponse.body().statusCode.equals(STATUS_CODE_INVALID_EXPIRED_TOKEN)) {
                    try {
                        authViewModel.removeLoggedInUser();
                        Toast.makeText(requireContext(), "Session expired! Please login again", Toast.LENGTH_SHORT).show();
                        Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } catch (GeneralSecurityException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    binding.llLoading.setVisibility(View.GONE);
                    binding.llData.setVisibility(View.GONE);
                    binding.llEmpty.setVisibility(View.VISIBLE);

                }
            }else {
                binding.llLoading.setVisibility(View.GONE);
                binding.llData.setVisibility(View.GONE);
                binding.llEmpty.setVisibility(View.VISIBLE);
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }


    private void submit(){
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


            double sBalance= Double.parseDouble(sourceBalance);
            double tAmount= Double.parseDouble(amount);
            double mTransactionLimit= Double.parseDouble(transactionLimit);

            if (tAmount >= 1 && tAmount <= mTransactionLimit) {
                if ((sBalance > tAmount)){

                    String message = String.format("You are about to withdraw KES %s from %s to %s number " +
                            "%s\nDo you want to proceed?", amount, sourceAccountName, accountTypeName, formattedPhoneNumber);

                    transactionCost(message, String.valueOf(tAmount),formattedPhoneNumber);


                }else {
                    notSuccessDialog("A/C "+sourceAccountName+" has insufficient funds to initiate money transfer of "+amount);
                }

            }else {
                notSuccessDialog("Amount should be between 1 and "+transactionLimit);

            }

        }
    }

    private void transactionCost(String message, String amount,String phoneNumber){

        liveDataViewModel.setAccountBalancesBosaLiveData(null);
        liveDataViewModel.setAccountBalancesFosaLiveData(null);

        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Processing. Please wait...", true);
        authViewModel.transactionCost(Constants.MPESA, amount).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            boolean isSuccess1 = apiResponse != null && apiResponse.isSuccessful() ;
            if (isSuccess1) {
                if (apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                    builder.setCancelable(false);
                    View customView = getLayoutInflater().inflate(R.layout.custom_dialog_transaction_cost, null);
                    builder.setView(customView);
                    TextView one =customView.findViewById(R.id.one);
                    TextView two =customView.findViewById(R.id.two);
                    one.setText(message);
                    two.setText(String.format("Transaction cost, KES %s", apiResponse.body().charges));
                    AlertDialog alertDialog = builder.create();
                    TextView positiveButton = customView.findViewById(R.id.positive_button);
                    positiveButton.setOnClickListener(v -> {

                        OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("sourceAccountNumber",sourceAccountNumber);
                        args.putString("amount",amount);
                        args.putString("phoneNumber",phoneNumber);
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
                }else {
                    notSuccessDialog(Constants.API_ERROR);

                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }

    private void sendToMobile(String sourceAccountNumber, String amount, String phoneNumber, String otp){
        ProgressDialog progressDialog2 = ProgressDialog.show(getContext(), "",
                "Initiating transaction. Please wait...", true);
        authViewModel.sendToMobile(sourceAccountNumber,amount,"+"+phoneNumber, otp).observe(getViewLifecycleOwner(), apiResponse2 -> {
            progressDialog2.dismiss();
            boolean isSuccess = apiResponse2 != null && apiResponse2.isSuccessful();
            if (isSuccess) {
                if (apiResponse2.body().statusCode.equals(STATUS_CODE_SUCCESS)){
                    successDialog(apiResponse2.body().statusDesc);
                }else {
                    notSuccessDialog(apiResponse2.body().statusDesc);
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
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
        if (requestCode == DISMISS && resultCode == Activity.RESULT_OK && data != null) {
            navigateUp();
        }else  if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {
            sendToMobile(data.getStringExtra("sourceAccountNumber"),data.getStringExtra("amount"),
                    data.getStringExtra("phoneNumber"),data.getStringExtra("otp"));
        }else if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            String phone = data.getStringExtra("phoneNumber");

            if (phone != null && phone.length() > 9){
                binding.phoneNumberEditTextForgotPassword.setText(String.format("0%s", phone.substring(phone.length()  -9)));
            }
        }
    }




}