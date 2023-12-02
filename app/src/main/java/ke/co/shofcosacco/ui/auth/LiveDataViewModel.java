package ke.co.shofcosacco.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import ke.co.shofcosacco.app.MainApplication;
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.api.responses.CarouselResponse;
import ke.co.shofcosacco.app.api.responses.LoanProductsResponse;
import ke.co.shofcosacco.app.api.responses.NextOfKinResponse;
import ke.co.shofcosacco.app.api.responses.StatementResponse;
import ke.co.shofcosacco.app.models.Profile;


public class LiveDataViewModel extends AndroidViewModel {

    private final MutableLiveData<AccountBalanceBosaResponse> accountBalanceBosaResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<AccountBalanceFosaResponse> accountBalanceFosaResponseMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<StatementResponse> statementResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<LoanProductsResponse> loanProductsResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<CarouselResponse> carouselResponseMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<NextOfKinResponse> nextOfKinResponseMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<Profile> profileMutableLiveData = new MutableLiveData<>();





    public LiveDataViewModel(@NonNull Application application) {
        super(application);
        ((MainApplication) application).getApplicationComponent().inject(this);
    }

    public LiveData<AccountBalanceFosaResponse> getAccountBalancesFosaLiveData() {
        return accountBalanceFosaResponseMutableLiveData;
    }

    public void setAccountBalancesFosaLiveData(AccountBalanceFosaResponse accountBalanceBosaResponse) {
        accountBalanceFosaResponseMutableLiveData.setValue(accountBalanceBosaResponse);
    }

    public LiveData<AccountBalanceBosaResponse> getAccountBalancesBosaLiveData() {
        return accountBalanceBosaResponseMutableLiveData;
    }

    public void setAccountBalancesBosaLiveData(AccountBalanceBosaResponse accountBalanceBosaResponse) {
        accountBalanceBosaResponseMutableLiveData.setValue(accountBalanceBosaResponse);
    }
    public LiveData<StatementResponse> getStatementResponseLiveData() {
        return statementResponseMutableLiveData;
    }

    public void setStatementResponseLiveData(StatementResponse statementResponse) {
        statementResponseMutableLiveData.setValue(statementResponse);
    }

    public LiveData<LoanProductsResponse> getLoanProductsResponseLiveData() {
        return loanProductsResponseMutableLiveData;
    }

    public void setLoanProductsResponseLiveData(LoanProductsResponse loanProductsResponse) {
        loanProductsResponseMutableLiveData.setValue(loanProductsResponse);
    }

    public LiveData<CarouselResponse>getCarouselResponseMutableLiveData () {
        return carouselResponseMutableLiveData;
    }

    public void setCarouselResponseMutableLiveData(CarouselResponse carouselResponse) {
        carouselResponseMutableLiveData.setValue(carouselResponse);
    }

    public LiveData<NextOfKinResponse>getNextOfKinResponseMutableLiveData () {
        return nextOfKinResponseMutableLiveData;
    }

    public void setNextOfKinResponseMutableLiveData(NextOfKinResponse nextOfKinResponse) {
        nextOfKinResponseMutableLiveData.setValue(nextOfKinResponse);
    }

    public LiveData<Profile>getProfileMutableLiveData () {
        return profileMutableLiveData;
    }

    public void setProfileMutableLiveData(Profile profile) {
        profileMutableLiveData.setValue(profile);
    }


}
