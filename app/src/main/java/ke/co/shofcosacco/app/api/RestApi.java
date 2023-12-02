package ke.co.shofcosacco.app.api;


import ke.co.shofcosacco.app.api.requests.AccountBalanceRequest;
import ke.co.shofcosacco.app.api.requests.AccountSummaryRequest;
import ke.co.shofcosacco.app.api.requests.ChangePasswordRequest;
import ke.co.shofcosacco.app.api.requests.DashboardRequest;
import ke.co.shofcosacco.app.api.requests.EligibilityRequest;
import ke.co.shofcosacco.app.api.requests.ForgotPasswordRequest;
import ke.co.shofcosacco.app.api.requests.HashPinRequest;
import ke.co.shofcosacco.app.api.requests.LoanApplicationRequest;
import ke.co.shofcosacco.app.api.requests.LoanBalanceRequest;
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
import ke.co.shofcosacco.app.api.responses.AccountBalanceBosaResponse;
import ke.co.shofcosacco.app.api.responses.AccountBalanceFosaResponse;
import ke.co.shofcosacco.app.api.responses.CarouselResponse;
import ke.co.shofcosacco.app.api.responses.ChangePinResponse;
import ke.co.shofcosacco.app.api.responses.DashboardResponse;
import ke.co.shofcosacco.app.api.responses.DestinationAccountResponse;
import ke.co.shofcosacco.app.api.responses.EligibilityResponse;
import ke.co.shofcosacco.app.api.responses.ForgotPinResponse;
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

    @POST("api/shofcosacco/FnLoginMember")
    @Headers({"Content-Type: application/json"})
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("api/otpcode/FnSendOTPCode")
    @Headers({"Content-Type: application/json"})
    Call<ForgotPinResponse> sendOtp(@Body ForgotPasswordRequest request);

    @POST("api/shofcosacco/FnChangePassword")
    @Headers({"Content-Type: application/json"})
    Call<ChangePinResponse> changePassword(@Body ChangePasswordRequest request);

    @POST("api/shofcosacco/FnValidateifRegistered")
    @Headers({"Content-Type: application/json"})
    Call<ValidateResponse> validateUser(@Body ValidateRequest request);

    @POST("api/shofcosacco/FnSignUpMember")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("api/shofcosacco/FnSignUpMember")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> registerOne(@Body RegisterOneRequest request);


    @POST("api/shofcosacco/forgotpassword")
    @Headers({"Content-Type: application/json"})
    Call<RegisterResponse> resetPin(@Body ResetPinRequest request);

    @POST(Constants.ENDPOINT)
    @Headers({"Content-Type: application/json"})
    Call<LoyaltyResponse> loyalty(@Body MainRequest request);

    @POST("api/shofcosacco/FnGetMemberInfo")
    @Headers({"Content-Type: application/json"})
    Call<Profile> getCustomerData(@Body ProfileRequest request);

    @POST("api/shofcosacco/FnGetaccountBalancesBOSA")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceBosaResponse> getAccountBalancesBosa(@Body AccountBalanceRequest request);

    @POST("api/shofcosacco/FnGetaccountBalancesFOSA")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceFosaResponse> getAccountBalancesFosa(@Body AccountBalanceRequest request);

    @POST("api/shofcosacco/FnGetaccountBalancesBOSAwithNoCharges")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceBosaResponse> getAccountBalancesBosaFree(@Body AccountBalanceRequest request);

    @POST("api/shofcosacco/FnGetaccountBalancesFOSAwithNoCharges")
    @Headers({"Content-Type: application/json"})
    Call<AccountBalanceFosaResponse> getAccountBalancesFosaFree(@Body AccountBalanceRequest request);

    @POST("api/shofcosacco/FnGetMinistatement")
    @Headers({"Content-Type: application/json"})
    Call<StatementResponse> getAccountStatement(@Body StatementRequest request);

    @POST("api/shofcosacco/FnGetLoansStatement")
    @Headers({"Content-Type: application/json"})
    Call<LoanStatementResponse> getLoanStatement(@Body LoanStatementRequest request);

    @POST("api/shofcosacco/FnGetallLoansBalances")
    @Headers({"Content-Type: application/json"})
    Call<LoanBalanceResponse> getLoanBalance(@Body LoanBalanceRequest request);

    @POST("api/shofcosacco/FnGetSourceAccounts")
    @Headers({"Content-Type: application/json"})
    Call<SourceAccountResponse> getSourceAccount(@Body AccountSummaryRequest request);

    @POST("api/shofcosacco/Fngetdestinationaccounts")
    @Headers({"Content-Type: application/json"})
    Call<DestinationAccountResponse> getDestinationAccount(@Body AccountSummaryRequest request);

    @POST("api/shofcosacco/FnProcessTransfer")
    @Headers({"Content-Type: application/json"})
    Call<TransferResponse> transfer(@Body TransferRequest request);

    @GET("api/shofcosacco/LoanProductDetails")
    @Headers({"Content-Type: application/json"})
    Call<LoanProductsResponse> loanProducts();

    @POST("api/shofcosacco/GetEligibility")
    @Headers({"Content-Type: application/json"})
    Call<Eligibility> loanEligibility(@Body EligibilityRequest request);

    @POST("api/shofcosacco/ProcessLoan")
    @Headers({"Content-Type: application/json"})
    Call<LoanApplicationResponse> loanApplication(@Body LoanApplicationRequest request);

    @POST("api/shofcosacco/FnDashboardStatistics")
    @Headers({"Content-Type: application/json"})
    Call<DashboardResponse> dashboardMinList(@Body DashboardRequest request);

    @POST("api/shofcosacco/GetTransactionCharges")
    @Headers({"Content-Type: application/json"})
    Call<TransactionCostResponse> transactionCost(@Body TransactionCostRequest request);

    @POST("api/shofcosacco/LoanrepaymentfromFOSA")
    @Headers({"Content-Type: application/json"})
    Call<RepayLoanResponse> repayLoan(@Body LoanRepaymentRequest request);

    @GET("api/shofcosacco/GetSecurityQuiz")
    @Headers({"Content-Type: application/json"})
    Call<SecurityQuestionResponse> securityQuestion();

    @POST("api/shofcosacco/launchSTKPush")
    @Headers({"Content-Type: application/json"})
    Call<StkPushResponse> stkPush(@Body StkPushRequest request);
    @POST("api/shofcosacco/PostMPESATransactions")
    @Headers({"Content-Type: application/json"})
    Call<SendToMobileResponse> sendToMobile(@Body SendToMobileRequest request);

    @POST("api/shofcosacco/FnGetNextOfKinInfo")
    @Headers({"Content-Type: application/json"})
    Call<NextOfKinResponse> nextOfKin(@Body NextOfKinRequest request);

    @POST("api/shofcosacco/GetDetailedStatement")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> getDetailedStatement(@Body ReportsRequest request);

    @POST("api/shofcosacco/GetLoansGuaranteed")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> getLoansGuaranteed(@Body ReportsRequest request);

    @POST("api/shofcosacco/memberisloanquaranteed")
    @Headers({"Content-Type: application/json"})
    Call<ReportsResponse> memberIsLoanGuaranteed(@Body ReportsRequest request);
}
