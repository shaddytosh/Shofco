package ke.co.shofcosacco.ui.auth;

import static android.app.Activity.RESULT_OK;

import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogOtpConfirmationBinding;


public class OtpConfirmationDialogFragment extends DialogFragment {

    private static final int REQ_USER_CONSENT = 1500;

    private DialogOtpConfirmationBinding binding;
    private AuthViewModel authViewModel;
    private String sourceAccountNumber, amount,phoneNumber, mOTP;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    public OtpConfirmationDialogFragment() {
        // Required empty public constructor
    }
    public static OtpConfirmationDialogFragment newInstance() {
        OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
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
        binding = DialogOtpConfirmationBinding.inflate(getLayoutInflater());

        if (getArguments() != null) {
            sourceAccountNumber = getArguments().getString("sourceAccountNumber");
            amount = getArguments().getString("amount");
            phoneNumber = getArguments().getString("phoneNumber");
            mOTP = getArguments().getString("mOTP");

        }

        binding.name.setText("Enter OTP sent");

        binding.ivClose.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });

        binding.tvVerifyOtp.setOnClickListener(v -> verifyOtp());

        binding.tvResendOtp.setOnClickListener(v -> resendOtp());

        startSmsUserConsent();

        return binding.getRoot();

    }

    private void verifyOtp(){
        String code=binding.otp.getOtp();
        if (code==null || code.trim().length()<binding.otp.getLength() || code.contains(" ")){
            Toast.makeText(requireContext(), R.string.please_complete_otp, Toast.LENGTH_SHORT).show();
            return;
        }

        if (mOTP.equals(code)){
            Intent result = new Intent();

            result.putExtra("sourceAccountNumber", sourceAccountNumber);
            result.putExtra("amount", amount);
            result.putExtra("phoneNumber", phoneNumber);
            result.putExtra("otp", code);

            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(VERIFY_OTP, RESULT_OK, result);
            }
            dismiss();
        }

    }

    private void resendOtp(){
        ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "",
                "Requesting OTP. Please wait...", true);
        authViewModel.sendOtp(authViewModel.getMemberNo(),"OTP").observe(getViewLifecycleOwner(), apiResponse1 -> {
            progressDialog1.dismiss();
            if (apiResponse1 != null && apiResponse1.isSuccessful()) {
                if (apiResponse1.body().success.equals(STATUS_CODE_SUCCESS)) {
                    OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("sourceAccountNumber",sourceAccountNumber);
                    args.putString("amount",amount);
                    args.putString("phoneNumber",phoneNumber);
                    dialogFragment.setArguments(args);
                    dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
                }
                else {

                    Toast.makeText(requireContext(), apiResponse1.body().description, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "An error occurred. Please try again later", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public void onResume()
    {
        super.onResume();
        binding.otp.post(() -> {
            binding.otp.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager)binding.otp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(binding.otp, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        InputMethodManager imm =
                (InputMethodManager)binding.otp.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        super.onDismiss(dialog);
    }

    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(requireContext());
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> {
        }).addOnFailureListener(e -> {
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOtpFromMessage(message);
            }
        }
    }
    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{4}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.otp.setOtp(matcher.group(0));
            verifyOtp();
        }
    }
    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        requireContext().registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }
    @Override
    public void onStop() {
        super.onStop();
        requireContext().unregisterReceiver(smsBroadcastReceiver);
    }


}