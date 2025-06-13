package ke.co.shofcosacco.ui.onboarding;

import static android.app.Activity.RESULT_OK;

import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.DatePickerDialog;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogNextOfKinBinding;
import ke.co.shofcosacco.app.api.responses.CountiesResponse;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;


public class AddNextOfKinDialogFragment extends DialogFragment {


    private DialogNextOfKinBinding binding;
    private String dateOfBirth;
    private Handler handler;
    private String relationshipTypeCode = "",selectedKinType;

    private List<CountiesResponse.RelationshipType> relationshipTypeList;
    private ArrayAdapter<CountiesResponse.RelationshipType> relationshipTypeArrayAdapter;
    private AuthViewModel authViewModel;


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
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
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

        binding.autoRelationshipType.setOnItemClickListener((parent, view, position, id) -> {
            if (relationshipTypeList != null && relationshipTypeList.size() > 0) {
                relationshipTypeCode = relationshipTypeArrayAdapter.getItem(position).code;
            }
        });

        // Set up the gender dropdown
        String[] kinTypes = new String[]{"Next of Kin", "Beneficiary"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                kinTypes
        );
        binding.tvKinType.setAdapter(genderAdapter);

        binding.tvKinType.setFocusable(false);  // Make it non-editable
        binding.tvKinType.setClickable(true);   // But still clickable to show the dropdown

        binding.tvKinType.setOnItemClickListener((parent, view1, position, id) -> {
            selectedKinType = String.valueOf(position+1);
            // Do something with selectedGender
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

        binding.tvDateOfBirth.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilYearOfBirth.setError(null);
                }
            }
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


        getRelationshipTypes();

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

    private void getRelationshipTypes(){
        authViewModel.getRelationshipTypes().observe(getViewLifecycleOwner(), listAPIResponse -> {

            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    relationshipTypeList = listAPIResponse.body().relationTypes;

                    if (relationshipTypeList != null && relationshipTypeList.size() > 0){
                        Comparator<CountiesResponse.RelationshipType> byName = (o1, o2) -> o1.typeName.compareTo(o2.typeName);
                        Collections.sort(relationshipTypeList, byName);
                        relationshipTypeArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, relationshipTypeList);

                        binding.autoRelationshipType.setAdapter(relationshipTypeArrayAdapter);

                        relationshipTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        Toast.makeText(requireContext(), "No Relationship types Available!", Toast.LENGTH_SHORT).show();

                    }
                }
            }else {
                Toast.makeText(requireContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addNextOfKin(){

        String fullName= binding.txtFullName.getText().toString().trim();
        String allocation= binding.txtAllocation.getText().toString().trim();
        String nationalId= binding.txtNationalId.getText().toString().trim();
        String telephone= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","").replace("+","");
        String email= binding.txtEmail.getText().toString();
        String town= binding.txtTown.getText().toString();
        String address= binding.txtAddress.getText().toString();


        if (TextValidator.isEmpty(selectedKinType)) {
            binding.tilFullName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(fullName)) {
            binding.tilFullName.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(nationalId)) {
            binding.tilNationalId.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(dateOfBirth)) {
            binding.tilYearOfBirth.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(telephone)) {
            binding.phoneNumberInputLayoutForgotPassword.setError(getString(R.string.required));
        }else if (relationshipTypeCode.isEmpty()) {
            Toast.makeText(requireContext(), "Relationship Types is required", Toast.LENGTH_SHORT).show();
        }else if (TextValidator.isEmpty(town)) {
            binding.tilTown.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(address)) {
            binding.tilAddress.setError(getString(R.string.required));
        }else if (TextValidator.isEmpty(allocation)) {
            binding.tilAllocation.setError(getString(R.string.required));
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
            Intent result = new Intent();

            result.putExtra("fullName", fullName);
            result.putExtra("allocation", allocation);
            result.putExtra("nationalId", nationalId);
            result.putExtra("telephone", telephone);
            result.putExtra("email", email);
            result.putExtra("town", town);
            result.putExtra("address", address);
            result.putExtra("dateOfBirth", dateOfBirth);
            result.putExtra("relationshipTypeCode", relationshipTypeCode);
            result.putExtra("kinType", selectedKinType);

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