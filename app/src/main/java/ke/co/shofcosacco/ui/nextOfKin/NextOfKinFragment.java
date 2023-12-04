package ke.co.shofcosacco.ui.nextOfKin;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentNextOfKinBinding;
import ke.co.shofcosacco.app.api.responses.NextOfKinResponse;
import ke.co.shofcosacco.app.models.NextOfKin;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class NextOfKinFragment extends BaseFragment  implements NextOfKinAdapter.Listener{

    private FragmentNextOfKinBinding binding;
    private NextOfKinAdapter nextOfKinAdapter;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;

    public NextOfKinFragment() {
        // Required empty public constructor
    }

    public static NextOfKinFragment newInstance() {
        return new NextOfKinFragment();
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
        binding = FragmentNextOfKinBinding.inflate(inflater, container, false);


        nextOfKinAdapter = new NextOfKinAdapter(requireContext(),this);

        binding.nextOfKinRecyclerView.setAdapter(nextOfKinAdapter);

        binding.toolbar.setNavigationOnClickListener(v -> {
            if (NextOfKinFragment.this.getParentFragment() != null && NextOfKinFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) NextOfKinFragment.this.getParentFragment()).navigateToHome();
            }else {
                navigateUp();
            }
        });
        binding.btnGoBack.setOnClickListener(v -> {
            if (NextOfKinFragment.this.getParentFragment() != null && NextOfKinFragment.this.getParentFragment() instanceof MainFragment) {
                ((MainFragment) NextOfKinFragment.this.getParentFragment()).navigateToHome();
            }else {
                navigateUp();
            }
        });

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            nextOfKin();
        });


        if (liveDataViewModel.getNextOfKinResponseMutableLiveData().getValue() == null) {
            nextOfKin();
        } else {
            displayData(liveDataViewModel.getNextOfKinResponseMutableLiveData().getValue());
        }

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (NextOfKinFragment.this.getParentFragment() != null && NextOfKinFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) NextOfKinFragment.this.getParentFragment()).navigateToHome();
                }else {
                    navigateUp();
                }
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> {
                // Dismiss the dialog if "No" is clicked
                dialogInterface.dismiss();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        return binding.getRoot();

    }

    private void nextOfKin() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Loading. Please wait...", true);

        authViewModel.nextOfKin().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);

            if (apiResponse != null && apiResponse.isSuccessful()) {

                boolean isSuccess = apiResponse.body().statusCode.equals(STATUS_CODE_SUCCESS);

                if (isSuccess){
                    displayData(apiResponse.body());
                    liveDataViewModel.setNextOfKinResponseMutableLiveData(apiResponse.body());
                }else {
                    binding.llEmpty.setVisibility(View.VISIBLE);
                    binding.llData.setVisibility(View.GONE);
                }
            } else {
                binding.llEmpty.setVisibility(View.VISIBLE);
                binding.llData.setVisibility(View.GONE);
            }

        });
    }

    private void displayData(NextOfKinResponse nextOfKinResponse){

        boolean isNextOfKinEmpty = nextOfKinResponse.nextOfKin.isEmpty();

        binding.llEmpty.setVisibility(isNextOfKinEmpty ? View.VISIBLE : View.GONE);
        binding.llLoading.setVisibility(isNextOfKinEmpty ? View.VISIBLE : View.GONE);
        binding.llData.setVisibility(!isNextOfKinEmpty ? View.VISIBLE : View.GONE);

        nextOfKinAdapter.submitList(nextOfKinResponse.nextOfKin);

        binding.nextOfKinRecyclerView.setVisibility(nextOfKinResponse.nextOfKin.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(NextOfKin item) {
    }

}