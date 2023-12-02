package ke.co.shofcosacco.ui.home;

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
import co.ke.shofcosacco.databinding.NotSuccessDialogBinding;


public class NotSuccessDialogFragment extends DialogFragment {
    private String message;
    private  int type;

    public NotSuccessDialogFragment() {
        // Required empty public constructor
    }
    public static NotSuccessDialogFragment newInstance() {
        return new NotSuccessDialogFragment();

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
        NotSuccessDialogBinding binding = NotSuccessDialogBinding.inflate(getLayoutInflater());

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