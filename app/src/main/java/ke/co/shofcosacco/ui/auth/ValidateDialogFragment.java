package ke.co.shofcosacco.ui.auth;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VALIDATE_TO_REGISTER;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogFragmentValidateBinding;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;


public class ValidateDialogFragment extends DialogFragment {

    private DialogFragmentValidateBinding binding;
    private AuthViewModel authViewModel;

    private boolean isResetPin = false;

    public ValidateDialogFragment() {
        // Required empty public constructor
    }
    public static ValidateDialogFragment newInstance() {
        ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
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
        binding = DialogFragmentValidateBinding.inflate(getLayoutInflater());


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        binding.tvClose.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });

        binding.txtMemberNo.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilMemberNo.setError(null);
                }
            }
        });

        if (getArguments() != null){
            isResetPin = getArguments().getBoolean("isResetPin");
        }

        if (isResetPin){
            binding.tvBeginRegistration.setText("Reset Pin");
        }

        binding.tvBeginRegistration.setOnClickListener(v -> verifyOtp(isResetPin));

        return binding.getRoot();

    }

    private void verifyOtp(boolean isResetPin){
        String memberNo =binding.txtMemberNo.getText().toString();
        if (TextValidator.isEmpty(memberNo)){
            binding.tilMemberNo.setError(getString(R.string.required));
        }else {
            binding.tilMemberNo.setError(null);
            ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                    "Validating. Please wait...", true);
            authViewModel.validateUser(memberNo).observe(getViewLifecycleOwner(), apiResponse -> {
                progressDialog.dismiss();
                if (apiResponse != null && apiResponse.isSuccessful()) {
                    if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                        if (isResetPin){
                            sendOtp(memberNo, true);
                        }else {

                            Intent result = new Intent();
                            result.putExtra("message", apiResponse.body().description);
                            if (getParentFragment() != null) {
                                getParentFragment().onActivityResult(3000, Activity.RESULT_OK, result);
                            }
                            dismiss();
                        }
                    } else {
                        sendOtp(memberNo,isResetPin);
                    }
                }

            });

        }
    }

    private void sendOtp(String memberNo, boolean  isResetPin) {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Requesting OTP. Please wait...", true);
        authViewModel.sendOtp(memberNo,"VALIDATE").observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            if (apiResponse != null && apiResponse.isSuccessful()) {
                if (apiResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                   if (isResetPin){
                       Intent result = new Intent();
                       result.putExtra("member_no", memberNo);
                       if (getParentFragment() != null) {
                           getParentFragment().onActivityResult(4000, Activity.RESULT_OK, result);
                       }
                       dismiss();
                   }else {
                       Intent result = new Intent();
                       result.putExtra("member_no", memberNo);
                       if (getParentFragment() != null) {
                           getParentFragment().onActivityResult(VALIDATE_TO_REGISTER, Activity.RESULT_OK, result);
                       }
                       dismiss();
                   }
                }
                else {
                    notSuccessDialog(apiResponse.body().description);
                }
            } else {

                notSuccessDialog("An error occurred. Please try again later");

            }
        });
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
    public void onResume()
    {
        super.onResume();
        binding.txtMemberNo.post(() -> {
            binding.txtMemberNo.requestFocus();
            InputMethodManager imm =
                    (InputMethodManager)binding.txtMemberNo.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.showSoftInput(binding.txtMemberNo, InputMethodManager.SHOW_IMPLICIT);
        });
    }


}