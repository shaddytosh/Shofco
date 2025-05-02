package ke.co.shofcosacco.ui.onboarding;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogNextOfKinBinding;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;


public class AddNextOfKinDialogFragment extends DialogFragment {


    private DialogNextOfKinBinding binding;
    private String dateOfBirth;
    private Handler handler;

    public AddNextOfKinDialogFragment() {
        // Required empty public constructor
    }
    public static AddNextOfKinDialogFragment newInstance() {
        AddNextOfKinDialogFragment dialogFragment = new AddNextOfKinDialogFragment();
        return dialogFragment;

    }

   public static void show (FragmentManager fragmentManager){
        newInstance().show(fragmentManager,"paymentTrueDialogFragment");
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.RoundedCornersDialog);
        setCancelable(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DialogNextOfKinBinding.inflate(getLayoutInflater());

        handler = new Handler(Looper.getMainLooper());


        binding.btnCancel.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });

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
        binding.txtFullName.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilFullName.setError(null);
                }
            }
        });
        binding.txtAllocation.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilAllocation.setError(null);
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

        binding.btnAdd.setOnClickListener(v -> addNextOfKin());


        return binding.getRoot();

    }

    private void addNextOfKin(){

        String fullName= binding.txtFullName.getText().toString().trim();
        String allocation= binding.txtAllocation.getText().toString().trim();
        String nationalId= binding.txtNationalId.getText().toString().trim();
        String telephone= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","").replace("+","");
        String email= binding.txtEmail.getText().toString();
        String town= binding.txtTown.getText().toString();
        String address= binding.txtAddress.getText().toString();


        if (TextValidator.isEmpty(fullName)) {
            binding.tilFullName.setError(getString(R.string.required));
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
        }else if (TextValidator.isEmpty(allocation)) {
            binding.tilAllocation.setError(getString(R.string.required));
        }else {
            validatesInput();
            Intent result = new Intent();

            result.putExtra("fullName", fullName);
            result.putExtra("allocation", allocation);
            result.putExtra("nationalId", nationalId);
            result.putExtra("telephone", telephone);
            result.putExtra("email", email);
            result.putExtra("town", town);
            result.putExtra("address", address);
            result.putExtra("dateOfBirth", dateOfBirth);

            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(2000, RESULT_OK, result);
            }
            dismiss();
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


}