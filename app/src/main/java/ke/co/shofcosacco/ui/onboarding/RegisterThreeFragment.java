package ke.co.shofcosacco.ui.onboarding;


import static ke.co.shofcosacco.app.utils.Constants.DISMISS;
import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;
import static ke.co.shofcosacco.app.utils.Constants.SUCCESS;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentRegisterOneBinding;
import co.ke.shofcosacco.databinding.FragmentRegisterThreeBinding;
import ke.co.shofcosacco.app.api.responses.CountiesResponse;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.EditTextValidator;
import ke.co.shofcosacco.app.utils.TextValidator;
import ke.co.shofcosacco.ui.auth.AuthViewModel;
import ke.co.shofcosacco.ui.home.NotSuccessDialogFragment;
import ke.co.shofcosacco.ui.main.SuccessDialogFragment;


public class RegisterThreeFragment extends BaseFragment {

    private FragmentRegisterThreeBinding binding;
    private Handler handler;
    private AuthViewModel authViewModel;

    private final String[] disabilityOptions = {"No", "Yes"};
    private String fullName;
    private String nationalId;
    private String dateOfBirth;
    private String telephone;
    private String email;
    private String town;
    private String address;
    private String selectedGender;
    private String selectedStatus;

    private String clusterCode = "", branchCode = "";

    private List<CountiesResponse.Branch> branchList;
    private ArrayAdapter<CountiesResponse.Branch> branchArrayAdapter;

    private List<CountiesResponse.Cluster> clusterList;
    private ArrayAdapter<CountiesResponse.Cluster> clusterArrayAdapter;


    public RegisterThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterThreeBinding.inflate(inflater, container, false);
        handler = new Handler(Looper.getMainLooper());

        binding.toolbar.setNavigationOnClickListener(v -> navigateUp());

        fullName = RegisterThreeFragmentArgs.fromBundle(getArguments()).getFullName();
        nationalId  = RegisterThreeFragmentArgs.fromBundle(getArguments()).getNationalId();
        dateOfBirth  = RegisterThreeFragmentArgs.fromBundle(getArguments()).getDateOfBirth();
        telephone = RegisterThreeFragmentArgs.fromBundle(getArguments()).getTelephone();
        email = RegisterThreeFragmentArgs.fromBundle(getArguments()).getEmail();
        town = RegisterThreeFragmentArgs.fromBundle(getArguments()).getTown();
        address = RegisterThreeFragmentArgs.fromBundle(getArguments()).getAddress();
        selectedGender = RegisterThreeFragmentArgs.fromBundle(getArguments()).getSelectedGender();
        selectedStatus = RegisterThreeFragmentArgs.fromBundle(getArguments()).getSelectedStatus();

        binding.tvNext.setOnClickListener(v -> validate());


        setupDropdown(binding.autoDisability, disabilityOptions);

        binding.ccpForgotPassword.registerCarrierNumberEditText(binding.phoneNumberEditTextForgotPassword);

        // Handle disability selection
        binding.autoDisability.setOnItemClickListener((adapterView, view1, i, l) -> {
            boolean isYes = disabilityOptions[i].equalsIgnoreCase("Yes");
            binding.tilSpecifyDisability.setVisibility(isYes ? View.VISIBLE : View.GONE);
        });

        binding.autoBranch.setOnItemClickListener((parent, view, position, id) -> {
            if (branchList != null && branchList.size() > 0) {
                branchCode = branchArrayAdapter.getItem(position).code;
            }
        });

        binding.autoCluster.setOnItemClickListener((parent, view, position, id) -> {
            if (clusterList != null && clusterList.size() > 0) {
                clusterCode = clusterArrayAdapter.getItem(position).code;
            }
        });

        getBranches();
        getClusters();

        return binding.getRoot();
    }

    private void getBranches(){
        authViewModel.getBranchesOrClusters(true).observe(getViewLifecycleOwner(), listAPIResponse -> {

            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    branchList = listAPIResponse.body().branches;
                    Comparator<CountiesResponse.Branch> byName = (o1, o2) -> o1.branchName.compareTo(o2.branchName);
                    Collections.sort(branchList, byName);
                    if (branchList != null && branchList.size() > 0){
                        branchArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, branchList);

                        binding.autoBranch.setAdapter(branchArrayAdapter);

                        branchArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        Toast.makeText(requireContext(), "No Branches Available!", Toast.LENGTH_SHORT).show();

                    }
                }
            }else {
                Toast.makeText(requireContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getClusters(){
        authViewModel.getBranchesOrClusters(false).observe(getViewLifecycleOwner(), listAPIResponse -> {

            if (listAPIResponse != null && listAPIResponse.isSuccessful()){
                if (listAPIResponse.body().statusCode.equals(STATUS_CODE_SUCCESS)) {
                    clusterList = listAPIResponse.body().clusters;
                    Comparator<CountiesResponse.Cluster> byName = (o1, o2) -> o1.clusterName.compareTo(o2.clusterName);
                    Collections.sort(clusterList, byName);
                    if (clusterList != null && !clusterList.isEmpty()){
                        clusterArrayAdapter = new ArrayAdapter<>(requireContext(),
                                android.R.layout.simple_spinner_dropdown_item, clusterList);

                        binding.autoCluster.setAdapter(clusterArrayAdapter);

                        clusterArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    }else {
                        Toast.makeText(requireContext(), "No Clusters Available!", Toast.LENGTH_SHORT).show();

                    }
                }
            }else {
                Toast.makeText(requireContext(), Constants.API_ERROR, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void validate() {
        String disability = binding.autoDisability.getText().toString().trim();
        String specifyDisability = binding.etSpecifyDisability.getText().toString().trim();
        String introducerName = binding.etIntroducerName.getText().toString().trim();
        String introducerId = binding.etIntroducerId.getText().toString().trim();
        String introducerPhoneNo= binding.ccpForgotPassword.getFormattedFullNumber().replace(" ","");

        boolean isYes = disability.equalsIgnoreCase("Yes");


       if (branchCode.isEmpty()) {
            Toast.makeText(requireContext(), "Branch is required", Toast.LENGTH_SHORT).show();
        }else if (clusterCode.isEmpty()) {
            Toast.makeText(requireContext(), "Cluster is required", Toast.LENGTH_SHORT).show();
        }else if (disability.isEmpty()) {
            Toast.makeText(requireContext(), "Disability option is required", Toast.LENGTH_SHORT).show();
        }else if(isYes){
           Toast.makeText(requireContext(), "Disability description is required", Toast.LENGTH_SHORT).show();
       }else {

            // Build request model or map
            navigate(RegisterThreeFragmentDirections.actionRegisterOneToRegisterTwo(
                    fullName, nationalId, dateOfBirth, telephone, email, town, address, selectedGender, selectedStatus,
                    branchCode, clusterCode, disability, specifyDisability, introducerName, introducerId, introducerPhoneNo));
        }
    }

    private void setupDropdown(AutoCompleteTextView view, String[] options) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                options
        );
        view.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void notSuccessDialog(String message){
        NotSuccessDialogFragment dialogFragment = new NotSuccessDialogFragment();
        Bundle args= new Bundle();
        args.putInt("type",DISMISS);
        args.putString("message", message);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager(),dialogFragment.getTag());
    }





}