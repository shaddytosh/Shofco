package ke.co.shofcosacco.app.repositories;

import static ke.co.shofcosacco.app.utils.Constants.STATUS_CODE_SUCCESS;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ke.co.shofcosacco.app.api.APIResponse;
import ke.co.shofcosacco.app.api.ApiCarouselManager;
import ke.co.shofcosacco.app.api.ApiManager;
import ke.co.shofcosacco.app.api.requests.AddNextOfKinRequest;
import ke.co.shofcosacco.app.api.requests.LoanApplicationRequest;
import ke.co.shofcosacco.app.api.responses.AcceptOrRejectGuarantorResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.api.responses.CarouselResponse;
import ke.co.shofcosacco.app.api.responses.ChangePinResponse;
import ke.co.shofcosacco.app.api.responses.CountiesResponse;
import ke.co.shofcosacco.app.api.responses.DashboardResponse;
import ke.co.shofcosacco.app.api.responses.DestinationAccountResponse;
import ke.co.shofcosacco.app.api.responses.EligibilityResponse;
import ke.co.shofcosacco.app.api.responses.ForgotPinResponse;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;
import ke.co.shofcosacco.app.api.responses.HashPinResponse;
import ke.co.shofcosacco.app.api.responses.LoanApplicationResponse;
import ke.co.shofcosacco.app.api.responses.LoanBalanceResponse;
import ke.co.shofcosacco.app.api.responses.LoanProductsResponse;
import ke.co.shofcosacco.app.api.responses.LoanStatementResponse;
import ke.co.shofcosacco.app.api.responses.LoginResponse;
import ke.co.shofcosacco.app.api.responses.LoyaltyResponse;
import ke.co.shofcosacco.app.api.responses.NextOfKinResponse;
import ke.co.shofcosacco.app.api.responses.RepayLoanResponse;
import ke.co.shofcosacco.app.api.responses.ReportsResponse;
import ke.co.shofcosacco.app.api.responses.SecurityQuestionResponse;
import ke.co.shofcosacco.app.api.responses.SendToMobileResponse;
import ke.co.shofcosacco.app.api.responses.SourceAccountResponse;
import ke.co.shofcosacco.app.api.responses.StatementResponse;
import ke.co.shofcosacco.app.api.responses.StkPushResponse;
import ke.co.shofcosacco.app.api.responses.TransactionCostResponse;
import ke.co.shofcosacco.app.api.responses.TransferResponse;
import ke.co.shofcosacco.app.api.responses.ValidateResponse;
import ke.co.shofcosacco.app.api.responses.RegisterResponse;
import ke.co.shofcosacco.app.models.Eligibility;
import ke.co.shofcosacco.app.models.Profile;
import ke.co.shofcosacco.app.sharedprefs.SecurePrefs;


@Singleton
public class AuthRepository {
    private final ApiManager apiManager;
    private final ApiCarouselManager apiCarouselManager;

    private final SecurePrefs securePrefs;

    @Inject
    public AuthRepository(ApiManager apiManager, ApiCarouselManager apiCarouselManager, SecurePrefs securePrefs) {
        this.apiManager = apiManager;
        this.apiCarouselManager = apiCarouselManager;
        this.securePrefs = securePrefs;
    }

    public LiveData<APIResponse<CarouselResponse>> getCarousels() {
        MutableLiveData<APIResponse<CarouselResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CarouselResponse> response = apiCarouselManager.getCarousels();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<HashPinResponse>> hashPin(String phoneNumber, String pin, String newPin) {
        MutableLiveData<APIResponse<HashPinResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<HashPinResponse> response = apiCarouselManager.hashPin(phoneNumber, pin, newPin);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<Profile>> getCustomerData() {
        MutableLiveData<APIResponse<Profile>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<Profile> response = apiManager.getCustomerData();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoginResponse>> login(String memberNo,String password) {
        MutableLiveData<APIResponse<LoginResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoginResponse> response = apiManager.login(memberNo,password);

                if (response.isSuccessful()){
                    if (response.body().success.equals(STATUS_CODE_SUCCESS)){
                        securePrefs.saveMemberNo(memberNo);
                        securePrefs.saveToken(response.body().token);
                    }
                }
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            } catch (GeneralSecurityException e) {
                throw new RuntimeException(e);
            }
        });
        return liveData;
    }


    public LiveData<APIResponse<ForgotPinResponse>> sendOtp(String memberNo, String description) {
        MutableLiveData<APIResponse<ForgotPinResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ForgotPinResponse> response = apiManager.sendOtp(memberNo,description);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ChangePinResponse>> changePassword(String oldPassword, String newPassword,String otp) {
        MutableLiveData<APIResponse<ChangePinResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ChangePinResponse> response = apiManager.changePassword(oldPassword, newPassword, otp);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ValidateResponse>> validateUser(String memberNo,boolean isValidateGuarantor, boolean isRegister) {
        MutableLiveData<APIResponse<ValidateResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ValidateResponse> response = apiManager.validateUser(memberNo,isValidateGuarantor,isRegister);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<RegisterResponse>> register(String memberNo, String password, String nationalId,
                                                            String otp, String questionCode, String answer, String IMEI) {
        MutableLiveData<APIResponse<RegisterResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<RegisterResponse> response = apiManager.register(memberNo, password, nationalId, otp,questionCode,answer,IMEI);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<RegisterResponse>> registerOne(AddNextOfKinRequest addNextOfKinRequest) {
        MutableLiveData<APIResponse<RegisterResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<RegisterResponse> response = apiManager.registerOne(addNextOfKinRequest);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<RegisterResponse>> resetPin(String memberNo, String password, String nationalId,
                                                            String otp, String questionCode, String answer) {
        MutableLiveData<APIResponse<RegisterResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<RegisterResponse> response = apiManager.resetPin(memberNo, password, nationalId, otp,questionCode,answer);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public LiveData<APIResponse<AccountBalanceBosaResponse>> getAccountBalancesBosa() {
        MutableLiveData<APIResponse<AccountBalanceBosaResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<AccountBalanceBosaResponse> response = apiManager.getAccountBalancesBosa();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<AccountBalanceFosaResponse>> getAccountBalancesFosa() {
        MutableLiveData<APIResponse<AccountBalanceFosaResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<AccountBalanceFosaResponse> response = apiManager.getAccountBalancesFosa();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<AccountBalanceBosaResponse>> getAccountBalancesBosaFree() {
        MutableLiveData<APIResponse<AccountBalanceBosaResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<AccountBalanceBosaResponse> response = apiManager.getAccountBalancesBosaFree();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<AccountBalanceFosaResponse>> getAccountBalancesFosaFree() {
        MutableLiveData<APIResponse<AccountBalanceFosaResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<AccountBalanceFosaResponse> response = apiManager.getAccountBalancesFosaFree();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }
    public LiveData<APIResponse<SourceAccountResponse>> getSourceAccount() {
        MutableLiveData<APIResponse<SourceAccountResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<SourceAccountResponse> response = apiManager.getSourceAccount();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<DestinationAccountResponse>> getDestinationAccount() {
        MutableLiveData<APIResponse<DestinationAccountResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<DestinationAccountResponse> response = apiManager.getDestinationAccount();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }


    public LiveData<APIResponse<StatementResponse>> getAccountStatement(String balCode) {
        MutableLiveData<APIResponse<StatementResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<StatementResponse> response = apiManager.getAccountStatement(balCode);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoanBalanceResponse>> getLoanBalance() {
        MutableLiveData<APIResponse<LoanBalanceResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoanBalanceResponse> response = apiManager.getLoanBalance();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<TransferResponse>> transfer(String sourceAccount, String destinationAccount,
                                                            String amount, String transType, String otp) {
        MutableLiveData<APIResponse<TransferResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<TransferResponse> response = apiManager.transfer(sourceAccount, destinationAccount, amount,transType,otp);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoyaltyResponse>> loyalty() {
        MutableLiveData<APIResponse<LoyaltyResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoyaltyResponse> response = apiManager.loyalty();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoanProductsResponse>> loanProducts(String type) {
        MutableLiveData<APIResponse<LoanProductsResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoanProductsResponse> response = apiManager.loanProducts(type);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<GuarantorResponse>> getOnlineLoans() {
        MutableLiveData<APIResponse<GuarantorResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<GuarantorResponse> response = apiManager.getOnlineLoans();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<Eligibility>> loanEligibility(String productCode, String period, String amount) {
        MutableLiveData<APIResponse<Eligibility>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<Eligibility> response = apiManager.loanEligibility(productCode,period,amount);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoanApplicationResponse>> loanApplication(String productCode, String period, String amount, String otp,
                                                                          List<LoanApplicationRequest.Guarantors> guarantorsList, boolean isOnline) {
        MutableLiveData<APIResponse<LoanApplicationResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoanApplicationResponse> response = apiManager.loanApplication(productCode, period,amount,otp,guarantorsList,isOnline);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<DashboardResponse>> dashboardMinList() {
        MutableLiveData<APIResponse<DashboardResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<DashboardResponse> response = apiManager.dashboardMinList();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<TransactionCostResponse>> transactionCost(String transactionType, String amount) {
        MutableLiveData<APIResponse<TransactionCostResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<TransactionCostResponse> response = apiManager.transactionCost(transactionType,amount);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<RepayLoanResponse>> repayLoan(String accountNumber, String amount, String loanNo, String otp) {
        MutableLiveData<APIResponse<RepayLoanResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<RepayLoanResponse> response = apiManager.repayLoan(accountNumber, amount, loanNo,otp);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<LoanStatementResponse>> getLoanStatement(String loanNo) {
        MutableLiveData<APIResponse<LoanStatementResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<LoanStatementResponse> response = apiManager.getLoanStatement(loanNo);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<SecurityQuestionResponse>> securityQuestion() {
        MutableLiveData<APIResponse<SecurityQuestionResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<SecurityQuestionResponse> response = apiManager.securityQuestion();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<StkPushResponse>> stkPush(String accountNo, String mobile, String amount) {
        MutableLiveData<APIResponse<StkPushResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<StkPushResponse> response = apiManager.stkPush(accountNo, mobile, amount);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<SendToMobileResponse>> sendToMobile(String accountNumber, String amount, String phone, String otp) {
        MutableLiveData<APIResponse<SendToMobileResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<SendToMobileResponse> response = apiManager.sendToMobile(accountNumber, amount, phone,otp);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<NextOfKinResponse>> nextOfKin() {
        MutableLiveData<APIResponse<NextOfKinResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<NextOfKinResponse> response = apiManager.nextOfKin();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ReportsResponse>> memberIsLoanGuaranteed() {
        MutableLiveData<APIResponse<ReportsResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ReportsResponse> response = apiManager.memberIsLoanGuaranteed();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<CountiesResponse>> getCounties() {
        MutableLiveData<APIResponse<CountiesResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CountiesResponse> response = apiManager.getCounties();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<CountiesResponse>> getBranchesOrClusters(boolean isBranch) {
        MutableLiveData<APIResponse<CountiesResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CountiesResponse> response = apiManager.getBranchesOrClusters(isBranch);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }
    public LiveData<APIResponse<CountiesResponse>> getRelationshipTypes() {
        MutableLiveData<APIResponse<CountiesResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CountiesResponse> response = apiManager.getRelationshipTypes();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<CountiesResponse>> getSubCounty(String countyCode) {
        MutableLiveData<APIResponse<CountiesResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CountiesResponse> response = apiManager.getSubCounty(countyCode);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<CountiesResponse>> getWards(String subCountyCode) {
        MutableLiveData<APIResponse<CountiesResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<CountiesResponse> response = apiManager.getWards(subCountyCode);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ReportsResponse>> getLoansGuaranteed() {
        MutableLiveData<APIResponse<ReportsResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ReportsResponse> response = apiManager.getLoansGuaranteed();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ReportsResponse>> getDetailedStatement(String dateFrom, String dateTo) {
        MutableLiveData<APIResponse<ReportsResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ReportsResponse> response = apiManager.getDetailedStatement(dateFrom, dateTo);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<ReportsResponse>> FnGetCoroselImages() {
        MutableLiveData<APIResponse<ReportsResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<ReportsResponse> response = apiManager.FnGetCoroselImages();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<GuarantorResponse>> getLoansGuarantorRequests() {
        MutableLiveData<APIResponse<GuarantorResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<GuarantorResponse> response = apiManager.getLoansGuarantorRequests();
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }

    public LiveData<APIResponse<AcceptOrRejectGuarantorResponse>> AcceptOrRejectGuarantor(String memberNo, String loanNo, String type, String otp){
        MutableLiveData<APIResponse<AcceptOrRejectGuarantorResponse>> liveData = new MutableLiveData<>();
        ApiManager.execute(() -> {
            try {
                APIResponse<AcceptOrRejectGuarantorResponse> response = apiManager.AcceptOrRejectGuarantor(memberNo, loanNo, type,otp);
                liveData.postValue(response);
            } catch (IOException e) {
                e.printStackTrace();
                liveData.postValue(null);
            }
        });
        return liveData;
    }



    public String getToken() {
        return apiManager.getToken();
    }
    public String getNationalId() {
        return apiManager.getNationalId();
    }
    public String getMemberNo() {
        return apiManager.getMemberNo();
    }
    public String getFirstName() {
        return apiManager.getFirstName();
    }
    public String getMiddleName() {
        return apiManager.getMiddleName();
    }

    public String getLastName() {
        return apiManager.getLastName();
    }

    public String getTransactionLimit() {
        return apiManager.getTransactionLimit();
    }

    public String getEmail() {
        return apiManager.getEmail();
    }
    public String getPhone() {
        return apiManager.getPhone();
    }

    public String getMPIN() {
        return apiManager.getMPIN();
    }

    public String getMID() {
        return apiManager.getMID();
    }

    public String getBiometric() {
        return apiManager.getBiometric();
    }






}

