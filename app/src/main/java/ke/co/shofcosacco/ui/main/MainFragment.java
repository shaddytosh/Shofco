package ke.co.shofcosacco.ui.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.FragmentMainBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;
import ke.co.shofcosacco.ui.home.HomeFragment;
import ke.co.shofcosacco.ui.loyaltyPoints.LoyaltyPointsFragment;
import ke.co.shofcosacco.ui.profile.ProfileFragment;
import ke.co.shofcosacco.ui.statements.AccountBalanceStatementFragment;

public class MainFragment extends BaseFragment {



    private MainViewModel mainViewModel;
    private FragmentMainBinding binding;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);

        binding.viewpager.setUserInputEnabled(false);
        MainFragmentAdapter mainFragmentAdapter= new MainFragmentAdapter(this);
        binding.viewpager.setAdapter(mainFragmentAdapter);

        binding.includedBottomNav.bottomNav.setOnItemSelectedListener(item -> {
            int position=0;
            int itemId = item.getItemId();
            if (itemId == R.id.action_home) {
                position = 0;
            } else if (itemId == R.id.action_statements) {
                position = 1;
            }
            else if (itemId == R.id.action_loyalty) {
                position = 2;
            }
            else if (itemId == R.id.action_profile) {
                position = 3;
            }
            mainViewModel.setSelectedPosition(position);
            return true;
        });

        mainViewModel.getSelectedPosition().observe(getViewLifecycleOwner(),position->{
            binding.viewpager.setCurrentItem(position,false);

        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                requireActivity().invalidateOptionsMenu();
            }
        });

        binding.includedBottomNav.fab.setOnClickListener(v -> navigate(MainFragmentDirections.actionMainToDeposits()));

        return  binding.getRoot();
    }



    public void navigateToHome() {
        binding.includedBottomNav.bottomNav.setSelectedItemId(R.id.action_home);
    }
    private static class MainFragmentAdapter extends FragmentStateAdapter {

        public MainFragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
                case 1:
                    return AccountBalanceStatementFragment.newInstance();
                case 2:
                    return LoyaltyPointsFragment.newInstance();
                case 3:
                    return ProfileFragment.newInstance();
                default:
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }

}