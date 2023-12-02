package ke.co.shofcosacco.ui.profile;




import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;


import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentProfileBinding;
import ke.co.shofcosacco.app.database.AppDatabase;
import ke.co.shofcosacco.app.database.models.ProfilePicture;
import ke.co.shofcosacco.app.models.Profile;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.PreventDoubleClick;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.MainFragmentDirections;


public class ProfileFragment extends BaseFragment {

    private FragmentProfileBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;
    private AppDatabase appDatabase;
    
    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    private Uri mProfilePhoto;


    private final ActivityResultLauncher<Uri> mTakeImage = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> displayProfilePhoto());

    private final ActivityResultLauncher<String> mPickImage = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
        mProfilePhoto = result;
        displayProfilePhoto();
    });

    private final ActivityResultLauncher<String> mRequestPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (mProfilePhoto != null){
                mTakeImage.launch(mProfilePhoto);
            }
        } else {
            showCameraRationale();
        }
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(this).get(LiveDataViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        appDatabase = Room.databaseBuilder(requireContext(),
                AppDatabase.class, "chuna").build();

        binding.toolbar.setNavigationOnClickListener(v -> {
            if (ProfileFragment.this.getParentFragment() != null && ProfileFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) ProfileFragment.this.getParentFragment()).navigateToHome();
            }else {
                navigateUp();
            }
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            getProfile();
            binding.swipeToRefresh.setRefreshing(false);
        });

        getProfile();

        binding.tvLogOut.setOnClickListener(v -> logOut());

        binding.tvChangePin.setOnClickListener(v -> changePin());

        binding.ivProfilePic.setOnClickListener(v -> pickOrTakeImage());

        binding.ivShare.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage = getString(R.string.share_message);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });


        loadDataFromDb(authViewModel.getPhone());

        return binding.getRoot();

    }


    private void getProfile(){

        authViewModel.getCustomerData().observe(getViewLifecycleOwner(), apiResponse -> {
            if (apiResponse != null && apiResponse.isSuccessful()) {

                boolean isSuccess = apiResponse.body().success.equals(STATUS_CODE_SUCCESS);

                if (isSuccess){
                    displayData(apiResponse.body());
                    liveDataViewModel.setProfileMutableLiveData(apiResponse.body());
                }

            }

        });
    }


    private void displayData(Profile profile) {
        if (profile.name != null ) {
            binding.tvFullNames.setText(profile.name);
        }

        if (profile.member_no != null ) {
            binding.tvMemberNo.setText(profile.member_no);
        }

        if (profile.mobile_no != null) {
            binding.tvMobileNumber.setText(profile.mobile_no);
        }
        if (profile.email != null) {
            binding.tvEmail.setText(profile.email);
        }
        if (profile.id_no != null) {
            binding.tvNationalId.setText(profile.id_no);
        }
    }

    private void logOut(){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        builder.setCancelable(false);
        builder.setCancelable(false);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView textView =customView.findViewById(R.id.dialog_message);
        textView.setText(getString(R.string.log_out_text));

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {
            try {
                authViewModel.removeLoggedInUser();
                Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                alertDialog.dismiss();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });

        alertDialog.show();
    }

    private void changePin(){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        builder.setCancelable(false);
        builder.setCancelable(false);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        builder.setView(customView);
        TextView textView =customView.findViewById(R.id.dialog_message);
        textView.setText("Do you want to change pin?");

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);


        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {

            if (ProfileFragment.this.getParentFragment() != null && ProfileFragment.this.getParentFragment() instanceof MainFragment) {
                navigate(MainFragmentDirections.actionMainToChangePin());

            }else {
                navigate(ProfileFragmentDirections.actionProfileToChangePin());

            }
            alertDialog.dismiss();

        });

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            // perform negative action
            alertDialog.dismiss();

        });


        alertDialog.show();
    }

    private void pickOrTakeImage(){
        androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Profile Picture")
                .setItems(new String[]{"Take Image", "Pick Image"}, (dialog, which) -> {
                    //Take an image
                    if (which == 0) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                showCameraRationale();
                            } else {
                                mRequestPermission.launch(Manifest.permission.CAMERA);
                            }
                        } else {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss", Locale.getDefault());
                            String fileName = simpleDateFormat.format(new Date()) + ".jpg";
                            File file = new File(requireContext().getCacheDir(), fileName);
                            file.deleteOnExit();
                            mProfilePhoto = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", file);

                            mTakeImage.launch(mProfilePhoto);
                        }
                    }
                    //Pick an image
                    else {
                        mPickImage.launch("image/*");
                    }
                })
                .create();
        alertDialog.show();
    }

    private void showCameraRationale() {
        Snackbar.make(binding.coordinator, R.string.camera_rationale, BaseTransientBottomBar.LENGTH_INDEFINITE)
                .setAction(R.string.grant, v -> mRequestPermission.launch(Manifest.permission.CAMERA)).show();
    }

    private void displayProfilePhoto()  {
        if (mProfilePhoto != null) {
            Glide.with(requireContext())
                    .load(mProfilePhoto)
                    .centerCrop()
                    .fitCenter()
                    .into(binding.ivProfilePic);


            saveProfilePictureToDatabase(requireContext(),mProfilePhoto);

        } else {
            Glide.with(requireContext())
                    .load(R.drawable.icons8_profile_picture_48)
                    .placeholder(R.drawable.icons8_profile_picture_48)
                    .centerCrop()
                    .fitCenter()
                    .error(R.drawable.icons8_profile_picture_48)
                    .into(binding.ivProfilePic);


        }
    }


    public void saveProfilePictureToDatabase(Context context, Uri imageUri) {
        try {
            Bitmap bitmap = getBitmapFromUri(context, imageUri);

            if (bitmap != null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();


                ProfilePicture userProfile = new ProfilePicture();
                userProfile.setImage(imagemTratada(byteArray));
                userProfile.setPhone(authViewModel.getPhone());

                insertAccount(userProfile);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        return BitmapFactory.decodeStream(inputStream);
    }


    private void insertAccount(ProfilePicture account) {
        new InsertAccountTask().execute(account);
    }

    private byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }

    private class InsertAccountTask extends AsyncTask<ProfilePicture, Void, Void> {
        @Override
        protected Void doInBackground(ProfilePicture... accounts) {
            for (ProfilePicture account : accounts) {
                // Check if the profile picture with a specific identifier (e.g., user ID) already exists
                ProfilePicture existingProfile = appDatabase.profileDao().getProfilePic(authViewModel.getPhone());

                if (existingProfile != null) {
                    // If it exists, update the existing profile picture with the new data
                    existingProfile.setImage(account.getImage());
                    appDatabase.profileDao().update(existingProfile);
                } else {
                    // If it doesn't exist, insert the new profile picture
                    appDatabase.profileDao().insert(account);
                }
            }
            return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void loadDataFromDb(String phone) {
        new AsyncTask<Void, Void, byte[]>() {
            @Override
            protected byte[] doInBackground(Void... voids) {
                // Perform database query in background thread
                ProfilePicture profilePicture = appDatabase.profileDao().getProfilePic(phone);
                if (profilePicture != null) {
                    return profilePicture.getImage();
                }
                return null;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(byte[] image) {
                if (image != null) {
                    // Use Glide to load the image
                    Glide.with(requireContext())
                            .load(image)
                            .placeholder(R.drawable.icons8_profile_picture_48)
                            .error(R.drawable.icons8_profile_picture_48)
                            .into(binding.ivProfilePic);
                } else {
                    // Handle the case when no profile picture is found
                }
            }
        }.execute();
    }

    public void onBackPressed() {
        if (ProfileFragment.this.getParentFragment() != null && ProfileFragment.this.getParentFragment() instanceof MainFragment) {
            ((MainFragment) ProfileFragment.this.getParentFragment()).navigateToHome();
        }else {
            navigateUp();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackPressed();
            }
        });
        loadDataFromDb(authViewModel.getPhone());


    }

}

