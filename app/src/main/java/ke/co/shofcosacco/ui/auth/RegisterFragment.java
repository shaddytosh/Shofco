package ke.co.shofcosacco.ui.auth;


import static android.app.Activity.RESULT_OK;

import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.REGISTER_TO_LOGIN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRegisterBinding;
import ke.co.shofcosacco.app.models.SecurityQuestion;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class RegisterFragment extends BaseFragment {

    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;

    private String memberNo;
    private SmsBroadcastReceiver smsBroadcastReceiver;

    private static final int REQ_USER_CONSENT = 1500;

    private List<SecurityQuestion> securityQuestions;
    private ArrayAdapter<SecurityQuestion> securityQuestionArrayAdapter;

    private String questionCode;
    private String answer;





    public RegisterFragment() {
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
        binding = FragmentRegisterBinding.inflate(inflater, container, false);


        startSmsUserConsent();

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        if (getArguments() != null) {
            memberNo = RegisterFragmentArgs.fromBundle(getArguments()).getMemberNo();

        }


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

        binding.txtAnswer.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilAnswer.setError(null);
                }
            }
        });

        getSecurityQuestion();

        binding.spinnerSecurityQuestion.setOnItemClickListener((parent, view, position, id) -> {
            if (securityQuestions != null && securityQuestions.size() > 0) {
                questionCode = securityQuestionArrayAdapter.getItem(position).code;
            }
        });


        String IMEI = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;


        binding.tvRegister.setOnClickListener(v -> {

            String answer= binding.txtAnswer.getText().toString();

            String newPin= binding.txtNewPin.getText().toString();
            String confirmNewPin= binding.txtConfirmNewPin.getText().toString();
            String code=binding.otp.getOtp();
            if (code==null || code.trim().length()<binding.otp.getLength() || code.contains(" ")){
                Toast.makeText(requireContext(), R.string.please_complete_otp, Toast.LENGTH_SHORT).show();

            }else if (TextValidator.isEmpty(answer)) {
                binding.tilAnswer.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(newPin)) {
                binding.tilNewPin.setError(getString(R.string.required));
            }else if (TextValidator.isEmpty(confirmNewPin)) {
                binding.tilConfirmNewPin.setError(getString(R.string.required));
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
                View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
                builder.setView(customView);
                TextView textView = customView.findViewById(R.id.dialog_message);
                textView.setText(R.string.do_you_want_to_register);

                AlertDialog alertDialog = builder.create();
                builder.setCancelable(false);
                TextView positiveButton = customView.findViewById(R.id.positive_button);
                positiveButton.setOnClickListener(v4 -> {

                    if (newPin.equals(confirmNewPin)){
                        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                                "Registration ongoing. Please wait...", true);
                        authViewModel.register(memberNo,confirmNewPin,memberNo,code, questionCode, answer, IMEI).observe(getViewLifecycleOwner(), apiResponse -> {
                            progressDialog.dismiss();
                            alertDialog.dismiss();
                            if (apiResponse != null && apiResponse.isSuccessful()) {
                                if (apiResponse.body().success.equals("true")){
                                    SuccessDialogFragment dialogFragment=new SuccessDialogFragment();
                                    Bundle args=new Bundle();
                                    args.putString("message",apiResponse.body().description);
                                    args.putInt("type",REGISTER_TO_LOGIN);
                                    dialogFragment.setArguments(args);
                                    dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
                                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);

                                }else {
                                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                                    notSuccessDialog(apiResponse.body().description);
                                }
                            }else {
                                notSuccessDialog("An error occurred. Please try again later");
                            }
                        });
                    }else {
                        binding.tilConfirmNewPin.setError("Your pin does not match");

                    }

                });

                TextView negativeButton = customView.findViewById(R.id.negative_button);
                negativeButton.setOnClickListener(v2 -> {
                    // perform negative action
                    alertDialog.dismiss();

                });

                alertDialog.show();

            }

        });


        return binding.getRoot();
    }

    private void getSecurityQuestion(){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting Security Questions. Please wait...", true);
        authViewModel.securityQuestion().observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().success.equals(STATUS_CODE_SUCCESS)) {
                    securityQuestions = listAPIResponse.body().securityQuestions;
                    if (securityQuestions != null && securityQuestions.size() > 0){
                        securityQuestionArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, securityQuestions);

                        binding.spinnerSecurityQuestion.setAdapter(securityQuestionArrayAdapter);

                        securityQuestionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    }else {
                        notSuccessDialog("Failed to get security questions. Contact admin");
                    }

                }
            }else {
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_TO_LOGIN && resultCode == Activity.RESULT_OK && data != null) {
            navigate(RegisterFragmentDirections.actionRegisterToLogin());


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