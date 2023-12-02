package ke.co.shofcosacco.ui.auth;


import static android.app.Activity.RESULT_OK;

import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.FORGOT_PIN_TO_LOGIN;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentChangePinBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class ChangePinFragment extends BaseFragment {

    private FragmentChangePinBinding binding;
    private AuthViewModel authViewModel;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    private static final int REQ_USER_CONSENT = 1500;



    public ChangePinFragment() {
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
        binding = FragmentChangePinBinding.inflate(inflater, container, false);

        startSmsUserConsent();

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());


        binding.txtNewPin.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilNewPin.setError(null);
                }
            }
        });


        binding.txtConfirmNewPin.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilConfirmNewPin.setError(null);
                }
            }
        });

        binding.txtOldPin.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s!=null){
                    binding.tilOldPin.setError(null);
                }
            }
        });


        binding.tvChange.setOnClickListener(v -> {
            String oldPin= binding.txtOldPin.getText().toString();
            String newPin= binding.txtNewPin.getText().toString();
            String confirmNewPin= binding.txtConfirmNewPin.getText().toString();
            String code=binding.otp.getOtp();
            if (code==null || code.trim().length()<binding.otp.getLength() || code.contains(" ")){
                Toast.makeText(requireContext(), R.string.please_complete_otp, Toast.LENGTH_SHORT).show();

            }else if (TextValidator.isEmpty(oldPin)) {
                binding.tilOldPin.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(newPin)) {
                binding.tilNewPin.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(confirmNewPin)) {
                binding.tilConfirmNewPin.setError(getString(R.string.required));
            }else {

                binding.tilNewPin.setError(null);
                binding.tilOldPin.setError(null);
                binding.tilConfirmNewPin.setError(null);

                if (newPin.equals(confirmNewPin)){
                    ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                            "Changing pin. Please wait...", true);
                    authViewModel.changePassword(oldPin,newPin,code).observe(getViewLifecycleOwner(), apiResponse -> {
                        progressDialog.dismiss();
                        if (apiResponse != null && apiResponse.isSuccessful()) {

                            if (apiResponse.body().success.equals("true")){
                                SuccessDialogFragment dialogFragment=new SuccessDialogFragment();
                                Bundle args=new Bundle();
                                args.putString("message",apiResponse.body().description);
                                args.putInt("type",FORGOT_PIN_TO_LOGIN);
                                dialogFragment.setArguments(args);
                                dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
                                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                            }else {
                                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                                notSuccessDialog(apiResponse.body().description);
                            }
                        }
                    });
                }else {
                    binding.tilConfirmNewPin.setError("Your pin does not match");

                }
            }


        });


        return binding.getRoot();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FORGOT_PIN_TO_LOGIN && resultCode == Activity.RESULT_OK && data != null) {

            navigate(ChangePinFragmentDirections.actionChangePinToLogin());

            try {
                authViewModel.removeLoggedInUser();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }

        }else if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);

                getOtpFromMessage(message);
            }
        }
    }


    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(requireContext());
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(aVoid -> {
        }).addOnFailureListener(e -> {
        });
    }

    private void getOtpFromMessage(String message) {
        // This will match any 6 digit number in the message
        Pattern pattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            binding.otp.setOtp(matcher.group(0));
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