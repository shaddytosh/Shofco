package ke.co.shofcosacco.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.SuccessDialogBinding;


public class SuccessDialogFragment extends DialogFragment {
    private String message;
    private  int type;

    public SuccessDialogFragment() {
        // Required empty public constructor
    }
    public static SuccessDialogFragment newInstance() {
        return new SuccessDialogFragment();

    }

   public static void show (FragmentManager fragmentManager){
        newInstance().show(fragmentManager,"paymentTrueDialogFragment");
   }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.RoundedCornersDialog);
        setCancelable(false);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SuccessDialogBinding binding = SuccessDialogBinding.inflate(getLayoutInflater());

        if (getArguments()!=null){
            message=getArguments().getString("message");
            type=getArguments().getInt("type");

        }

        binding.tvMessage.setText(message);

        binding.btnDone.setOnClickListener(view -> {
            Intent result = new Intent();
            result.putExtra("success", true);
            if (getParentFragment() != null) {
                getParentFragment().onActivityResult(type, Activity.RESULT_OK, result);
            }
            dismiss();
        });


        return binding.getRoot();

    }

}