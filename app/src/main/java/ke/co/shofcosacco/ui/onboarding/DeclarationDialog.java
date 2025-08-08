package ke.co.shofcosacco.ui.onboarding;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.DialogDeclarationBinding;


public class DeclarationDialog extends DialogFragment {

    private DialogDeclarationBinding binding;

    private String memberNo;

    public DeclarationDialog() {
        // Required empty public constructor
    }
    public static DeclarationDialog newInstance() {
        DeclarationDialog dialogFragment = new DeclarationDialog();
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
        binding = DialogDeclarationBinding.inflate(getLayoutInflater());


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (getArguments() != null){
            memberNo = getArguments().getString("member_no");
        }

        binding.tvIReject.setOnClickListener(view -> {
            Intent result = new Intent();
            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(8000, Activity.RESULT_OK, result);
            }
            dismiss();
        });

        binding.tvIAccept.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("member_no", memberNo);
            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(9000, Activity.RESULT_OK, result);
            }
            dismiss();
        });

        return binding.getRoot();

    }


}