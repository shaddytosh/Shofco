package ke.co.shofcosacco.ui.home;


import static ke.co.shofcosacco.app.utils.Constants.COMING_SOON;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.compose.ui.graphics.Canvas;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselGravity;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.imaginativeworld.whynotimagecarousel.model.CarouselType;
import org.imaginativeworld.whynotimagecarousel.utils.Utils;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentHomeBinding;
import ke.co.shofcosacco.app.api.responses.ReportsResponse;
import ke.co.shofcosacco.app.api.responses.StatementResponse;
import ke.co.shofcosacco.app.models.Carousel;
import ke.co.shofcosacco.app.models.Dashboard;
import ke.co.shofcosacco.app.models.MiniStatement;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.BlurUtils;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.main.MainFragmentDirections;
import ke.co.shofcosacco.ui.statements.StatementAdapter;


public class HomeFragment extends BaseFragment implements RecentTransactionsAdapter.Listener{

    private FragmentHomeBinding binding;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;
    private long backPressedTime = 0;

    private long lastUserActivity = 0;
    private RecentTransactionsAdapter recentTransactionsAdapter;

    final Handler handler = new Handler();

    private String balance = "0.00";

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(this).get(LiveDataViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        lastUserActivity = System.currentTimeMillis();

        lastUserActivity = System.currentTimeMillis();


        binding.tvFirstName.setText(authViewModel.getFirstName());
        binding.toolbar.setTitle(authViewModel.getFirstName());

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay < 12){
            binding.tvGreetings.setText(getString(R.string.good_morning));
        }else if(timeOfDay < 16){
            binding.tvGreetings.setText(getString(R.string.good_afternoon));
        }else if(timeOfDay < 21){
            binding.tvGreetings.setText(getString(R.string.good_evening));
        }else {
            binding.tvGreetings.setText(getString(R.string.good_ninght));
        }

        binding.layoutTwo.cvDeposit.setOnClickListener(v ->
                navigate(ke.co.shofcosacco.ui.main.MainFragmentDirections.actionMainToDeposits()));

        binding.layoutTwo.cvLoans.setOnClickListener(v ->
                navigate(ke.co.shofcosacco.ui.main.MainFragmentDirections.actionMainToMainLoans()));

        binding.layoutTwo.cvMyAccount.setOnClickListener(v ->
                navigate(ke.co.shofcosacco.ui.main.MainFragmentDirections.actionMainToMyAccount()));


        binding.partialAction.llMoneyTransfer.setOnClickListener(v ->
                navigate(ke.co.shofcosacco.ui.main.MainFragmentDirections.actionMainToTransfer(authViewModel.getTransactionLimit())));

        binding.partialAction.llBuyAirtime.setOnClickListener(v ->toast());

        binding.partialAction.llSendMoney.setOnClickListener(v ->navigate(MainFragmentDirections.actionMainToSendToMobile(authViewModel.getTransactionLimit())));

        binding.layoutThree.cvMyBills.setOnClickListener(v ->toast());

        binding.layoutThree.cvNextOfKin.setOnClickListener(v ->
                navigate(MainFragmentDirections.actionMainToNextOfKin()));

        binding.layoutThree.cvReports.setOnClickListener(v ->
                navigate(MainFragmentDirections.actionMainToReports()));

        binding.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                    binding.toolbar.setVisibility(View.GONE);
                    binding.collapsingToolbar.setTitleEnabled(false);
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true;
                    // Collapsed
                    binding.toolbar.setVisibility(View.VISIBLE);
                } else if (isShow) {
                    isShow = false;
                    // Expanded
                    binding.toolbar.setVisibility(View.GONE);
                    binding.collapsingToolbar.setTitleEnabled(false);
                }else {
                    binding.toolbar.setVisibility(View.GONE);
                    binding.collapsingToolbar.setTitleEnabled(false);
                }
            }
        });


        setCarousel();

        recentTransactionsAdapter = new RecentTransactionsAdapter(requireContext(),this);
        binding.statementRecyclerView.setAdapter(recentTransactionsAdapter);

        getStatement();

        binding.tvViewAll.setOnClickListener(view -> navigate(MainFragmentDirections.actionMainToStatements("ORDINARY","ORDINARY")));

        binding.tvShowBalance.setText(String.format("%s %s", Constants.CURRENCY,balance));
        binding.tvHideBalance.setText(String.format("%s %s", Constants.CURRENCY, balance));

        hideBalance();

        binding.ivShowBalance.setOnClickListener(v -> showBalance());
        binding.ivHideBalance.setOnClickListener(v -> hideBalance());

        return binding.getRoot();

    }


    private void showBalance() {
        binding.ivHideBalance.setVisibility(View.VISIBLE);
        binding.ivShowBalance.setVisibility(View.INVISIBLE);

        binding.tvHideBalance.setVisibility(View.INVISIBLE);
        binding.tvShowBalance.setVisibility(View.VISIBLE);
    }


    private void hideBalance() {
        if (Build.VERSION.SDK_INT >= 11) {
            binding.tvHideBalance.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        float radius = binding.tvHideBalance.getTextSize() / 3;
        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
        binding.tvHideBalance.getPaint().setMaskFilter(filter);
        binding.ivHideBalance.setVisibility(View.INVISIBLE);
        binding.ivShowBalance.setVisibility(View.VISIBLE);

        binding.tvHideBalance.setVisibility(View.VISIBLE);
        binding.tvShowBalance.setVisibility(View.INVISIBLE);
    }




    private void toast(){
        Toast.makeText(requireContext(), COMING_SOON, Toast.LENGTH_SHORT).show();
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




    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            // If the back button has been pressed twice within 2 seconds, exit the app
            requireActivity().finish();
            try {
                authViewModel.removeLoggedInUser();
            } catch (GeneralSecurityException | IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Otherwise, show a toast message asking the user to press back again to exit
            Toast.makeText(requireActivity(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
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

        startInactivityTimer();


    }

    private void startInactivityTimer() {
        final long TIMEOUT = 60 * 1000; // 1 minute
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                long timeSinceLastActivity = System.currentTimeMillis() - lastUserActivity;
                if (timeSinceLastActivity >= TIMEOUT) {
                    // User has been inactive for 2 minutes, log them out
                    logoutUser();
                } else {
                    handler.postDelayed(this, TIMEOUT - timeSinceLastActivity);
                }
            }
        };
        handler.postDelayed(runnable, TIMEOUT);
    }

    private void logoutUser() {
        try {
            authViewModel.removeLoggedInUser();
            Toast.makeText(requireContext(),"Session expired. Please login again", Toast.LENGTH_LONG).show();

            Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
        cancelInactivityTimer();
    }

    private void cancelInactivityTimer() {
        handler.removeCallbacksAndMessages(null);
    }


    private void getStatement() {
        authViewModel.dashboardMinList().observe(getViewLifecycleOwner(), apiResponse -> {
            boolean isSuccess = apiResponse != null && apiResponse.isSuccessful();
            if (isSuccess){
                List<Dashboard> miniStatements = apiResponse.body().success.equals(STATUS_CODE_SUCCESS) ? apiResponse.body().minlist : Collections.emptyList();
                binding.llData.setVisibility(!miniStatements.isEmpty() ? View.VISIBLE : View.GONE);
                binding.llEmpty.setVisibility(miniStatements.isEmpty() ? View.VISIBLE : View.GONE);

                recentTransactionsAdapter.submitList(miniStatements);
                binding.statementRecyclerView.setVisibility(!miniStatements.isEmpty() ? View.VISIBLE : View.GONE);

                binding.tvShowBalance.setText(String.format("%s %s", Constants.CURRENCY, !miniStatements.isEmpty() ? apiResponse.body().balance: 0.00));
                binding.tvHideBalance.setText(String.format("%s %s", Constants.CURRENCY, !miniStatements.isEmpty() ? apiResponse.body().balance: 0.00));
                binding.tvBalanceView.setText(!miniStatements.isEmpty() ? apiResponse.body().bal_code: "Balance");

            }else {
                showEmptyState();
            }

        });
    }


    private void showEmptyState() {
        binding.llEmpty.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.GONE);
    }

    @Override
    public void onClick(Dashboard item) {

    }


}