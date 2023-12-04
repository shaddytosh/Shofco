package ke.co.shofcosacco.ui.auth;

import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VALIDATE_TO_REGISTER;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentLoginBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.PreventDoubleClick;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;


public class LoginFragment extends BaseFragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    boolean isKeyboardShowing = false;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);


        binding.txtNationalId.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilNationalId.setError(null);
                }
            }
        });

        binding.txtPin.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilPin.setError(null);
                }
            }
        });

        binding.closeBtn.setOnClickListener(v -> {
            navigateUp();
        });


        binding.forgotPassword.setOnClickListener(v -> validateDialog(true));




        binding.login.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            isOnline();
            String memberNo = binding.txtNationalId.getText().toString().trim();
            String password= binding.txtPin.getText().toString().trim();


            if (TextValidator.isEmpty(memberNo)) {
                binding.tilNationalId.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(password)) {
                binding.tilPin.setError(getString(R.string.required));
            }else {
                login(memberNo,password);
            }

        });


        return binding.getRoot();
    }


    private void  login(String memberNo, String password){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Logging in. Please wait...", true);
        authViewModel.login(memberNo, password).observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            if (apiResponse != null && apiResponse.isSuccessful()) {
                if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                    if (apiResponse.body().passwordReset.equals(STATUS_CODE_SUCCESS)){
                        changePin(memberNo,apiResponse.body().passwordResetDescription);
                    }else {
                        if (apiResponse.body().token != null) {
                            try {

                                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                                authViewModel.saveTokenAndMemberNo(apiResponse.body().token, memberNo, apiResponse.body().usertype);
                                authViewModel.saveUser(apiResponse.body().email,apiResponse.body().member_no,apiResponse.body().name,apiResponse.body().mobile_no,apiResponse.body().id_no);
                                authViewModel.saveAccountNo(memberNo);
                                authViewModel.saveBiometricDetails(memberNo,password);

                                navigate(LoginFragmentDirections.actionLoginToMain());
                            } catch (GeneralSecurityException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            notSuccessDialog(apiResponse.body().description);


                        }
                    }

                }
                else {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                    notSuccessDialog(apiResponse.body().description);

                }
            } else {

                Toast.makeText(requireContext(), "An error occurred. Please try again later", Toast.LENGTH_SHORT).show();

            }
        });
    }




    private void validateDialog(boolean isResetPin){
        ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isResetPin", isResetPin);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALIDATE_TO_REGISTER && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginFragmentDirections.actionLoginToRegister(data.getStringExtra("member_no")));

        }else  if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            notSuccessDialog(data.getStringExtra("message"));

        }else if (requestCode == 4000 && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginFragmentDirections.actionLoginToResetPin(data.getStringExtra("member_no")));
        }else if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginFragmentDirections.actionLoginToMain());

        }
    }




    private void changePin(String nationalID, String passwordResetDescription){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView textView =customView.findViewById(R.id.dialog_message);
        textView.setText(passwordResetDescription);

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);


        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            alertDialog.dismiss();

            ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                    "Requesting OTP. Please wait...", true);
            authViewModel.sendOtp(nationalID,"LOGIN").observe(getViewLifecycleOwner(), apiResponse -> {
                progressDialog.dismiss();
                if (apiResponse != null && apiResponse.isSuccessful()) {
                    if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {

                        navigate(LoginFragmentDirections.actionLoginToChangePin());

                    }
                    else {

                        notSuccessDialog(apiResponse.body().description);
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


    public void isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(requireContext(), "No Internet connection!", Toast.LENGTH_LONG).show();
        }
    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    @Override
    public void onResume() {
        super.onResume();

        String biometric = authViewModel.getBiometric();
        if (biometric != null && biometric.equals("true") && authViewModel.getMID() != null && authViewModel.getMPIN() != null){
            new Handler().postDelayed(() -> {
                if (getFragmentManager() != null && !getFragmentManager().isStateSaved()) {
                    showBiometricPrompt(authViewModel.getMID(), authViewModel.getMPIN());
                }
            }, 100);
        }

    }


    private void showBiometricPrompt(String memberNo, String password) {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Login")
                .setSubtitle("Use your fingerprint or face recognition")
                .setNegativeButtonText("Cancel")
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(requireActivity(), ContextCompat.getMainExecutor(requireContext()),
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        // Biometric authentication succeeded, proceed with login
                        login(memberNo, password);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        // Biometric authentication failed
                        // Handle accordingly
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
//


}