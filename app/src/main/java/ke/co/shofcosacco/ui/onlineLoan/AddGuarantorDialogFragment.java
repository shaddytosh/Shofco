package ke.co.shofcosacco.ui.onlineLoan;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogGuarantorBinding;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;


public class AddGuarantorDialogFragment extends DialogFragment {


    private DialogGuarantorBinding binding;
    private AuthViewModel authViewModel;


    public AddGuarantorDialogFragment() {
        // Required empty public constructor
    }
    public static AddGuarantorDialogFragment newInstance() {
        AddGuarantorDialogFragment dialogFragment = new AddGuarantorDialogFragment();
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
        binding = DialogGuarantorBinding.inflate(getLayoutInflater());


        binding.btnCancel.setOnClickListener(view -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            dismiss();
        });


        binding.txtNationalId.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilNationalId.setError(null);
                }
            }
        });


        binding.btnValidate.setOnClickListener(v -> addNextOfKin());



        return binding.getRoot();

    }


    private void addNextOfKin(){

        String nationalId= binding.txtNationalId.getText().toString().trim();


         if (TextValidator.isEmpty(nationalId)) {
            binding.tilNationalId.setError(getString(R.string.required));
        }else {
            Intent result = new Intent();


            result.putExtra("nationalId", nationalId);


            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(2000, RESULT_OK, result);
            }
            dismiss();
        }

    }



}