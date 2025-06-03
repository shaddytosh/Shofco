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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogOtpConfirmationBinding;
import ke.co.shofcosacco.app.utils.TextValidator;


public class OtpConfirmationDialogFragment extends DialogFragment {

    private static final int REQ_USER_CONSENT = 1500;

    private DialogOtpConfirmationBinding binding;
    private AuthViewModel authViewModel;
    private String sourceAccountNumber, amount,phoneNumber, mOTP,sType;

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
            sType = getArguments().getString("sType");



        }

        binding.ivClose.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });

        binding.tvVerifyOtp.setOnClickListener(v -> verifyOtp());


        return binding.getRoot();

    }

    private void verifyOtp(){

        String password= binding.txtPin.getText().toString().trim();

        if (TextValidator.isEmpty(password)) {
            binding.tilPin.setError(getString(R.string.required));
        }else {
            if (mOTP != null && mOTP.equals(password)){
                Intent result = new Intent();

                result.putExtra("sourceAccountNumber", sourceAccountNumber);
                result.putExtra("amount", amount);
                result.putExtra("phoneNumber", phoneNumber);
                result.putExtra("otp", password);
                result.putExtra("sType", sType);



                if (getParentFragment() != null) {
                    getParentFragment().onActivityResult(VERIFY_OTP, RESULT_OK, result);
                }
                dismiss();
            }else {
                Toast.makeText(requireContext(), "Wrong PIN, Please try again. Too many attempts will block account", Toast.LENGTH_SHORT).show();

            }
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        binding.txtPin.post(() -> {
            binding.txtPin.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager)binding.txtPin.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(binding.txtPin, InputMethodManager.SHOW_IMPLICIT);
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog)
    {
        InputMethodManager imm =
                (InputMethodManager)binding.txtPin.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

        super.onDismiss(dialog);
    }


}