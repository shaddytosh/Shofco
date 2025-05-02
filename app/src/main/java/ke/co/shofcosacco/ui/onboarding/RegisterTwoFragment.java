package ke.co.shofcosacco.ui.onboarding;


import static android.app.Activity.RESULT_OK;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRegisterTwoBinding;
import ke.co.shofcosacco.app.api.requests.AddNextOfKinRequest;
import ke.co.shofcosacco.app.api.responses.CountiesResponse;
import ke.co.shofcosacco.app.models.NextOfKinNew;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;


public class RegisterTwoFragment extends BaseFragment {

    private FragmentRegisterTwoBinding binding;
    private Handler handler;
    private AuthViewModel authViewModel;

    private static final int REQUEST_IMAGE_CAPTURE_ID_FRONT = 10;
    private static final int REQUEST_IMAGE_CAPTURE_ID_BACK = 20;
    private static final int REQUEST_IMAGE_CAPTURE_SIGNATURE = 30;
    private static final int REQUEST_IMAGE_CAPTURE_PASSPORT = 40;


    private static final int REQUEST_IMAGE_PICK_ID_FRONT = 50;
    private static final int REQUEST_IMAGE_PICK_ID_BACK = 60;
    private static final int REQUEST_IMAGE_PICK_SIGNATURE = 70;
    private static final int REQUEST_IMAGE_PICK_PASSPORT= 80;

    private static final int REQUEST_PERMISSIONS_ID_FRONT = 90;
    private static final int REQUEST_PERMISSIONS_ID_BACK = 100;
    private static final int REQUEST_PERMISSIONS_SIGNATURE = 110;
    private static final int REQUEST_PERMISSIONS_PASSPORT = 120;
    private String fullName;
    private String nationalId;
    private String dateOfBirth;
    private String telephone;
    private String email;
    private String town;
    private String address;
    private String selectedGender;
    private String selectedStatus;


    private IdFrontAdapter idFrontAdapter;
    private IdBackAdapter idBackAdapter;
    private PassportAdapter passportAdapter;
    private List<Bitmap> idFrontList = new ArrayList<>();
    private List<Bitmap> idBackList = new ArrayList<>();
    private List<Bitmap> passportList = new ArrayList<>();


    private SignatureAdapter signatureAdapter;
    private List<Bitmap> signatureList = new ArrayList<>();

    private Uri idFrontUri;
    private Uri idBackUri;
    private Uri signatureUri;
    private Uri passportUri;

    private String countyCode, subCountyCode, wardCode;

    private NextOfKinsAdapter nextOfKinsAdapter;
    private List<NextOfKinNew> nextOfKinNewList = new ArrayList<>();

    private Uri   imageUri ;

    private List<CountiesResponse.Counties> countiesList;
    private ArrayAdapter<CountiesResponse.Counties> countiesArrayAdapter;

    private List<CountiesResponse.SubCounties> subCountiesList;
    private ArrayAdapter<CountiesResponse.SubCounties> subCountiesArrayAdapter;

    private List<CountiesResponse.Wards> wardsList;
    private ArrayAdapter<CountiesResponse.Wards> wardsArrayAdapter;

    private final Map<String, ActivityResultLauncher<Uri>> imageCaptureLaunchers = new HashMap<>();
    private final Map<String, ActivityResultLauncher<String>> permissionLaunchers = new HashMap<>();


    private void initLaunchers() {
        setupImageCapture("idFront", idFrontUri, idFrontList, idFrontAdapter);
        setupImageCapture("idBack", idBackUri, idBackList, idBackAdapter);
        setupImageCapture("passport", passportUri, passportList, passportAdapter);
        setupImageCapture("signature", signatureUri, signatureList, signatureAdapter);
    }

    private void setupImageCapture(String key, Uri uri, List<Bitmap> list, RecyclerView.Adapter<?> adapter) {
        imageCaptureLaunchers.put(key, registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
                if (bitmap != null && list.size() < 1) {
                    list.clear(); // Clear if needed
                    list.add(bitmap);
                    adapter.notifyDataSetChanged();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }));

        permissionLaunchers.put(key, registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                imageCaptureLaunchers.get(key).launch(uri);
            } else {
                showCameraRationale();
            }
        }));
    }

    private final ActivityResultLauncher<Uri> mTakeImageIdFront = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), idFrontUri);
            if (imageBitmap != null && idFrontList.size() < 1) {
                idFrontList.add(imageBitmap);
                idFrontAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    });

    private final ActivityResultLauncher<String> mRequestPermissionIdFront = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (idFrontUri != null){
                mTakeImageIdFront.launch(idFrontUri);
            }
        } else {
            showCameraRationale();
        }
    });


    private final ActivityResultLauncher<Uri> mTakeImageIdBack = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), idBackUri);
            if (imageBitmap != null && idBackList.size() < 1) {
                idBackList.add(imageBitmap);
                idBackAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    });

    private final ActivityResultLauncher<String> mRequestPermissionIdBack = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (idBackUri != null){
                mTakeImageIdBack.launch(idBackUri);
            }
        } else {
            showCameraRationale();
        }
    });

   private final ActivityResultLauncher<Uri> mTakeImagePassport = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), passportUri);
            if (imageBitmap != null && passportList.size()  < 1) {
                passportList.add(imageBitmap);
                passportAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    });

    private final ActivityResultLauncher<String> mRequestPermissionPassport= registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (passportUri != null){
                mTakeImagePassport.launch(passportUri);
            }
        } else {
            showCameraRationale();
        }
    });

    private final ActivityResultLauncher<Uri> mTakeImageSignature = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
        try {
            Bitmap imageBitmap1 = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), signatureUri);
            if (imageBitmap1 != null && signatureList.size() < 1) {
                signatureList.add(imageBitmap1);
                signatureAdapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    });

    private final ActivityResultLauncher<String> mRequestPermissionSignature = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (signatureUri != null){
                mTakeImageSignature.launch(signatureUri);
            }
        } else {
            showCameraRationale();
        }
    });


    public RegisterTwoFragment() {
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
        binding = FragmentRegisterTwoBinding.inflate(inflater, container, false);
        handler = new Handler(Looper.getMainLooper());

       fullName = RegisterTwoFragmentArgs.fromBundle(getArguments()).getFullName();
       nationalId  = RegisterTwoFragmentArgs.fromBundle(getArguments()).getNationalId();
       dateOfBirth  = RegisterTwoFragmentArgs.fromBundle(getArguments()).getDateOfBirth();
       telephone = RegisterTwoFragmentArgs.fromBundle(getArguments()).getTelephone();
       email = RegisterTwoFragmentArgs.fromBundle(getArguments()).getEmail();
       town = RegisterTwoFragmentArgs.fromBundle(getArguments()).getTown();
       address = RegisterTwoFragmentArgs.fromBundle(getArguments()).getAddress();
       selectedGender = RegisterTwoFragmentArgs.fromBundle(getArguments()).getSelectedGender();
        selectedStatus = RegisterTwoFragmentArgs.fromBundle(getArguments()).getSelectedStatus();

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());


        binding.tvSubmit.setOnClickListener(v -> validate());

        binding.tvCounty.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null){
                    binding.tilCounty.setError(null);
                }
            }
        });

        binding.tvSubCounty.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null) {
                    binding.tilSubCounty.setError(null);
                }
            }
        });

        binding.tvWard.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null) {
                    binding.tilWard.setError(null);
                }
            }
        });

        binding.txtPhysicalAddress.addTextChangedListener(new TextValidator() {
            @Override
            public void validate(String s) {
                if (s != null) {
                    binding.tilPhysicalAddress.setError(null);
                }
            }
        });

        getCounties();

        binding.tvCounty.setOnItemClickListener((parent, view, position, id) -> {
            if (countiesList != null && countiesList.size() > 0) {
                countyCode = countiesArrayAdapter.getItem(position).code;
                getSubCounty(countyCode);
            }
        });

        binding.tvSubCounty.setOnItemClickListener((parent, view, position, id) -> {
            if (subCountiesList != null && subCountiesList.size() > 0) {
                if (countyCode != null) {
                    subCountyCode = subCountiesArrayAdapter.getItem(position).code;
                    getWards(subCountyCode);
                }else {
                    Toast.makeText(requireContext(), "Please select County First!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.tvWard.setOnItemClickListener((parent, view, position, id) -> {
            if (wardsList != null && wardsList.size() > 0) {
                if (subCountyCode != null) {
                    wardCode = wardsArrayAdapter.getItem(position).countyName;
                }else {
                    Toast.makeText(requireContext(), "Please select Sub County First!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        idFrontAdapter = new IdFrontAdapter(requireContext(), idFrontList, position -> {
            // Remove image at the given position
            idFrontList.remove(position);
            idFrontAdapter.notifyItemRemoved(position);
        });

        idBackAdapter = new IdBackAdapter(requireContext(), idBackList, position -> {
            // Remove image at the given position
            idBackList.remove(position);
            idBackAdapter.notifyItemRemoved(position);
        });

        passportAdapter = new PassportAdapter(requireContext(), passportList, position -> {
            // Remove image at the given position
            passportList.remove(position);
            passportAdapter.notifyItemRemoved(position);
        });

        signatureAdapter = new SignatureAdapter(requireContext(), signatureList, position -> {
            // Remove image at the given position
            signatureList.remove(position);
            signatureAdapter.notifyItemRemoved(position);
        }, "START ODO");


        binding.recyclerIdFront.setAdapter(idFrontAdapter);
        binding.recyclerIdFront.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerIdBack.setAdapter(idBackAdapter);
        binding.recyclerIdBack.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerPassport.setAdapter(passportAdapter);
        binding.recyclerPassport.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.recyclerSignature.setAdapter(signatureAdapter);
        binding.recyclerSignature.setLayoutManager(new LinearLayoutManager(requireContext()));

        nextOfKinsAdapter = new NextOfKinsAdapter(requireContext(), nextOfKinNewList, position -> {
            // Remove image at the given position
            nextOfKinNewList.remove(position);
            nextOfKinsAdapter.notifyItemRemoved(position);
        });

        binding.recyclerNextOfKin.setAdapter(nextOfKinsAdapter);
        binding.recyclerNextOfKin.setLayoutManager(new LinearLayoutManager(requireContext()));


        binding.btnTakeIdFront.setOnClickListener(v -> pickOrTakeImageIdFront());
        binding.btnUploadIdFront.setOnClickListener(v -> {
            checkPermissionsAndPickImage(REQUEST_PERMISSIONS_ID_FRONT);
        });

        binding.btnTakeIdBack.setOnClickListener(v -> pickOrTakeImageIdBack());
        binding.btnUploadIdBack.setOnClickListener(v -> {
            checkPermissionsAndPickImage(REQUEST_PERMISSIONS_ID_BACK);
        });


        binding.buttonSignature.setOnClickListener(v -> pickOrTakeImageSignature());
        binding.buttonSignature1.setOnClickListener(v -> {
            checkPermissionsAndPickImage(REQUEST_PERMISSIONS_SIGNATURE);
        });


        binding.buttonPassport.setOnClickListener(v -> pickOrTakeImagePassport());
        binding.buttonPassport1.setOnClickListener(v -> {
            checkPermissionsAndPickImage(REQUEST_PERMISSIONS_PASSPORT);
        });


        binding.buttonNextOfKin.setOnClickListener(v -> {
            AddNextOfKinDialogFragment dialogFragment = new AddNextOfKinDialogFragment();
            dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());

        });



        return binding.getRoot();
    }


    private void validate() {
        String physicalAddress= binding.txtPhysicalAddress.getText().toString().trim();

        String idFront= null;
        String idBack= null;
        String signature= null;
        String passport= null;

        if (TextValidator.isEmpty(physicalAddress)) {
            binding.tilPhysicalAddress.setError(getString(R.string.required));
        }else if (idFrontUri == null) {
            Toast.makeText(requireContext(), "ID Front is required", Toast.LENGTH_SHORT).show();
        }else if (idBackUri == null) {
            Toast.makeText(requireContext(), "ID Back is required", Toast.LENGTH_SHORT).show();
        }else if (signatureUri == null) {
            Toast.makeText(requireContext(), "Signature is required", Toast.LENGTH_SHORT).show();
        }else if (passportUri == null) {
            Toast.makeText(requireContext(), "Passport is required", Toast.LENGTH_SHORT).show();
        }else {

            for (int i = 0; i < idFrontList.size(); i++) {
                Bitmap imageUri = idFrontList.get(i);
                 idFront = encodeImage(imageUri);
            }

            for (int i = 0; i < idBackList.size(); i++) {
                Bitmap imageUri = idBackList.get(i);
                 idBack = encodeImage(imageUri);
            }

            for (int i = 0; i < signatureList.size(); i++) {
                Bitmap imageUri = signatureList.get(i);
                signature = encodeImage(imageUri);
            }

            for (int i = 0; i < passportList.size(); i++) {
                Bitmap imageUri = passportList.get(i);
                passport = encodeImage(imageUri);
            }

            List<AddNextOfKinRequest.NextOfKin> nextOfKin = getNextOfKinList();

            AddNextOfKinRequest addNextOfKinRequest =  new AddNextOfKinRequest(fullName, dateOfBirth,nationalId, telephone, email, town,
                     address, idFront,idBack, signature, passport, physicalAddress, countyCode, subCountyCode, wardCode, selectedGender,
                     selectedStatus, nextOfKin);



            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
            builder.setCancelable(false);
            View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
            builder.setView(customView);
            TextView textView =customView.findViewById(R.id.dialog_message);
            textView.setText(getString(R.string.register_text));

            AlertDialog alertDialog = builder.create();

            TextView positiveButton = customView.findViewById(R.id.positive_button);
            positiveButton.setOnClickListener(v -> {
                ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                        "Registration ongoing. Please wait...", true);
                authViewModel.registerOne(addNextOfKinRequest).observe(getViewLifecycleOwner(), apiResponse -> {
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

    private List<AddNextOfKinRequest.NextOfKin> getNextOfKinList() {
        List<AddNextOfKinRequest.NextOfKin> nextOfKinList = new ArrayList<>();

        // Loop through the list of NextOfKinNew and convert each to NextOfKin
        for (NextOfKinNew nextOfKinNew : nextOfKinNewList) {
            AddNextOfKinRequest.NextOfKin nextOfKin = new AddNextOfKinRequest.NextOfKin(
                    nextOfKinNew.getName(),
                    nextOfKinNew.getDateOfBirth(),
                    nextOfKinNew.getIdNo(),
                    nextOfKinNew.getTelephone(),
                    nextOfKinNew.getEmail(),
                    nextOfKinNew.getTown(),
                    nextOfKinNew.getAddress(),
                    nextOfKinNew.getAllocation()
            );
            nextOfKinList.add(nextOfKin); // Add to the list
        }

        return nextOfKinList; // Return the list
    }


    private void getCounties(){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Loading Counties. Please wait...", true);
        authViewModel.getCounties().observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();

            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    countiesList = listAPIResponse.body().counties;
                    Comparator<CountiesResponse.Counties> byName = (o1, o2) -> o1.countyName.compareTo(o2.countyName);
                    Collections.sort(countiesList, byName);
                    if (countiesList != null && countiesList.size() > 0){
                        countiesArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, countiesList);

                        binding.tvCounty.setAdapter(countiesArrayAdapter);

                        countiesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        notSuccessDialog("No Counties Available");
                    }
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }



    private void getSubCounty(String countyCode){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Loading Sub Counties. Please wait...", true);
        authViewModel.getSubCounty(countyCode).observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    subCountiesList = listAPIResponse.body().subCounties;
                    Comparator<CountiesResponse.SubCounties> byName = (o1, o2) -> o1.countyName.compareTo(o2.countyName);
                    Collections.sort(subCountiesList, byName);
                    if (subCountiesList != null && subCountiesList.size() > 0){
                        subCountiesArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, subCountiesList);

                        binding.tvSubCounty.setAdapter(subCountiesArrayAdapter);

                        subCountiesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        notSuccessDialog("No Sub counties Available");
                    }
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }

    private void getWards(String subCountyCode){
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Loading Wards. Please wait...", true);
        authViewModel.getWards(subCountyCode).observe(getViewLifecycleOwner(), listAPIResponse -> {
            progressDialog.dismiss();
            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    wardsList = listAPIResponse.body().wards;
                    Comparator<CountiesResponse.Wards> byName = (o1, o2) -> o1.countyName.compareTo(o2.countyName);
                    Collections.sort(wardsList, byName);
                    if (wardsList != null && wardsList.size() > 0){
                        wardsArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, wardsList);

                        binding.tvWard.setAdapter(wardsArrayAdapter);

                        wardsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        notSuccessDialog("No Wards Available");
                    }
                }
            }else {
                notSuccessDialog(Constants.API_ERROR);
            }
        });
    }

    private String encodeImage(Bitmap bitmap) {
        if (bitmap == null) return "";

        try {
            // Resize the image to reduce data size
            int targetWidth = 800;
            float aspectRatio = (float) bitmap.getHeight() / bitmap.getWidth();
            int targetHeight = Math.round(targetWidth * aspectRatio);

            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            // Use WEBP for better compression and smaller output
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resizedBitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 70, outputStream);
            }

            byte[] byteArray = outputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private void pickOrTakeImageIdFront(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showCameraRationale();
            } else {
                mRequestPermissionIdFront.launch(Manifest.permission.CAMERA);
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
            String fileName = simpleDateFormat.format(new Date()) + ".jpg";
            File file = new File(requireContext().getCacheDir(), fileName);
            file.deleteOnExit();
            idFrontUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

            mTakeImageIdFront.launch(idFrontUri);
        }
    }

    private void pickOrTakeImageIdBack(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showCameraRationale();
            } else {
                mRequestPermissionIdBack.launch(Manifest.permission.CAMERA);
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
            String fileName = simpleDateFormat.format(new Date()) + ".jpg";
            File file = new File(requireContext().getCacheDir(), fileName);
            file.deleteOnExit();
            idBackUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

            mTakeImageIdBack.launch(idBackUri);
        }
    }

    private void pickOrTakeImagePassport(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showCameraRationale();
            } else {
                mRequestPermissionPassport.launch(Manifest.permission.CAMERA);
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
            String fileName = simpleDateFormat.format(new Date()) + ".jpg";
            File file = new File(requireContext().getCacheDir(), fileName);
            file.deleteOnExit();
            passportUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

            mTakeImagePassport.launch(passportUri);
        }
    }

    private void pickOrTakeImageSignature(){
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showCameraRationale();
            } else {
                mRequestPermissionSignature.launch(Manifest.permission.CAMERA);
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
            String fileName = simpleDateFormat.format(new Date()) + ".jpg";
            File file = new File(requireContext().getCacheDir(), fileName);
            file.deleteOnExit();
            signatureUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

            mTakeImageSignature.launch(signatureUri);
        }
    }


    private void showCameraRationale() {
        Snackbar.make(binding.coordinator, R.string.camera_rationale, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.grant, v -> mRequestPermissionIdFront.launch(Manifest.permission.CAMERA)).show();
    }



    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",1100);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }

    private void successDialog(String message){
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",1200);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case 1200:
                navigate(RegisterTwoFragmentDirections.actionRegisterTwoToLoginOptions());
                break;

            case REQUEST_IMAGE_CAPTURE_ID_FRONT:
                handleImageCapture(idFrontUri, idFrontList, idFrontAdapter);
                break;

            case REQUEST_IMAGE_CAPTURE_ID_BACK:
                handleImageCapture(idBackUri, idBackList, idBackAdapter);
                break;

            case REQUEST_IMAGE_CAPTURE_SIGNATURE:
                handleImageCapture(signatureUri, signatureList, signatureAdapter);
                break;

            case REQUEST_IMAGE_CAPTURE_PASSPORT:
                handleImageCapture(passportUri, passportList, passportAdapter);
                break;

            case REQUEST_IMAGE_PICK_ID_FRONT:
                handleImagePick(data, idFrontList, idFrontAdapter);
                break;

            case REQUEST_IMAGE_PICK_ID_BACK:
                handleImagePick(data, idBackList, idBackAdapter);
                break;

            case REQUEST_IMAGE_PICK_SIGNATURE:
                handleImagePick(data, signatureList, signatureAdapter);
                break;

            case REQUEST_IMAGE_PICK_PASSPORT:
                handleImagePick(data, passportList, passportAdapter);
                break;

            case 2000:
                if (!nextOfKinNewList.isEmpty()) {
                    binding.buttonNextOfKin.setText("Add Another Next of Kin");
                } else {
                    binding.buttonNextOfKin.setText("Add Next of Kin");
                }

                if (data != null) {
                    nextOfKinNewList.add(new NextOfKinNew(
                            data.getStringExtra("fullName"),
                            data.getStringExtra("dateOfBirth"),
                            data.getStringExtra("nationalId"),
                            data.getStringExtra("telephone"),
                            data.getStringExtra("email"),
                            data.getStringExtra("town"),
                            data.getStringExtra("address"),
                            data.getStringExtra("allocation")
                    ));
                    nextOfKinsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void handleImageCapture(Uri uri, List<Bitmap> list, RecyclerView.Adapter adapter) {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
            if (imageBitmap != null && list.size() < 1) {
                list.clear();
                list.add(imageBitmap);
                adapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleImagePick(Intent data, List<Bitmap> list, RecyclerView.Adapter adapter) {
        if (data == null) return;
        Uri photoURI = data.getData();
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), photoURI);
            if (imageBitmap != null && list.size() < 1) {
                list.clear();
                list.add(imageBitmap);
                adapter.notifyDataSetChanged();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }
    }


    private void checkPermissionsAndPickImage(int permissionType) {
        // Check if the READ_EXTERNAL_STORAGE permission is granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    permissionType);
        } else {
            // If permission is granted, proceed to pick an image
            // Check if we are on Android 10 or above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Use the new method to pick images
                openImagePicker(permissionType);
            } else {
                // For older versions, you can use the original method
                dispatchPickPhotoIntent(permissionType);
            }
        }
    }



    private void openImagePicker(int permissionType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, permissionType);
    }




    private void dispatchPickPhotoIntent(int permissionType) {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickImageIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(pickImageIntent, permissionType);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_ID_FRONT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to pick an image
                dispatchPickPhotoIntent(REQUEST_PERMISSIONS_ID_FRONT);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Permission denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_PERMISSIONS_ID_BACK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to pick an image
                dispatchPickPhotoIntent(REQUEST_PERMISSIONS_ID_BACK);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Permission denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_PERMISSIONS_SIGNATURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to pick an image
                dispatchPickPhotoIntent(REQUEST_PERMISSIONS_SIGNATURE);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Permission denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == REQUEST_PERMISSIONS_PASSPORT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to pick an image
                dispatchPickPhotoIntent(REQUEST_PERMISSIONS_PASSPORT);
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Permission denied to read your external storage", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private static class ImageCaptureConfig {
        Uri uri;
        List<Bitmap> targetList;
        RecyclerView.Adapter<?> adapter;

        ImageCaptureConfig(Uri uri, List<Bitmap> targetList, RecyclerView.Adapter<?> adapter) {
            this.uri = uri;
            this.targetList = targetList;
            this.adapter = adapter;
        }
    }

}