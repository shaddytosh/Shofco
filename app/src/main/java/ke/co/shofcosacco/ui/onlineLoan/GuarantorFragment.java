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

public class GuarantorFragment extends BaseFragment implements GuarantorAdapter.Listener {

    private FragmentGuarantorBinding binding;
    private GuarantorAdapter guarantorAdapter;
    private AuthViewModel authViewModel;
    private GuarantorResponse.GuarantorRequest guarantor;

    public GuarantorFragment() {
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
        binding = FragmentGuarantorBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> {
            navigateUp();
        });

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        guarantorAdapter = new GuarantorAdapter(requireContext(), this);
        binding.guarantorRequestsRecyclerView.setAdapter(guarantorAdapter);

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
                if (GuarantorFragment.this.getParentFragment() != null && GuarantorFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) GuarantorFragment.this.getParentFragment()).navigateToHome();
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
                "Getting Guarantor Requests. Please wait...", true);
        authViewModel.getLoansGuarantorRequests().observe(getViewLifecycleOwner(), apiResponse -> {
            progressDialog.dismiss();
            binding.llLoading.setVisibility(View.GONE);
            if (apiResponse != null && apiResponse.isSuccessful()) {
                displayData(apiResponse.body());
            } else {

                showEmptyState();
            }
        });
    }

    private void displayData(GuarantorResponse guarantorResponse) {
        if (guarantorResponse.success.equals(STATUS_CODE_SUCCESS)) {
            List<GuarantorResponse.GuarantorRequest> guarantors = guarantorResponse.guarantors;
            binding.llData.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
            binding.llEmpty.setVisibility(guarantors.isEmpty() ? View.VISIBLE : View.GONE);
            binding.guarantorRequestsRecyclerView.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
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
        binding.llLoading.setVisibility(View.GONE);
    }

    @Override
    public void onClick(GuarantorResponse.GuarantorRequest item) {
        if("Pending".equalsIgnoreCase(item.getApprovalStatus())) {
            sendOtp(item);
            guarantor = item;
        }
    }

    private void sendOtp(GuarantorResponse.GuarantorRequest item){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedCornersDialog);
        View customView = getLayoutInflater().inflate(R.layout.custom_dialog_guarantor, null);
        builder.setView(customView);
        TextView textView = customView.findViewById(R.id.dialog_message);
        textView.setText(String.format("Accept or Reject loan guarantor request from %s", item.getName()));

        AlertDialog alertDialog = builder.create();
        builder.setCancelable(false);

        TextView positiveButton = customView.findViewById(R.id.positive_button);
        positiveButton.setOnClickListener(v -> {

            new AlertDialog.Builder(requireContext())
                    .setTitle("Accept Guarantor Request")
                    .setMessage("Are you sure you want to accept  this request?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("mOTP", authViewModel.getMPIN());
                        args.putString("sType", "accept");
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());

                        alertDialog.dismiss();                        })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();


        });

        TextView cancelButton = customView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(view -> alertDialog.dismiss());

        TextView negativeButton = customView.findViewById(R.id.negative_button);
        negativeButton.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Reject Guarantor Request")
                    .setMessage("Are you sure you want to reject this request?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        OtpConfirmationDialogFragment dialogFragment = new OtpConfirmationDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("mOTP", authViewModel.getMPIN());
                        args.putString("sType", "reject");
                        dialogFragment.setArguments(args);
                        dialogFragment.show(getChildFragmentManager(), dialogFragment.getTag());


                        alertDialog.dismiss();                        })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

        });

        alertDialog.show();

    }



    private void acceptOrRejectGuarantorship(GuarantorResponse.GuarantorRequest item, String otp, String sType){

        if (sType != null ){
            if (sType.equals("accept")){
                ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "",
                        "Accepting guarantor request. Please wait...", true);
                authViewModel.AcceptOrRejectGuarantor(item.getMemberNo(),item.getApplicationNo(), "accept", otp).observe(getViewLifecycleOwner(), apiResponse1 -> {
                    progressDialog1.dismiss();
                    if (apiResponse1 != null && apiResponse1.isSuccessful()) {
                        if (apiResponse1.body().success.equals(STATUS_CODE_SUCCESS)) {
                            successDialog(apiResponse1.body().description);
                        } else {
                            notSuccessDialog(apiResponse1.body().description);
                        }
                    } else {

                        notSuccessDialog("An error occurred. Please try again later");

                    }
                });
            }else if (sType.equals("reject")){
                ProgressDialog progressDialog1 = ProgressDialog.show(getContext(), "",
                        "Rejecting guarantor request. Please wait...", true);
                authViewModel.AcceptOrRejectGuarantor(item.getMemberNo(),item.getApplicationNo(), "reject",otp).observe(getViewLifecycleOwner(), apiResponse1 -> {
                    progressDialog1.dismiss();
                    if (apiResponse1 != null && apiResponse1.isSuccessful()) {
                        if (apiResponse1.body().success.equals(STATUS_CODE_SUCCESS)) {
                            successDialog(apiResponse1.body().description);
                        } else {
                            notSuccessDialog(apiResponse1.body().description);
                        }
                    } else {

                        notSuccessDialog("An error occurred. Please try again later");

                    }
                });
            }
        }


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
        }else if (requestCode == VERIFY_OTP && resultCode == Activity.RESULT_OK && data != null) {
            acceptOrRejectGuarantorship(guarantor,data.getStringExtra("otp"),data.getStringExtra("sType"));
        }
    }
}
