package ke.co.shofcosacco.app.api;


import ke.co.shofcosacco.app.api.requests.AcceptOrRejectGuarantorRequest;
import ke.co.shofcosacco.app.api.requests.AccountBalanceRequest;
import ke.co.shofcosacco.app.api.requests.AccountSummaryRequest;
import ke.co.shofcosacco.app.api.requests.AddNextOfKinRequest;
import ke.co.shofcosacco.app.api.requests.ChangePasswordRequest;
import ke.co.shofcosacco.app.api.requests.DashboardRequest;
import ke.co.shofcosacco.app.api.requests.EligibilityRequest;
import ke.co.shofcosacco.app.api.requests.ForgotPasswordRequest;
import ke.co.shofcosacco.app.api.requests.GuarantorRequest;
import ke.co.shofcosacco.app.api.requests.HashPinRequest;
import ke.co.shofcosacco.app.api.requests.LoanApplicationRequest;
import ke.co.shofcosacco.app.api.requests.LoanBalanceRequest;
import ke.co.shofcosacco.app.api.requests.LoanProductRequest;
import ke.co.shofcosacco.app.api.requests.LoanRepaymentRequest;
import ke.co.shofcosacco.app.api.requests.LoanStatementRequest;
import ke.co.shofcosacco.app.api.requests.LoginRequest;
import ke.co.shofcosacco.app.api.requests.MainRequest;
import ke.co.shofcosacco.app.api.requests.NextOfKinRequest;
import ke.co.shofcosacco.app.api.requests.ProfileRequest;
import ke.co.shofcosacco.app.api.requests.RegisterOneRequest;
import ke.co.shofcosacco.app.api.requests.RegisterRequest;
import ke.co.shofcosacco.app.api.requests.ReportsRequest;
import ke.co.shofcosacco.app.api.requests.ResetPinRequest;
import ke.co.shofcosacco.app.api.requests.SendToMobileRequest;
import ke.co.shofcosacco.app.api.requests.StatementRequest;
import ke.co.shofcosacco.app.api.requests.StkPushRequest;
import ke.co.shofcosacco.app.api.requests.TransactionCostRequest;
import ke.co.shofcosacco.app.api.requests.TransferRequest;
import ke.co.shofcosacco.app.api.requests.ValidateRequest;
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
import ke.co.shofcosacco.app.utils.Constants;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RestApi {

    @POST(Constants.CAROUSEL_ENDPOINT)
    @Headers({"Content-Type: application/json"})
    Call<CarouselResponse> getCarousels();

    @POST(Constants.HASH_ENDPOINT)
    @Headers({"Content-Type: application/json"})
    Call<HashPinResponse> hashPin(@Body HashPinRequest request);

    @POST("api/au_mobile_apis/FnLoginMember")
    @Headers({"Content-Type: application/json"})
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/otpcode/FnSendOTPCode")
    @Headers({"Content-Type: application/json"})
    Call<ForgotPinResponse> sendOtp(@Body ForgotPasswordRequest request);

    @POST("api/au_mobile_apis/FnChangePassword")
    @Headers({"Content-Type: application/json"})
    Call<ChangePinResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("api/au_mobile_apis/FnValidateifRegistered")
    @Headers({"Content-Type: application/json"})
    Call<ValidateResponse> validateUser(@Body ValidateRequest request);

    @POST("api/au_mobile_apis/FnGuarantorID")
    @Headers({"Content-Type: application/json"})
    Call<ValidateResponse> validateGuarantor(@Body ValidateRequest request);

    @POST("api/au_mobile_apis/FnSignUpMember")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/au_mobile_apis/RegisternewMember")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> registerOne(@Body AddNextOfKinRequest request);


    @POST("api/au_mobile_apis/forgotpassword")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> resetPin(@Body ResetPinRequest request);

    @POST(Constants.ENDPOINT)
    @Headers({"Content-Type: application/json"})
    Call<LoyaltyResponse> loyalty(@Body MainRequest request);

    @POST("api/au_mobile_apis/FnGetMemberInfo")
    @Headers({"Content-Type: application/json"})
    Call<Profile> getCustomerData(@Body ProfileRequest request);

    @POST("api/au_mobile_apis/FnGetaccountBalancesBOSA")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceBosaResponse> getAccountBalancesBosa(@Body AccountBalanceRequest request);

    @POST("api/au_mobile_apis/FnGetaccountBalancesFOSA")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceFosaResponse> getAccountBalancesFosa(@Body AccountBalanceRequest request);

    @POST("api/au_mobile_apis/FnGetaccountBalancesBOSAwithNoCharges")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceBosaResponse> getAccountBalancesBosaFree(@Body AccountBalanceRequest request);

    @POST("api/au_mobile_apis/FnGetaccountBalancesFOSAwithNoCharges")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceFosaResponse> getAccountBalancesFosaFree(@Body AccountBalanceRequest request);

    @POST("api/au_mobile_apis/FnGetMinistatement")
    @Headers({"Content-Type: application/json"})
    Call<StatementResponse> getAccountStatement(@Body StatementRequest request);

    @POST("api/au_mobile_apis/FnGetLoansStatement")
    @Headers({"Content-Type: application/json"})
    Call<LoanStatementResponse> getLoanStatement(@Body LoanStatementRequest request);

    @POST("api/au_mobile_apis/FnGetallLoansBalances")
    @Headers({"Content-Type: application/json"})
    Call<LoanBalanceResponse> getLoanBalance(@Body LoanBalanceRequest request);

    @POST("api/au_mobile_apis/FnGetSourceAccounts")
    @Headers({"Content-Type: application/json"})
    Call<SourceAccountResponse> getSourceAccount(@Body AccountSummaryRequest request);

    @POST("api/au_mobile_apis/Fngetdestinationaccounts")
    @Headers({"Content-Type: application/json"})
    Call<DestinationAccountResponse> getDestinationAccount(@Body AccountSummaryRequest request);

    @POST("api/au_mobile_apis/FnProcessTransfer")
    @Headers({"Content-Type: application/json"})
    Call<TransferResponse> transfer(@Body TransferRequest request);

    @GET("api/au_mobile_apis/LoanProductDetails")
    @Headers({"Content-Type: application/json"})
    Call<LoanProductsResponse> loanProducts();

    @POST("api/au_mobile_apis/GetEligibility")
    @Headers({"Content-Type: application/json"})
    Call<Eligibility> loanEligibility(@Body EligibilityRequest request);

    @POST("api/au_mobile_apis/ProcessLoan")
    @Headers({"Content-Type: application/json"})
    Call<LoanApplicationResponse> loanApplication(@Body LoanApplicationRequest request);

    @POST("api/au_mobile_apis/OnlineLoanApplication")
    @Headers({"Content-Type: application/json"})
    Call<LoanApplicationResponse> onlineLoanApplication(@Body LoanApplicationRequest request);

    @POST("api/au_mobile_apis/FnDashboardStatistics")
    @Headers({"Content-Type: application/json"})
    Call<DashboardResponse> dashboardMinList(@Body DashboardRequest request);

    @POST("api/au_mobile_apis/GetTransactionCharges")
    @Headers({"Content-Type: application/json"})
    Call<TransactionCostResponse> transactionCost(@Body TransactionCostRequest request);

    @POST("api/au_mobile_apis/LoanrepaymentfromFOSA")
    @Headers({"Content-Type: application/json"})
    Call<RepayLoanResponse> repayLoan(@Body LoanRepaymentRequest request);

    @GET("api/au_mobile_apis/GetSecurityQuiz")
    @Headers({"Content-Type: application/json"})
    Call<SecurityQuestionResponse> securityQuestion();

    @POST("api/au_mobile_apis/launchSTKPush")
    @Headers({"Content-Type: application/json"})
    Call<StkPushResponse> stkPush(@Body StkPushRequest request);
    @POST("api/au_mobile_apis/PostMPESATransactions")
    @Headers({"Content-Type: application/json"})
    Call<SendToMobileResponse> sendToMobile(@Body SendToMobileRequest request);

    @POST("api/au_mobile_apis/FnGetNextOfKinInfo")
    @Headers({"Content-Type: application/json"})
    Call<NextOfKinResponse> nextOfKin(@Body NextOfKinRequest request);

    @POST("api/au_mobile_apis/GetDetailedStatement")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> getDetailedStatement(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetLoansGuaranteed")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> getLoansGuaranteed(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/memberisloanquaranteed")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> memberIsLoanGuaranteed(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetCounties")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getCounties(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetSubcounty")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getSubCounty(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetWards")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getWards(@Body ReportsRequest request);

    @GET("api/au_mobile_apis/FnGetCoroselImages")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> FnGetCoroselImages();

    @POST("api/au_mobile_apis/GetBranches")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getBranches(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetClusters")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getClusters(@Body ReportsRequest request);

    @POST("api/au_mobile_apis/GetRelationshipTypes")
    @Headers({"Content-Type: application/json"})
    Call<CountiesResponse> getRelationshipTypes(@Body ReportsRequest request);
    @POST("api/au_mobile_apis/GetOnlineLoanGuarantors")
    @Headers({"Content-Type: application/json"})
    Call<GuarantorResponse> getLoansGuarantorRequests(@Body GuarantorRequest request);

    @POST("api/au_mobile_apis/GetOnlineLoans")
    @Headers({"Content-Type: application/json"})
    Call<GuarantorResponse> getOnlineLoans(@Body GuarantorRequest request);
    @POST("api/au_mobile_apis/AcceptGuarantorship")
    @Headers({"Content-Type: application/json"})
    Call<AcceptOrRejectGuarantorResponse> AcceptGuarantorship(@Body AcceptOrRejectGuarantorRequest request);

    @POST("api/au_mobile_apis/RejectGuarantorship")
    @Headers({"Content-Type: application/json"})
    Call<AcceptOrRejectGuarantorResponse> RejectGuarantorship(@Body AcceptOrRejectGuarantorRequest request);


}
