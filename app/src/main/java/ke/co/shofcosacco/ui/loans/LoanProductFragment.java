package ke.co.shofcosacco.ui.loans;


import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_EXPIRED_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentLoanProductBinding;
import ke.co.shofcosacco.app.api.responses.LoanProductsResponse;
import ke.co.shofcosacco.app.models.LoanProduct;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.LiveDataViewModel;
import ke.co.shofcosacco.ui.deposits.DepositsFragment;
import ke.co.shofcosacco.ui.main.MainFragment;

public class LoanProductFragment extends BaseFragment implements LoanProductAdapter.Listener {

    private FragmentLoanProductBinding binding;
    private LoanProductAdapter loanProductAdapter;
    private AuthViewModel authViewModel;
    private LiveDataViewModel liveDataViewModel;

    public LoanProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        liveDataViewModel = new ViewModelProvider(requireActivity()).get(LiveDataViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoanProductBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        loanProductAdapter = new LoanProductAdapter(requireContext(),this);
        binding.loanProductRecyclerView.setAdapter(loanProductAdapter);

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getLoanProduct();
        });

        getLoanProduct();

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (LoanProductFragment.this.getParentFragment() != null && LoanProductFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) LoanProductFragment.this.getParentFragment()).navigateToHome();
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

    private void getLoanProduct() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting loan products. Please wait...", true);
        authViewModel.loanProducts("FOSA").observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                displayData(apiResponse.body());
                liveDataViewModel.setLoanProductsResponseLiveData(apiResponse.body());
            }else {

                showEmptyState();
            }
        });
    }

    private void displayData(LoanProductsResponse loanProductsResponse) {
        if (loanProductsResponse.statusCode.equals(STATUS_CODE_SUCCESS)) {
            List<LoanProduct> loanProducts = loanProductsResponse.loanProducts;
            binding.llData.setVisibility(!loanProducts.isEmpty() ? View.VISIBLE : View.GONE);
            binding.llEmpty.setVisibility(loanProducts.isEmpty() ? View.VISIBLE : View.GONE);
            binding.llLoading.setVisibility(loanProducts.isEmpty() ? View.VISIBLE : View.GONE);
            binding.tvTitle.setVisibility(!loanProducts.isEmpty() ? View.VISIBLE : View.GONE);
            binding.loanProductRecyclerView.setVisibility(!loanProducts.isEmpty() ? View.VISIBLE : View.GONE);
            loanProductAdapter.submitList(loanProducts);

        }else if (loanProductsResponse.statusCode.equals(STATUS_CODE_INVALID_TOKEN) ||
                loanProductsResponse.statusCode.equals(STATUS_CODE_INVALID_EXPIRED_TOKEN)) {

            try {
                authViewModel.removeLoggedInUser();
                Toast.makeText(requireContext(), "Session expired! Please login again", Toast.LENGTH_SHORT).show();
                Intent intent = requireContext().getPackageManager().getLaunchIntentForPackage(requireContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }else {
            showEmptyState();
        }

    }

    private void showEmptyState() {
        binding.llEmpty.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.GONE);
        binding.llLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(LoanProduct item) {
        navigate(LoanProductFragmentDirections.actionLoanProductToApplyLoan(item));
    }
}