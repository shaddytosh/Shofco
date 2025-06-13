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
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import com.yalantis.ucrop.UCrop;

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


public class RegisterTwoFragment extends BaseFragment implements CropImageHelperFragment.OnImageCroppedListener{

    private FragmentRegisterTwoBinding binding;
    private Handler handler;
    private AuthViewModel authViewModel;

    private String fullName;
    private String nationalId;
    private String dateOfBirth;
    private String telephone;
    private String email;
    private String town;
    private String address;
    private String selectedGender;
    private String selectedStatus;

    private String branch;
    private String cluster;
    private String disability;
    private String specifyDisability;
    private String introducerName;
    private String introducerId;
    private String introducerPhoneNo;



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
    String _employerName;
    String _employerAddress;
    String _employerIncome;
    String _businessName;
    String _businessLocation;
    String _businessIncome ;

    private String countyCode, subCountyCode, wardCode;

    private NextOfKinsAdapter nextOfKinsAdapter;
    private List<NextOfKinNew> nextOfKinNewList = new ArrayList<>();
    private List<CountiesResponse.Counties> countiesList;
    private ArrayAdapter<CountiesResponse.Counties> countiesArrayAdapter;

    private List<CountiesResponse.SubCounties> subCountiesList;
    private ArrayAdapter<CountiesResponse.SubCounties> subCountiesArrayAdapter;

    private List<CountiesResponse.Wards> wardsList;
    private ArrayAdapter<CountiesResponse.Wards> wardsArrayAdapter;
    String employmentStatus;

    private CropImageHelperFragment cropImageHelper;
    private Map<CropImageHelperFragment.ImageType, String> imageBase64Map = new HashMap<>();
    private Map<CropImageHelperFragment.ImageType, Uri> imageUriMap = new HashMap<>();


    public RegisterTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        cropImageHelper = CropImageHelperFragment.newInstance(this);
        getChildFragmentManager()
                .beginTransaction()
                .add(cropImageHelper, "crop_helper")
                .commit();
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

        branch = RegisterTwoFragmentArgs.fromBundle(getArguments()).getBranch();
        cluster = RegisterTwoFragmentArgs.fromBundle(getArguments()).getCluster();
        disability = RegisterTwoFragmentArgs.fromBundle(getArguments()).getDisability();
        specifyDisability = RegisterTwoFragmentArgs.fromBundle(getArguments()).getSpecifyDisability();
        introducerName = RegisterTwoFragmentArgs.fromBundle(getArguments()).getIntroducerName();
        introducerId = RegisterTwoFragmentArgs.fromBundle(getArguments()).getIntroducerId();
        introducerPhoneNo = RegisterTwoFragmentArgs.fromBundle(getArguments()).getIntroducerPhoneNo();

        _employerName = RegisterTwoFragmentArgs.fromBundle(getArguments()).getEmployerName();
        _employerAddress = RegisterTwoFragmentArgs.fromBundle(getArguments()).getEmployerAddress();
        _employerIncome = RegisterTwoFragmentArgs.fromBundle(getArguments()).getEmployerIncome();
        _businessName = RegisterTwoFragmentArgs.fromBundle(getArguments()).getBusinessName();
        _businessLocation = RegisterTwoFragmentArgs.fromBundle(getArguments()).getBusinessLocation();
        _businessIncome = RegisterTwoFragmentArgs.fromBundle(getArguments()).getBusinessIncome();
         employmentStatus  = RegisterTwoFragmentArgs.fromBundle(getArguments()).getEmploymentStatus();



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

        binding.btnTakeIdFront.setOnClickListener(v ->
                cropImageHelper.launchCamera(CropImageHelperFragment.ImageType.FRONT_ID));
        binding.btnUploadIdFront.setOnClickListener(v ->
                cropImageHelper.launchPicker(CropImageHelperFragment.ImageType.FRONT_ID));

        binding.btnTakeIdBack.setOnClickListener(v ->
                cropImageHelper.launchCamera(CropImageHelperFragment.ImageType.BACK_ID));
        binding.btnUploadIdBack.setOnClickListener(v ->
                cropImageHelper.launchPicker(CropImageHelperFragment.ImageType.BACK_ID));

        binding.buttonSignature.setOnClickListener(v ->
                cropImageHelper.launchCamera(CropImageHelperFragment.ImageType.SIGNATURE));
        binding.buttonSignature1.setOnClickListener(v ->
                cropImageHelper.launchPicker(CropImageHelperFragment.ImageType.SIGNATURE));

        binding.buttonPassport.setOnClickListener(v ->
                cropImageHelper.launchCamera(CropImageHelperFragment.ImageType.PASSPORT));
        binding.buttonPassport1.setOnClickListener(v ->
                cropImageHelper.launchPicker(CropImageHelperFragment.ImageType.PASSPORT));



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
        }else if (idFrontList.isEmpty()) {
            Toast.makeText(requireContext(), "ID Front is required", Toast.LENGTH_SHORT).show();
        }else if (idBackList.isEmpty()) {
            Toast.makeText(requireContext(), "ID Back is required", Toast.LENGTH_SHORT).show();
        }else if (signatureList.isEmpty()) {
            Toast.makeText(requireContext(), "Signature is required", Toast.LENGTH_SHORT).show();
        }else if (passportList.isEmpty()) {
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
                     selectedStatus, nextOfKin,branch, cluster, disability, specifyDisability, introducerName, introducerId, introducerPhoneNo,
                   _employerName, _employerAddress, _employerIncome,
                    _businessName, _businessLocation, _businessIncome,employmentStatus);



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
                            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                            Toast.makeText(requireContext(), apiResponse.body().description, Toast.LENGTH_SHORT).show();

                            StkPushDialogFragment dialogFragment = new StkPushDialogFragment();
                            Bundle args = new Bundle();
                            args.putString("amount", apiResponse.body().amount);
                            args.putString("phoneNumber", apiResponse.body().mobileNo);
                            args.putString("message", apiResponse.body().description);
                            args.putString("idNo", apiResponse.body().idNo);
                            dialogFragment.setArguments(args);
                            dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());

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
                    nextOfKinNew.getAllocation(),
                    nextOfKinNew.getRelationshipTypeCode(),
                    nextOfKinNew.getKinType()


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
            // Use JPEG format for standard image compression
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);

            byte[] byteArray = outputStream.toByteArray();
            return Base64.encodeToString(byteArray, Base64.NO_WRAP);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
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
        Uri imageUri = data.getData();

        switch (requestCode) {
            case 1200:
                navigate(RegisterTwoFragmentDirections.actionRegisterTwoToLoginOptions());
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
                            data.getStringExtra("allocation"),
                            data.getStringExtra("relationshipTypeCode"),
                            data.getStringExtra("kinType")

                    ));
                    nextOfKinsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onImageCropped(Uri croppedImageUri, String base64Data, CropImageHelperFragment.ImageType imageType) {
        // Store the data
        imageBase64Map.put(imageType, base64Data);
        imageUriMap.put(imageType, croppedImageUri);

        // Update UI based on image type
        switch (imageType) {
            case FRONT_ID:
                handleImagePick(croppedImageUri,idFrontList,idFrontAdapter);
                break;
            case BACK_ID:
                handleImagePick(croppedImageUri,idBackList,idBackAdapter);
                break;
            case SIGNATURE:
                handleImagePick(croppedImageUri,signatureList,signatureAdapter);
                break;
            case PASSPORT:
                handleImagePick(croppedImageUri,passportList,passportAdapter);
                break;
        }

        Toast.makeText(getContext(), imageType.getValue() + " image captured successfully", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onImageCropError(String error, CropImageHelperFragment.ImageType imageType) {
        Toast.makeText(getContext(), "Error capturing " + imageType.getValue() + ": " + error, Toast.LENGTH_LONG).show();
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

    private void handleImagePick(Uri imageUri, List<Bitmap> list, RecyclerView.Adapter adapter) {
        try {
            Bitmap imageBitmap;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) { // API 28+
                ImageDecoder.Source source = ImageDecoder.createSource(requireActivity().getContentResolver(), imageUri);
                imageBitmap = ImageDecoder.decodeBitmap(source);
            } else {
                imageBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
            }

            if (imageBitmap != null && list.size() < 1) {
                list.add(imageBitmap);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("ImagePicker", "Bitmap is null after decoding");
            }
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Error loading image", Toast.LENGTH_SHORT).show();
        }

    }

}