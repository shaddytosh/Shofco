package ke.co.shofcosacco.ui.onboarding;

import static android.app.Activity.RESULT_OK;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogOtpConfirmationBinding;
import co.ke.shofcosacco.databinding.DialogStkPushBinding;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;


public class StkPushDialogFragment extends DialogFragment {

    private static final int REQ_USER_CONSENT = 1500;

    private DialogStkPushBinding binding;
    private AuthViewModel authViewModel;
    private String message, amount, phoneNumber,idNo;
    private Handler handler;

    public StkPushDialogFragment() {
        // Required empty public constructor
    }
    public static StkPushDialogFragment newInstance() {
        StkPushDialogFragment dialogFragment = new StkPushDialogFragment();
        return dialogFragment;

    }

   public static void show (FragmentManager fragmentManager){
        newInstance().show(fragmentManager,"paymentTrueDialogFragment");
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(requireParentFragment()).get(AuthViewModel.class);
        setStyle(STYLE_NORMAL, R.style.RoundedCornersDialog);
        setCancelable(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogStkPushBinding.inflate(getLayoutInflater());
        handler = new Handler(Looper.getMainLooper());

        if (getArguments() != null) {
            message = getArguments().getString("message");
            amount = getArguments().getString("amount");
            phoneNumber = getArguments().getString("phoneNumber");
            idNo = getArguments().getString("idNo");

        }

        binding.title.setText(message);

        binding.ivClose.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            Intent result = new Intent();
            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(1200, RESULT_OK, result);
            }
            dismiss();
        });

        binding.tvVerifyOtp.setOnClickListener(v -> verifyOtp());
        binding.close.setOnClickListener(v -> {
            Intent result = new Intent();
            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(1200, RESULT_OK, result);
            }
            dismiss();
        });
        binding.phoneNumberEditTextForgotPassword.setText((phoneNumber != null && phoneNumber.length() >= 10) ? String.format("0%s",phoneNumber.substring( phoneNumber.length()  -9)) : null);

        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        setupValidationListeners();


        return binding.getRoot();

    }

    private void verifyOtp(){

        String telephone= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","");


      if (TextValidator.isEmpty(telephone)) {
            binding.phoneNumberInputLayoutForgotPassword.setError(getString(R.string.required));
        }else {

          validatesInput();
          AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
          builder.setCancelable(false);
          View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
          builder.setView(customView);
          TextView textView =customView.findViewById(R.id.dialog_message);
          textView.setText("You are about to initiate an STK Push to "+telephone);

          AlertDialog alertDialog = builder.create();

          TextView positiveButton = customView.findViewById(R.id.positive_button);
          positiveButton.setOnClickListener(v -> {
              ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                      "Processing. Please wait...", true);
              authViewModel.stkPush(idNo,telephone,amount).observe(getViewLifecycleOwner(), apiResponse -> {
                  progressDialog.dismiss();
                  if (apiResponse != null && apiResponse.isSuccessful()) {
                      if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                          successDialog(apiResponse.body().description);
                          InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                          imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                      } else {
                          notSuccessDialog(apiResponse.body().description);
                      }
                  } else {

                      Toast.makeText(requireContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();

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

    @Override
    public void onResume()
    {
        super.onResume();
        binding.phoneNumberEditTextForgotPassword.post(() -> {
            binding.phoneNumberEditTextForgotPassword.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager)binding.phoneNumberHolderForgotpassword.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(binding.phoneNumberEditTextForgotPassword, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        InputMethodManager imm =
                (InputMethodManager)binding.phoneNumberHolderForgotpassword.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        super.onDismiss(dialog);
    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",1100);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    private void successDialog(String message){
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",1200);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }


}