package ke.co.shofcosacco.ui.auth;


import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import co.ke.shofcosacco.databinding.FragmentHelpBottomSheetBinding;


public class HelpBottomSheetFragment extends BottomSheetDialogFragment {

    private final String[] permissionsCall = new String[]{Manifest.permission.CALL_PHONE};

    public HelpBottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        co.ke.shofcosacco.databinding.FragmentHelpBottomSheetBinding binding = FragmentHelpBottomSheetBinding.inflate(inflater, container, false);


        binding.cvCall.setOnClickListener(v -> callPhone());

        binding.cvEmail.setOnClickListener(v -> email());

        binding.cvLocation.setOnClickListener(v -> openLocation());

        return binding.getRoot();
    }

    private void callPhone() {
        if (doesNotNeedPermissionsCall()) { //  permissions  granted.
            //Creating intents for making a call
            try {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+254700160012"));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(requireContext(), "Your device does not support phone calls", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void email() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:info@shofcosacco.com")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback");
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(requireContext(), "There are no email client installed on your device.",Toast.LENGTH_LONG).show();
        }
    }

    private void openLocation(){

        String strUri = "http://maps.google.com/maps?q=loc:" + "-1.3159342948978476" + "," + "36.783172014792626" + " ( SHOFCO SACCO )";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);
    }

    private boolean doesNotNeedPermissionsCall() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissionsCall) {
            result = ContextCompat.checkSelfPermission(requireContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), listPermissionsNeeded.toArray(new String[0]), 124);
            return false;
        }

        return true;
    }

}