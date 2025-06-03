package ke.co.shofcosacco.ui.onlineLoan;

import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_EXPIRED_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.VERIFY_OTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentGuarantorBinding;
import co.ke.shofcosacco.databinding.FragmentLoanProductBinding;
import co.ke.shofcosacco.databinding.FragmentOnlineLoansBinding;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;
import ke.co.shofcosacco.app.api.responses.LoanProductsResponse;
import ke.co.shofcosacco.app.models.Guarantor;
import ke.co.shofcosacco.app.models.LoanProduct;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.auth.OtpConfirmationDialogFragment;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class OnlineLoansFragment extends BaseFragment implements OnlineLoansAdapter.Listener {

    private FragmentOnlineLoansBinding binding;
    private OnlineLoansAdapter guarantorAdapter;
    private AuthViewModel authViewModel;

    public OnlineLoansFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOnlineLoansBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });

        binding.btnGoBack.setOnClickListener(v -> navigateUp());


        guarantorAdapter = new OnlineLoansAdapter(requireContext(), this);
        binding.loanProductRecyclerView.setAdapter(guarantorAdapter);

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            getGuarantor();
        });

        getGuarantor();

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (OnlineLoansFragment.this.getParentFragment() != null && OnlineLoansFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) OnlineLoansFragment.this.getParentFragment()).navigateToHome();
                } else {
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

    private void getGuarantor() {
        ProgressDialog progressDialog = ProgressDialog.show(getContext(), "",
                "Getting Online Loans. Please wait...", true);
        authViewModel.getOnlineLoans().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            if (apiResponse != null && apiResponse.isSuccessful()) {
                displayData(apiResponse.body());
            } else {

                showEmptyState();
            }
        });
    }

    private void displayData(GuarantorResponse guarantorResponse) {
        if (guarantorResponse.success.equals(STATUS_CODE_SUCCESS)) {
            List<GuarantorResponse.Loans> guarantors = guarantorResponse.loanlist;
            binding.llData.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
            binding.llEmpty.setVisibility(guarantors.isEmpty() ? View.VISIBLE : View.GONE);
            binding.loanProductRecyclerView.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
            guarantorAdapter.submitList(guarantors);


        } else if (guarantorResponse.success.equals(STATUS_CODE_INVALID_TOKEN) ||
                guarantorResponse.success.equals(STATUS_CODE_INVALID_EXPIRED_TOKEN)) {

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

        } else {
            showEmptyState();
        }

    }

    private void showEmptyState() {
        binding.llEmpty.setVisibility(View.VISIBLE);
        binding.llData.setVisibility(View.GONE);
    }

    @Override
    public void onClick(GuarantorResponse.Loans item) {

       GuarantorResponse.GuarantorRequest[] guarantorRequestList = item.guarantors.toArray(new GuarantorResponse.GuarantorRequest[0]);

        navigate(OnlineLoansFragmentDirections.actionOnlineLoansToOnlineLoanGuarantors(guarantorRequestList));
    }




    private void notSuccessDialog(String message) {
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", 3000);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
    }

    private void successDialog(String message) {
        SuccessDialogFragment dialogFragment = new SuccessDialogFragment();
        Bundle args = new Bundle();
        args.putInt("type", DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DISMISS && resultCode == Activity.RESULT_OK && data != null) {
            getGuarantor();
        }
    }
}
