package ke.co.shofcosacco.ui.onboarding;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CropImageHelperFragment extends Fragment {

    public enum ImageType {
        FRONT_ID("front"),
        BACK_ID("back"),
        SIGNATURE("signature"),
        PASSPORT("passport");

        private final String value;

        ImageType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public interface OnImageCroppedListener {
        void onImageCropped(Uri croppedImageUri, String base64Data, ImageType imageType);
        void onImageCropError(String error, ImageType imageType);
    }

    private OnImageCroppedListener listener;
    private ImageType currentImageType;
    private Uri cameraImageUri;

    // Gallery image picker
    private final ActivityResultLauncher<Intent> galleryLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri sourceUri = result.getData().getData();
                    if (sourceUri != null) {
                        startCrop(sourceUri);
                    }
                } else {
                    if (listener != null) {
                        listener.onImageCropError("Image selection cancelled", currentImageType);
                    }
                }
            });

    // UCrop result
    private final ActivityResultLauncher<Intent> cropLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri croppedUri = UCrop.getOutput(result.getData());
                    if (croppedUri != null && listener != null) {
                        String base64Data = convertImageToBase64(croppedUri);
                        if (base64Data != null) {
                            listener.onImageCropped(croppedUri, base64Data, currentImageType);
                        } else {
                            listener.onImageCropError("Failed to convert image to base64", currentImageType);
                        }
                    }
                } else if (result.getResultCode() == UCrop.RESULT_ERROR) {
                    Throwable cropError = UCrop.getError(result.getData());
                    String errorMessage = cropError != null ? cropError.getMessage() : "Unknown crop error";
                    if (listener != null) {
                        listener.onImageCropError(errorMessage, currentImageType);
                    }
                }
            });

    // Camera image capture
    private final ActivityResultLauncher<Uri> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
                if (result && cameraImageUri != null) {
                    startCrop(cameraImageUri);
                } else {
                    if (listener != null) {
                        listener.onImageCropError("Camera capture cancelled", currentImageType);
                    }
                }
            });

    public static CropImageHelperFragment newInstance(OnImageCroppedListener listener) {
        CropImageHelperFragment fragment = new CropImageHelperFragment();
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(OnImageCroppedListener listener) {
        this.listener = listener;
    }

    public void launchPicker(ImageType imageType) {
        this.currentImageType = imageType;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    public void launchCamera(ImageType imageType) {
        this.currentImageType = imageType;

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Request the permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 101); // Handle the result in onRequestPermissionsResult
            return;
        }

        // Permission granted - proceed to open camera
        try {
            File photoFile = File.createTempFile(
                    "camera_" + imageType.getValue() + "_" + System.currentTimeMillis(),
                    ".jpg",
                    requireContext().getCacheDir()
            );
            cameraImageUri = FileProvider.getUriForFile(requireContext(),
                    "co.ke.shofcosacco.provider", photoFile);

            cameraLauncher.launch(cameraImageUri);
        } catch (IOException e) {
            Log.e("CropImageHelper", "Failed to create camera file", e);
            if (listener != null) {
                listener.onImageCropError("Failed to create camera file", currentImageType);
            }
        }
    }


    private void startCrop(Uri sourceUri) {
        Uri destUri = Uri.fromFile(new File(requireContext().getCacheDir(),
                "cropped_" + currentImageType.getValue() + "_" + System.currentTimeMillis() + ".jpg"));

        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setCompressionQuality(90);

        UCrop uCrop = UCrop.of(sourceUri, destUri).withOptions(options);

        switch (currentImageType) {
            case FRONT_ID:
            case BACK_ID:
                uCrop = uCrop.withAspectRatio(16f, 10f);
                break;
            case PASSPORT:
                uCrop = uCrop.withAspectRatio(4f, 3f);
                break;
            case SIGNATURE:
                uCrop = uCrop.withAspectRatio(3f, 1f);
                break;
        }

        Intent cropIntent = uCrop.getIntent(requireContext());
        cropLauncher.launch(cropIntent);
    }

    private String convertImageToBase64(Uri imageUri) {
        try {
            Bitmap bitmap = loadBitmapFromUri(imageUri);
            if (bitmap == null) return null;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("CropImageHelper", "Error converting image to base64", e);
            return null;
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            if ("file".equals(uri.getScheme())) {
                return BitmapFactory.decodeFile(uri.getPath());
            } else {
                return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            }
        } catch (IOException e) {
            Log.e("CropImageHelper", "Error loading bitmap from URI", e);
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry opening camera with current imageType
                launchCamera(currentImageType);
            } else {
                if (listener != null) {
                    listener.onImageCropError("Camera permission denied", currentImageType);
                }
            }
        }
    }

}
