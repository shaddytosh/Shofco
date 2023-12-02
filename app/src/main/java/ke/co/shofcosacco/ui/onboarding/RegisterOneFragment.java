package ke.co.shofcosacco.ui.onboarding;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.SUCCESS;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRegisterOneBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;


public class RegisterOneFragment extends BaseFragment {

    private FragmentRegisterOneBinding binding;
    private Handler handler;
    private AuthViewModel authViewModel;
    private String dateOfBirth;



    public RegisterOneFragment() {
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
        binding = FragmentRegisterOneBinding.inflate(inflater, container, false);
        handler = new Handler(Looper.getMainLooper());

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());


        binding.tvRegister.setOnClickListener(v -> validate());

        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        setupValidationListeners();
        binding.txtEmail.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilEmail.setError(null);
                }
            }
        });

        binding.txtNationalId.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilNationalId.setError(null);
                }
            }
        });
        binding.txtFirstName.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilFirstName.setError(null);
                }
            }
        });
        binding.txtLastName.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilLastName.setError(null);
                }
            }
        });


        binding.tvDateOfBirth.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            Calendar maxDate = Calendar.getInstance();
//            maxDate.set(year - 18, month, dayOfMonth);

            DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),R.style.DatePickerDialogTheme, (view1, year1, month1, dayOfMonth1) -> {
                String rawDateOfBirth = year1 + "-" + (month1 + 1) + "-" + dayOfMonth1;

                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDateOfBirth);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String formattedDateOfBirth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
                dateOfBirth=formattedDateOfBirth;
                binding.tvDateOfBirth.setText(formattedDateOfBirth);
            }, year, month, dayOfMonth);
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();
            datePickerDialog.setTitle("Select date of birth");
        });

        return binding.getRoot();
    }

    private void validate() {
        String firstName= binding.txtFirstName.getText().toString().trim();
        String lastName= binding.txtLastName.getText().toString().trim();
        String nationalId= binding.txtNationalId.getText().toString().trim();
        String telephone= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","").replace("+","");
        String email= binding.txtEmail.getText().toString();

        if (TextValidator.isEmpty(firstName)) {
            binding.tilFirstName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(lastName)) {
            binding.tilLastName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(nationalId)) {
            binding.tilNationalId.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(dateOfBirth)) {
            binding.tilYearOfBirth.setError(getString(R.string.required));
        }else {
            validatesInput();

            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
            builder.setCancelable(false);
            View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
            builder.setView(customView);
            TextView textView =customView.findViewById(R.id.dialog_message);
            textView.setText("You are about to register. Do you wish to continue?\n By Clicking Proceed you agree to register with CHUNA SACCO");

            AlertDialog alertDialog = builder.create();

            TextView positiveButton = customView.findViewById(R.id.positive_button);
            positiveButton.setOnClickListener(v -> {
                ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                        "Registration ongoing. Please wait...", true);
                authViewModel.registerOne(firstName+" "+lastName, nationalId, telephone, email).observe(getViewLifecycleOwner(), apiResponse -> {
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

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    private void successDialog(String message){
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",SUCCESS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUCCESS && resultCode == Activity.RESULT_OK && data != null) {
            navigate(RegisterOneFragmentDirections.actionRegisterOneToLoginOptions());
        }
    }


}