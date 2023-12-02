package ke.co.shofcosacco.ui.reports;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import co.ke.shofcosacco.databinding.FragmentReportsBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;

public class ReportsFragment extends BaseFragment {

    private FragmentReportsBinding binding;

    public ReportsFragment() {
        // Required empty public constructor
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportsBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        binding.layoutTwo.cvSavingsStatement.setOnClickListener(view -> navigate(ReportsFragmentDirections.actionReportsToSavingsStatements()));
        binding.layoutTwo.cvDetailedStatement.setOnClickListener(v ->  navigate(ReportsFragmentDirections.actionMainToMemberIsLoanQuaranteed()));
        binding.layoutThree.cvLoansGuaranteed.setOnClickListener(v ->  navigate(ReportsFragmentDirections.actionMainToLoansGuaranteed()));
        binding.layoutThree.cvMemberIsLoanGuaranteed.setOnClickListener(v -> navigate(ReportsFragmentDirections.actionMainToDetailedStatement()));

        return binding.getRoot();

    }


}