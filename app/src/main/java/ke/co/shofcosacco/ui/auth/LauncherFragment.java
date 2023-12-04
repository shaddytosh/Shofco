package ke.co.shofcosacco.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;



import java.util.Timer;
import java.util.TimerTask;

import co.ke.shofcosacco.databinding.FragmentLauncherBinding;
import ke.co.shofcosacco.app.navigation.BaseFragment;

public class LauncherFragment extends BaseFragment {

    private FragmentLauncherBinding launcherBinding;
    private AuthViewModel authViewModel;
    private Timer mTimer;
    private String token, memberNo;

    public LauncherFragment() {
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
        // Inflate the layout for this fragment
        launcherBinding = FragmentLauncherBinding.inflate(inflater, container, false);


        return launcherBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTimer!=null)
            mTimer.cancel();
        mTimer = new Timer("launch");
        mTimer.schedule((new TimerTask() {
            @Override
            public void run() {
                launcherBinding.getRoot().post(()-> {
                    token = authViewModel.getToken();

                    if (token !=null){
                        navigate(LauncherFragmentDirections.actionLauncherToLogin());

                    }else {
                        navigate(LauncherFragmentDirections.actionLauncherToLoginOptions());
                    }
                });
            }
        }), 2000);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        mTimer=null;
    }
}