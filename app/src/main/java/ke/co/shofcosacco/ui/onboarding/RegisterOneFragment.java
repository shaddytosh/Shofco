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
import android.text.Editable;
import android.text.TextWatcher;
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
    private String selectedGender, selectedStatus, idNo;


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


        binding.tvNext.setOnClickListener(v -> validate());

        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        idNo = RegisterOneFragmentArgs.fromBundle(getArguments()).getIdNo();

        binding.txtNationalId.setText(idNo);

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

        binding.txtTown.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilTown.setError(null);
                }
            }
        });

        binding.txtAddress.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilAddress.setError(null);
                }
            }
        });

        binding.tvDateOfBirth.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilYearOfBirth.setError(null);
                }
            }
        });

        // Set up the gender dropdown
        String[] genders = new String[]{"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                genders
        );
        binding.tvGender.setAdapter(genderAdapter);

        // Set up the marital status dropdown
        String[] maritalStatuses = new String[]{"Single", "Married", "Divorced", "Widowed"};
        ArrayAdapter<String> maritalStatusAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                maritalStatuses
        );

        binding.tvMaritalStatus.setAdapter(maritalStatusAdapter);


        binding.tvGender.setFocusable(false);  // Make it non-editable
        binding.tvGender.setClickable(true);   // But still clickable to show the dropdown
        binding.tvMaritalStatus.setFocusable(false);
        binding.tvMaritalStatus.setClickable(true);

        binding.tvGender.setOnItemClickListener((parent, view1, position, id) -> {
             selectedGender = parent.getItemAtPosition(position).toString();
            // Do something with selectedGender
        });

        binding.tvMaritalStatus.setOnItemClickListener((parent, view1, position, id) -> {
            selectedStatus = parent.getItemAtPosition(position).toString();
            // Do something with selectedStatus
        });

        binding.phoneNumberEditTextForgotPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && s.charAt(0) == '0') {
                    binding.phoneNumberEditTextForgotPassword.setText("");
                    binding.phoneNumberInputLayoutForgotPassword.setError("Phone number should not start with 0");
                } else {
                    binding.phoneNumberInputLayoutForgotPassword.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        binding.tvDateOfBirth.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String input = binding.tvDateOfBirth.getText().toString();
                if (!isValidDate(input)) {
                    binding.tilYearOfBirth.setError("Use format yyyy-MM-dd");
                } else {
                    binding.tilYearOfBirth.setError(null);
                }
            }
        });



        binding.tvDateOfBirth.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Set max date to 18 years ago
            Calendar maxDate = Calendar.getInstance();
            maxDate.set(year - 18, month, dayOfMonth);

            // Use maxDate as the default selection
            int defaultYear = maxDate.get(Calendar.YEAR);
            int defaultMonth = maxDate.get(Calendar.MONTH);
            int defaultDay = maxDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    R.style.DatePickerDialogTheme,
                    (view1, year1, month1, dayOfMonth1) -> {
                        String rawDateOfBirth = year1 + "-" + (month1 + 1) + "-" + dayOfMonth1;

                        Date date = null;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(rawDateOfBirth);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        String formattedDateOfBirth = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
                        dateOfBirth = formattedDateOfBirth;
                        binding.tvDateOfBirth.setText(formattedDateOfBirth);
                    },
                    defaultYear, defaultMonth, defaultDay // <-- show this by default
            );

            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();
            datePickerDialog.setTitle("Select date of birth");
        });

        return binding.getRoot();
    }

    private boolean isValidDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            sdf.setLenient(false);
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            Calendar limit = Calendar.getInstance();
            limit.add(Calendar.YEAR, -18);
            return !cal.after(limit);
        } catch (ParseException e) {
            return false;
        }
    }

    private void validate() {
        String firstName= binding.txtFirstName.getText().toString().trim();
        String lastName= binding.txtLastName.getText().toString().trim();
        String nationalId= binding.txtNationalId.getText().toString().trim();
        String telephone= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","");
        String email= binding.txtEmail.getText().toString();
        String town= binding.txtTown.getText().toString();
        String address= binding.txtAddress.getText().toString();





        if (TextValidator.isEmpty(firstName)) {
            binding.tilFirstName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(lastName)) {
            binding.tilLastName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(nationalId)) {
            binding.tilNationalId.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(dateOfBirth)) {
            binding.tilYearOfBirth.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(telephone)) {
            binding.phoneNumberInputLayoutForgotPassword.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(town)) {
            binding.tilTown.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(address)) {
            binding.tilAddress.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(selectedGender)) {
            binding.tilGender.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(selectedStatus)) {
            binding.tilMaritalStatus.setError(getString(R.string.required));
        }else {

            String phone = binding.phoneNumberEditTextForgotPassword.getText().toString().trim();
            if (phone.startsWith("0")) {
                binding.phoneNumberInputLayoutForgotPassword.setError("Phone number should not start with 0");
                return;
            } else {
                binding.phoneNumberInputLayoutForgotPassword.setError(null); // Clear error
                // Proceed
            }



            validatesInput();

            String fullName = firstName+" "+lastName;

            navigate(RegisterOneFragmentDirections.actionRegisterOneToRegisterThree(
                   fullName,nationalId,dateOfBirth,telephone,email,town,address,selectedGender,selectedStatus));

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




}