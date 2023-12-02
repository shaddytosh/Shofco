package ke.co.shofcosacco.ui.auth;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
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
            validateDialog(false);
        });

        binding.cvOpenAccount.setOnClickListener(v -> {
            PreventDoubleClick.preventMultiClick(v);
            navigate(LoginOptionsFragmentDirections.actionLoginToOpenAccount());
        });

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_t_and_c) {

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

    private void validateDialog(boolean isResetPin){
        ValidateDialogFragment dialogFragment = new ValidateDialogFragment();
        Bundle args = new Bundle();
        args.putBoolean("isResetPin", isResetPin);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VALIDATE_TO_REGISTER && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginFragmentDirections.actionLoginToRegister(data.getStringExtra("member_no")));

        }else  if (requestCode == 3000 && resultCode == Activity.RESULT_OK && data != null) {
            notSuccessDialog(data.getStringExtra("message"));

        }else if (requestCode == 4000 && resultCode == Activity.RESULT_OK && data != null) {
            navigate(LoginFragmentDirections.actionLoginToResetPin(data.getStringExtra("member_no")));
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

        List<CarouselItem> list = new ArrayList<>();

        list.add(new CarouselItem("https://shofcosacco.com/wp-content/uploads/2023/11/inigo-de-la-maza-s285sDw5Ikc-unsplash-1024x683.jpg", "Mama Mboga"));
        list.add(new CarouselItem("https://shofcosacco.com/wp-content/uploads/2023/11/DSC_3087-1-1-1024x684.jpg", "Boda Boda"));
        list.add(new CarouselItem("https://shofcosacco.com/wp-content/uploads/2023/11/niels-steeman-9oHlADjtBTQ-unsplash-1024x683.jpg", "Salaries"));
        list.add(new CarouselItem("https://shofcosacco.com/wp-content/uploads/2023/11/cytonn-photography-n95VMLxqM2I-unsplash-1024x684.jpg", "Business Community"));
        list.add(new CarouselItem("https://shofcosacco.com/wp-content/uploads/2023/11/devin-avery-lhAy4wmkjSk-unsplash-1024x683.jpg", "Youth Community"));



        carousel.addData(list);

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