package ke.co.shofcosacco.ui.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ke.co.shofcosacco.app.MainApplication;

public class MainViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> mSelectedPosition=new MutableLiveData<>();
    private final MutableLiveData<String> mAddress=new MutableLiveData<>();

    private final MutableLiveData<String> mSelectedLocation=new MutableLiveData<>();



    public MainViewModel(@NonNull Application application) {
        super(application);
        ((MainApplication) application).getApplicationComponent().inject(this);
        mSelectedPosition.setValue(0);
    }

    public void setSelectedPosition(int position){
        mSelectedPosition.postValue(position);
    }

    public LiveData<Integer> getSelectedPosition(){
        return mSelectedPosition;
    }

    public void setSelectedLocation(String location){
        mSelectedLocation.postValue(location);
    }

    public LiveData<String> getSelectedLocation(){
        return mSelectedLocation;
    }


}
