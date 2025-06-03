package ke.co.shofcosacco.ui.onlineLoan;

import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_EXPIRED_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_INVALID_TOKEN;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import android.app.Activity;
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
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentOnlineLoansBinding;
import ke.co.shofcosacco.app.api.requests.LoanApplicationRequest;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.MainFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;

public class OnlineLoansGuarantorFragment extends BaseFragment implements OnlineLoansGuaranterAdapter.Listener {

    private FragmentOnlineLoansBinding binding;
    private OnlineLoansGuaranterAdapter guarantorAdapter;
    private AuthViewModel authViewModel;

    private GuarantorResponse.GuarantorRequest[] guarantorRequests;

    public OnlineLoansGuarantorFragment() {
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


        guarantorRequests = OnlineLoansGuarantorFragmentArgs.fromBundle(getArguments()).getGuarantors();

        binding.btnGoBack.setOnClickListener(v -> navigateUp());

        guarantorAdapter = new OnlineLoansGuaranterAdapter(requireContext(), this);
        binding.loanProductRecyclerView.setAdapter(guarantorAdapter);

        binding.swipeToRefresh.setColorSchemeResources(R.color.primary);

        binding.swipeToRefresh.setOnRefreshListener(() -> {
            binding.swipeToRefresh.setRefreshing(false);
            displayData(guarantorRequests);
        });

        displayData(guarantorRequests);

        binding.ivHome.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to navigate to the home screen?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if (OnlineLoansGuarantorFragment.this.getParentFragment() != null && OnlineLoansGuarantorFragment.this.getParentFragment() instanceof MainFragment) {
                    ((MainFragment) OnlineLoansGuarantorFragment.this.getParentFragment()).navigateToHome();
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

    private void displayData(GuarantorResponse.GuarantorRequest[] guarantorList) {
        List<GuarantorResponse.GuarantorRequest> guarantors = Arrays.asList(guarantorList);
        binding.llData.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
        binding.llEmpty.setVisibility(guarantors.isEmpty() ? View.VISIBLE : View.GONE);
        binding.loanProductRecyclerView.setVisibility(!guarantors.isEmpty() ? View.VISIBLE : View.GONE);
        guarantorAdapter.submitList(guarantors);

    }

    @Override
    public void onClick(GuarantorResponse.GuarantorRequest item) {

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

    }
}
