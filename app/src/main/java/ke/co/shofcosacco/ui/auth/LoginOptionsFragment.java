package ke.co.shofcosacco.ui.auth;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VALIDATE_TO_REGISTER;
import static ke.co.shofcosacco.app.utils.ViewUtils.drawableToBitmap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselGravity;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.model.CarouselType;
import org.imaginativeworld.whynotimagecarousel.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentLoginOptionsBinding;
import ke.co.shofcosacco.app.api.responses.ReportsResponse;
import ke.co.shofcosacco.app.models.Carousel;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.PreventDoubleClick;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;


public class LoginOptionsFragment extends BaseFragment {

    private FragmentLoginOptionsBinding binding;
    private AppUpdateManager appUpdateManager;
    private static final int APP_UPDATE_REQUEST_CODE = 100;
    private AuthViewModel authViewModel;
    public LoginOptionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appUpdateManager = AppUpdateManagerFactory.create(requireContext());
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginOptionsBinding.inflate(inflater, container, false);

        checkNewUpdate();

        binding.cvLogin.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            navigate(LoginOptionsFragmentDirections.actionLoginOptionsToLogin());

        });
        binding.cvRegister.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            validateDialog(false,false);
        });

        binding.cvOpenAccount.setOnClickListener(v -> {

            PreventDoubleClick.preventMultiClick(v);
            validateDialog(false, true);
        });



        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_t_and_c) {
                terms();
            } else if (itemId == R.id.action_information) {
                website();
            }
            else if (itemId == R.id.action_help) {
                openDialog();
            }
            return true;
        });

        setCarousel();



        return binding.getRoot();
    }

    private void validateDialog(boolean isResetPin, boolean isRegister){
        ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isResetPin", isResetPin);
        args.putBoolean("isRegister", isRegister);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALIDATE_TO_REGISTER && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginOptionsFragmentDirections.actionLoginToRegister(data.getStringExtra("member_no")));

        }else  if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            notSuccessDialog(data.getStringExtra("message"));

        }else if (requestCode == 4000 && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginOptionsFragmentDirections.actionLoginToResetPin(data.getStringExtra("member_no")));
        }else if (requestCode == 5000 && resultCode == Activity.RESULT_OK && data != null) {

            navigate(LoginOptionsFragmentDirections.actionLoginToOpenAccount(data.getStringExtra("id_no")));
        }
    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
    private void openDialog(){
        HelpBottomSheetFragment dialogFragment = new HelpBottomSheetFragment();
        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
    }

    private void website() {
        String url = "https://shofcosacco.com/";

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Shofco Website");
        builder.setMessage("Do you want to open the website?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();

    }


    private void terms() {
        String url = "https://shofcosacco.com/faqs/";

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Shofco Terms");
        builder.setMessage("Do you want to open the website?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();

    }



    private void setCarousel() {
        ImageCarousel carousel = binding.carousel;

        carousel.registerLifecycle(getLifecycle());

        carousel.setShowTopShadow(false);
        carousel.setTopShadowAlpha(0.6f); // 0 to 1, 1 means 100%
        carousel.setTopShadowHeight(Utils.dpToPx(32, requireContext())); // px value of dp

        carousel.setShowBottomShadow(true);
        carousel.setBottomShadowAlpha(0.7f); // 0 to 1, 1 means 100%
        carousel.setBottomShadowHeight(Utils.dpToPx(48, requireContext())); // px value of dp

        carousel.setShowCaption(true);
        carousel.setCaptionMargin(Utils.dpToPx(8, requireContext())); // px value of dp
        carousel.setCaptionTextSize(Utils.spToPx(20, requireContext())); // px value of sp

        carousel.setShowIndicator(true);
        carousel.setIndicatorMargin(Utils.dpToPx(0, requireContext())); // px value of dp

        carousel.setImageScaleType(ImageView.ScaleType.FIT_XY);
        carousel.setAutoWidthFixing(true);
        carousel.setTouchToPause(true);
        carousel.setShowCaption(true);

        carousel.setCarouselBackground(new ColorDrawable(Color.parseColor("#333333")));
        carousel.setImagePlaceholder(ContextCompat.getDrawable(
                requireContext(),
                R.drawable.mama_mboga
        ));

        carousel.setCarouselPadding(Utils.dpToPx(0, requireContext()));
        carousel.setCarouselPaddingStart(Utils.dpToPx(0, requireContext()));
        carousel.setCarouselPaddingTop(Utils.dpToPx(0, requireContext()));
        carousel.setCarouselPaddingEnd(Utils.dpToPx(0, requireContext()));
        carousel.setCarouselPaddingBottom(Utils.dpToPx(0, requireContext()));

        carousel.setShowNavigationButtons(false);

        carousel.setCarouselType(CarouselType.SHOWCASE);
        carousel.setCarouselGravity(CarouselGravity.CENTER);

        carousel.setScaleOnScroll(false);
        carousel.setScalingFactor(.15f);
        carousel.setAutoWidthFixing(true);
        carousel.setAutoPlay(true);
        carousel.setAutoPlayDelay(10000); // Milliseconds
        carousel.setInfiniteCarousel(true);
        carousel.setTouchToPause(true);


        authViewModel.FnGetCoroselImages().observe(getViewLifecycleOwner(), listAPIResponse -> {
            if (listAPIResponse != null && listAPIResponse.isSuccessful()) {
                if (listAPIResponse.body().statusCode != null && listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    List<ReportsResponse.Carousel> carouselList = listAPIResponse.body().carouselList;

                    if (carouselList != null && !carouselList.isEmpty()) {
                        // Create a new list to hold carousel items
                        List<CarouselItem> carouselItems = new ArrayList<>();

                        // Loop through each carousel item from the API response
                        for (ReportsResponse.Carousel carouselItem : carouselList) {
                            String  imageUrl = carouselItem.url;
                            if (imageUrl != null) {
                                // Use the URI to add the image to the carousel
                                carouselItems.add(new CarouselItem(imageUrl, ""));
                            }
                        }


                        // Check if we have valid items to display
                        if (!carouselItems.isEmpty()) {
                            // Set the carousel data
                            binding.carousel.addData(carouselItems);
                            binding.carousel.setVisibility(View.VISIBLE);
                            binding.deals.setVisibility(View.VISIBLE);

                        } else {
                            binding.carousel.setVisibility(View.GONE); // No valid images
                            binding.deals.setVisibility(View.GONE);

                        }
                    } else {
                        binding.carousel.setVisibility(View.GONE); // Empty list
                        binding.deals.setVisibility(View.GONE);
                    }
                } else {
                    binding.carousel.setVisibility(View.GONE);
                    binding.deals.setVisibility(View.GONE);// API returned error
                }
            } else {
                binding.carousel.setVisibility(View.GONE); // Network or other issue
                binding.deals.setVisibility(View.GONE);
            }
        });


    }


    private void checkNewUpdate(){
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, requireActivity(), APP_UPDATE_REQUEST_CODE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}