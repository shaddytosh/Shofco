package ke.co.shofcosacco.ui.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.inject.Inject;

import ke.co.shofcosacco.app.MainApplication;
import ke.co.shofcosacco.app.api.APIResponse;
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
import ke.co.shofcosacco.app.api.responses.ForgotPinResponse;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;
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
import ke.co.shofcosacco.app.repositories.AuthRepository;
import ke.co.shofcosacco.app.sharedprefs.SecurePrefs;


public class AuthViewModel extends AndroidViewModel {

    @Inject
    AuthRepository authRepository;
    @Inject
    SecurePrefs securePrefs;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        ((MainApplication) application).getApplicationComponent().inject(this);
    }

    public LiveData<APIResponse<CarouselResponse>> getCarousels() {
        return authRepository.getCarousels();
    }


    public LiveData<APIResponse<LoginResponse>> login(String memberNo,String password) {
        return authRepository.login(memberNo, password);
    }

    public LiveData<APIResponse<Profile>> getCustomerData() {
        return authRepository.getCustomerData();
    }

    public LiveData<APIResponse<ChangePinResponse>> changePassword(String oldPassword, String newPassword,String otp) {
        return authRepository.changePassword(oldPassword, newPassword, otp);
    }

    public LiveData<APIResponse<ValidateResponse>> validateUser(String memberNo,boolean isValidateGuarantor) {
        return authRepository.validateUser(memberNo,isValidateGuarantor);
    }



    public LiveData<APIResponse<ForgotPinResponse>> sendOtp(String memberNo, String description) {
        return authRepository.sendOtp(memberNo,description);
    }

    public LiveData<APIResponse<RegisterResponse>> resetPin(String memberNo, String password,
                                                            String nationalId, String otp, String questionCode, String answer) {
        return authRepository.resetPin(memberNo, password, nationalId, otp,questionCode, answer);
    }

    public LiveData<APIResponse<RegisterResponse>> register(String memberNo, String password,
                                                            String nationalId, String otp, String questionCode, String answer, String IMEI) {
        return authRepository.register(memberNo, password, nationalId, otp,questionCode, answer,IMEI);
    }


    public LiveData<APIResponse<RegisterResponse>> registerOne(AddNextOfKinRequest addNextOfKinRequest) {
        return authRepository.registerOne(addNextOfKinRequest);
    }



    public LiveData<APIResponse<AccountBalanceBosaResponse>> getAccountBalancesBosa() {
        return authRepository.getAccountBalancesBosa();
    }

    public LiveData<APIResponse<AccountBalanceFosaResponse>> getAccountBalancesFosa() {
        return authRepository.getAccountBalancesFosa();
    }
    public LiveData<APIResponse<AccountBalanceBosaResponse>> getAccountBalancesBosaFree() {
        return authRepository.getAccountBalancesBosa();
    }

    public LiveData<APIResponse<AccountBalanceFosaResponse>> getAccountBalancesFosaFree() {
        return authRepository.getAccountBalancesFosaFree();
    }

    public LiveData<APIResponse<DestinationAccountResponse>> getDestinationAccount() {
        return authRepository.getDestinationAccount();
    }

    public LiveData<APIResponse<SourceAccountResponse>> getSourceAccount() {
        return authRepository.getSourceAccount();
    }

    public LiveData<APIResponse<StatementResponse>> getAccountStatement(String balCode) {
        return authRepository.getAccountStatement(balCode);
    }

    public LiveData<APIResponse<LoanBalanceResponse>> getLoanBalance() {
        return authRepository.getLoanBalance();
    }

    public LiveData<APIResponse<TransferResponse>> transfer(String sourceAccount, String destinationAccount,
                                                            String amount, String transType, String otp) {
        return authRepository.transfer(sourceAccount, destinationAccount, amount, transType,otp);
    }

    public LiveData<APIResponse<LoyaltyResponse>> loyalty() {
        return authRepository.loyalty();
    }

    public LiveData<APIResponse<LoanProductsResponse>> loanProducts(String type) {
        return authRepository.loanProducts(type);
    }

    public LiveData<APIResponse<GuarantorResponse>> getOnlineLoans() {
        return authRepository.getOnlineLoans();
    }

    public LiveData<APIResponse<Eligibility>> loanEligibility(String productCode, String period, String amount) {
        return authRepository.loanEligibility(productCode,period,amount);
    }

    public LiveData<APIResponse<LoanApplicationResponse>> loanApplication(String productCode, String period,
                                                                          String amount, String otp,
                                                                          List<LoanApplicationRequest.Guarantors> guarantorsList, boolean isOnline) {
        return authRepository.loanApplication(productCode, period,amount,otp,guarantorsList,isOnline);
    }

    public LiveData<APIResponse<DashboardResponse>> dashboardMinList() {
        return authRepository.dashboardMinList();
    }

    public LiveData<APIResponse<TransactionCostResponse>> transactionCost(String transactionType, String amount) {
        return authRepository.transactionCost(transactionType,amount);
    }

    public LiveData<APIResponse<RepayLoanResponse>> repayLoan(String accountNumber, String amount, String loanNo, String otp) {
        return authRepository.repayLoan(accountNumber, amount, loanNo,otp);
    }


    public LiveData<APIResponse<LoanStatementResponse>> getLoanStatement(String loanNo) {
        return authRepository.getLoanStatement(loanNo);
    }

    public LiveData<APIResponse<SecurityQuestionResponse>> securityQuestion() {
        return authRepository.securityQuestion();
    }

    public LiveData<APIResponse<StkPushResponse>> stkPush(String accountNo, String mobile, String amount) {
        return authRepository.stkPush(accountNo, mobile, amount);
    }

    public LiveData<APIResponse<SendToMobileResponse>> sendToMobile(String accountNumber, String amount, String phone, String otp) {
        return authRepository.sendToMobile(accountNumber, amount, phone,otp);
    }

    public LiveData<APIResponse<NextOfKinResponse>> nextOfKin() {
        return authRepository.nextOfKin();
    }

    public LiveData<APIResponse<ReportsResponse>> getLoansGuaranteed() {
        return authRepository.getLoansGuaranteed();
    }

    public LiveData<APIResponse<ReportsResponse>> memberIsLoanGuaranteed() {
        return authRepository.memberIsLoanGuaranteed();
    }


    public LiveData<APIResponse<CountiesResponse>> getCounties() {
        return authRepository.getCounties();
    }

    public LiveData<APIResponse<CountiesResponse>> getBranchesOrClusters(boolean isBranch) {
        return authRepository.getBranchesOrClusters(isBranch);
    }

    public LiveData<APIResponse<CountiesResponse>> getRelationshipTypes() {
        return authRepository.getRelationshipTypes();
    }


    public LiveData<APIResponse<CountiesResponse>> getSubCounty(String countyCode) {
        return authRepository.getSubCounty(countyCode);
    }

    public LiveData<APIResponse<CountiesResponse>> getWards(String subCountyCode) {
        return authRepository.getWards(subCountyCode);
    }

    public LiveData<APIResponse<ReportsResponse>> getDetailedStatement(String dateFrom, String dateTo) {
        return authRepository.getDetailedStatement(dateFrom, dateTo);
    }

    public LiveData<APIResponse<ReportsResponse>> FnGetCoroselImages() {
        return authRepository.FnGetCoroselImages();
    }

    public LiveData<APIResponse<GuarantorResponse>> getLoansGuarantorRequests() {
        return authRepository.getLoansGuarantorRequests();
    }

    public LiveData<APIResponse<AcceptOrRejectGuarantorResponse>> AcceptOrRejectGuarantor(String memberNo, String loanNo, String type, String otp) {
        return authRepository.AcceptOrRejectGuarantor(memberNo, loanNo, type,otp);
    }


    public String getToken() {
        return authRepository.getToken();
    }
    public String getNationalId() {
        return authRepository.getNationalId();
    }

    public String getMemberNo() {
        return authRepository.getMemberNo();
    }
    public String getFirstName() {
        return authRepository.getFirstName();
    }
    public String getMiddleName() {
        return authRepository.getMiddleName();
    }
    public String getLastName() {
        return authRepository.getLastName();
    }

    public String getTransactionLimit() {
        return authRepository.getTransactionLimit();
    }
    public String getEmail() {
        return authRepository.getEmail();
    }
    public String getPhone() {
        return authRepository.getPhone();
    }

    public String getMID() {
        return authRepository.getMID();
    }

    public String getMPIN() {
        return authRepository.getMPIN();
    }

    public String getBiometric() {
        return authRepository.getBiometric();
    }


    public void removeLoggedInUser() throws GeneralSecurityException, IOException {
        securePrefs.savePhone(null);
        securePrefs.saveNationalId(null);
        securePrefs.saveUserId(null);
        securePrefs.saveToken(null);

    }

    public void saveTokenAndMemberNo(String token, String memberNo, String userType) throws GeneralSecurityException, IOException {

        securePrefs.saveMemberNo(memberNo);
        securePrefs.saveToken(token);

    }

    public void saveAccountNo(String accountNo) throws GeneralSecurityException, IOException {
        securePrefs.saveAccountNumber(accountNo);

    }

    public String getAccountNo() throws GeneralSecurityException, IOException {


        return  securePrefs.getAccountNumber();
    }

    public String getName() throws GeneralSecurityException, IOException {


        return  securePrefs.getFirstName();
    }

    public void saveBiometric(String biometric) throws GeneralSecurityException, IOException {
        securePrefs.saveBiometric(biometric);

    }
   public void saveLastName(String memberNo) throws GeneralSecurityException, IOException {
       securePrefs.saveLastName(memberNo);

    }

    public void saveBiometricDetails(String MID, String MPIN) throws GeneralSecurityException, IOException {
        securePrefs.saveMID(MID);
        securePrefs.saveMPIN(MPIN);

    }

    public void saveUser(String email, String memberNo, String name, String phone, String idNo) throws GeneralSecurityException, IOException {

        securePrefs.saveEmail(email);
        securePrefs.saveFirstName(name);
        securePrefs.savePhone(phone);
        securePrefs.saveNationalId(idNo);


    }


}
