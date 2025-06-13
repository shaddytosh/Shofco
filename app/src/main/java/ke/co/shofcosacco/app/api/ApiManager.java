package ke.co.shofcosacco.app.api;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import ke.co.shofcosacco.app.api.deserializer.ResponseDeserializer;
import ke.co.shofcosacco.app.api.requests.AcceptOrRejectGuarantorRequest;
import ke.co.shofcosacco.app.api.requests.AccountBalanceRequest;
import ke.co.shofcosacco.app.api.requests.AccountSummaryRequest;
import ke.co.shofcosacco.app.api.requests.AddNextOfKinRequest;
import ke.co.shofcosacco.app.api.requests.ChangePasswordRequest;
import ke.co.shofcosacco.app.api.requests.DashboardRequest;
import ke.co.shofcosacco.app.api.requests.EligibilityRequest;
import ke.co.shofcosacco.app.api.requests.ForgotPasswordRequest;
import ke.co.shofcosacco.app.api.requests.GuarantorRequest;
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
import ke.co.shofcosacco.app.api.responses.ChangePinResponse;
import ke.co.shofcosacco.app.api.responses.CountiesResponse;
import ke.co.shofcosacco.app.api.responses.DashboardResponse;
import ke.co.shofcosacco.app.api.responses.DestinationAccountResponse;
import ke.co.shofcosacco.app.api.responses.EligibilityResponse;
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
import ke.co.shofcosacco.app.sharedprefs.SecurePrefs;
import ke.co.shofcosacco.app.utils.Constants;
import ke.co.shofcosacco.app.utils.FileUtils;
import ke.co.shofcosacco.app.utils.RefnoGenerator;
import ke.co.shofcosacco.app.utils.ViewUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class ApiManager {
    private static final int READ_TIMEOUT = 15;
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 15;
    public static final String SERVER_URL = "https://payments.auinnovation.co.ke:45321/";


    private static final int NUMBER_OF_THREADS = 6;
    private static final ExecutorService apiExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    private final RestApi api;
    private final SecurePrefs securePrefs;
    private final Application application;

    @Inject
    public ApiManager(SecurePrefs securePrefs, Application application) {
        this.securePrefs = securePrefs;
        this.application = application;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(logging);


        builder.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .method(original.method(), original.body());
            // Check if the header value is not null, and add it if it's present
            String headerValue = getToken();
            if (headerValue != null) {
                requestBuilder.addHeader("Token",  headerValue);
            }else {
                requestBuilder.addHeader("Token",  "");
            }

            String username = getAccountNumber();
            if (headerValue != null) {
                requestBuilder.addHeader("username", username);
            }else {
                requestBuilder.addHeader("username", "");

            }

            requestBuilder.addHeader("client_id", "Esrtd874Sx9098R2MAhWQ");
            Request request = requestBuilder.build();

            return chain.proceed(request);
        });


        OkHttpClient client = builder.build();


        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create();


        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(SERVER_URL)
                .client(client)
                .build();

        api = retrofit.create(RestApi.class);
    }

    public static void execute(Runnable runnable) {
        apiExecutor.execute(runnable);
    }

    @Nullable
    private RequestBody getPart(String value) {
        return value == null ? null : RequestBody.create(value, MediaType.parse("text/plain"));
    }

    @Nullable
    private MultipartBody.Part getFilePart(Uri uri, String name) {
        if (uri == null) {
            return null;
        }
        if (name == null) {
            name = "file";
        }
        byte[] fileData = FileUtils.readFile(application, uri);
        if (fileData == null) {
            return null;
        }
        DocumentFile attachment = FileUtils.fromUri(application, uri);
        String mimeType = FileUtils.getType(application, uri);
        RequestBody fileRequestBody = RequestBody.create(fileData, MediaType.parse(mimeType));
        return MultipartBody.Part.createFormData(name, attachment.getName(), fileRequestBody);
    }


    public String getNationalId() {
        try {
            return securePrefs.getNationalId();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMemberNo() {
        try {
            return securePrefs.getMemberNo();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getLastNameMemberNo() {
        try {
            return securePrefs.getLastName();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public String getPhone() {
        try {
            return securePrefs.getPhone();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getFirstName() {
        try {
            return securePrefs.getFirstName();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMiddleName() {
        try {
            return securePrefs.getMiddleName();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getLastName() {
        try {
            return securePrefs.getLastName();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTransactionLimit() {
        try {
            return securePrefs.getTransactionLimit();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEmail() {
        try {
            return securePrefs.getEmail();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUserId() {
        try {
            return securePrefs.getUserId();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getToken() {
        try {
            return securePrefs.getToken();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getAccountNumber() {
        try {
            return securePrefs.getAccountNumber();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMPIN() {
        try {
            return securePrefs.getMPIN();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMID() {
        try {
            return securePrefs.getMID();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBiometric() {
        try {
            return securePrefs.getBiometric();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public APIResponse<LoginResponse> login(String memberNo, String password) throws IOException {
        LoginRequest request = new LoginRequest();
        request.memberNo=memberNo;
        request.password=password;
        return new APIResponse<>(api.login(request).execute());
    }



    public APIResponse<ForgotPinResponse> sendOtp(String memberNo, String description) throws IOException {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        if (memberNo != null){
            request.username=memberNo;
        }else {
            request.username=getMemberNo();
        }
        request.description=description;
        return new APIResponse<>(api.sendOtp(request).execute());
    }



    public APIResponse<ChangePinResponse> changePassword(String oldPassword, String newPassword,String otp) throws IOException {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.username=getMemberNo();
        request.oldPassword = oldPassword;
        request.newPassword = newPassword;
        request.otpCode = otp;

        return new APIResponse<>(api.changePassword(request).execute());
    }

    public APIResponse<ValidateResponse> validateUser(String memberNo,boolean isValidateGuarantor, boolean isRegister) throws IOException {
        ValidateRequest request = new ValidateRequest();

        if (isRegister){
            request.idNumber = memberNo;
            request.memberNo = getLastNameMemberNo();
            return new APIResponse<>(api.validateId(request).execute());

        }
       else  if (isValidateGuarantor) {
            request.idNumber = memberNo;
            request.memberNo = getLastNameMemberNo();
            return new APIResponse<>(api.validateGuarantor(request).execute());

        }else {
            request.username = memberNo;
            return new APIResponse<>(api.validateUser(request).execute());

        }

    }

    public APIResponse<RegisterResponse> register(String memberNo, String password, String nationalId,
                                                  String otp, String questionCode, String answer, String IMEI) throws IOException {
        RegisterRequest request = new RegisterRequest();
        request.memberNo=memberNo;
        request.password = password;
        request.idNo = nationalId;
        request.otpCode = otp;
        request.security_code = questionCode;
        request.security_answer = answer;
        request.imei="123456789066";
        request.msisdn = "254728224921";

        return new APIResponse<>(api.register(request).execute());
    }

    public APIResponse<RegisterResponse> registerOne(AddNextOfKinRequest addNextOfKinRequest) throws IOException {
        return new APIResponse<>(api.registerOne(addNextOfKinRequest).execute());
    }

    public APIResponse<RegisterResponse> resetPin(String memberNo, String password, String nationalId,
                                                  String otp, String questionCode, String answer) throws IOException {
        ResetPinRequest request = new ResetPinRequest();
        request.memberNo=memberNo;
        request.password = password;
        request.idNo = nationalId;
        request.otpCode = otp;
        request.security_code = questionCode;
        request.security_answer = answer;
        request.old_password = "4857";


        return new APIResponse<>(api.resetPin(request).execute());
    }


    public APIResponse<LoyaltyResponse> loyalty() throws IOException {
        MainRequest request = new MainRequest();
        request.messageType = Constants.MTI2;
        request.phoneNumber = getPhone();
        request.transactionCode = Constants.LOYALTY_CODE;
        request.transactionDate = ViewUtils.getTransactionDate();
        request.minuteSecond = ViewUtils.getMinuteSecond();
        request.hourMinuteSecond = ViewUtils.getHourMinuteSecond();
        request.hourMinute = ViewUtils.getHourMinute();
        request.referenceNumber = RefnoGenerator.createRefno();
        request.currency = Constants.CURRENCY;
        request.narration = Constants.LOYALTY_NARRATION;
        request.userId = getUserId();
        request.memberNo = getMemberNo();
        request.source = Constants.SOURCE;


        return new APIResponse<>(api.loyalty(request).execute());
    }

    public APIResponse<Profile> getCustomerData() throws IOException, NoSuchAlgorithmException {

        ProfileRequest request = new ProfileRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getCustomerData(request).execute());

    }


    public APIResponse<AccountBalanceBosaResponse> getAccountBalancesBosa() throws IOException {
        AccountBalanceRequest request = new AccountBalanceRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getAccountBalancesBosa(request).execute());
    }

    public APIResponse<AccountBalanceFosaResponse> getAccountBalancesFosa() throws IOException {
        AccountBalanceRequest request = new AccountBalanceRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getAccountBalancesFosa(request).execute());
    }

    public APIResponse<AccountBalanceBosaResponse> getAccountBalancesBosaFree() throws IOException {
        AccountBalanceRequest request = new AccountBalanceRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getAccountBalancesBosaFree(request).execute());
    }

    public APIResponse<AccountBalanceFosaResponse> getAccountBalancesFosaFree() throws IOException {
        AccountBalanceRequest request = new AccountBalanceRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getAccountBalancesFosaFree(request).execute());
    }



    public APIResponse<StatementResponse> getAccountStatement(String balCode) throws IOException {
        StatementRequest request = new StatementRequest();
        request.memberNo = getMemberNo();
        request.balCode=balCode;
        return new APIResponse<>(api.getAccountStatement(request).execute());
    }

    public APIResponse<LoanStatementResponse> getLoanStatement(String loanNo) throws IOException {
        LoanStatementRequest request = new LoanStatementRequest();
        request.memberNo = getMemberNo();
        request.loanNo=loanNo;
        return new APIResponse<>(api.getLoanStatement(request).execute());
    }

    public APIResponse<LoanBalanceResponse> getLoanBalance() throws IOException {
        LoanBalanceRequest request = new LoanBalanceRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getLoanBalance(request).execute());
    }

    public APIResponse<SourceAccountResponse> getSourceAccount() throws IOException {
        AccountSummaryRequest request = new AccountSummaryRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getSourceAccount(request).execute());
    }

    public APIResponse<DestinationAccountResponse> getDestinationAccount() throws IOException {
        AccountSummaryRequest request = new AccountSummaryRequest();
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.getDestinationAccount(request).execute());
    }

    public APIResponse<TransferResponse> transfer(String sourceAccount, String destinationAccount,
                                                  String amount,String transType, String otp) throws IOException {
        TransferRequest request = new TransferRequest();
        request.amount = amount.replace(".0","").replace(".00","");;
        request.memberNo = getMemberNo();
        request.transType =transType;
        request.source_acc = sourceAccount;
        request.destination_acc = destinationAccount;
        request.otp_code = otp;
        return new APIResponse<>(api.transfer(request).execute());
    }

    public APIResponse<LoanProductsResponse> loanProducts(String type) throws IOException {
        LoanProductRequest request = new LoanProductRequest();
        request.type = type;
        request.memberNo = getMemberNo();
        return new APIResponse<>(api.loanProducts().execute());
    }

    public APIResponse<Eligibility> loanEligibility(String productCode, String period, String amount) throws IOException {
        EligibilityRequest request = new EligibilityRequest();
        request.productCode = productCode;
        request.period = period;
        request.amount = amount.replace(".0","").replace(".00","");
        request.accountNo= getAccountNumber();
        return new APIResponse<>(api.loanEligibility(request).execute());
    }

    public APIResponse<LoanApplicationResponse> loanApplication(String productCode, String period,
                                                                String amount, String otp,
                                                                List<LoanApplicationRequest.Guarantors> guarantorsList, boolean isOnline) throws IOException {
        LoanApplicationRequest request = new LoanApplicationRequest();


        if (isOnline){
            request.memberNo = getLastNameMemberNo();
            request.productCode = productCode;
            request.repaymentPeriod = period;
            request.loanAmount = amount.replace(".0","").replace(".00","");
            request.appType = Constants.SOURCE;
            request.otp_code = otp;
            request.username = getAccountNumber();
            request.guarantorsList = guarantorsList;
            return new APIResponse<>(api.onlineLoanApplication(request).execute());
        }else {
            request.docNo = RefnoGenerator.createRefno();
            request.accountNo = getAccountNumber();
            request.productCode = productCode;
            request.period = period;
            request.amount = amount.replace(".0","").replace(".00","");
            request.appType = Constants.SOURCE;
            request.otp_code = otp;
            return new APIResponse<>(api.loanApplication(request).execute());
        }

    }

    public APIResponse<DashboardResponse> dashboardMinList() throws IOException {
        DashboardRequest request = new DashboardRequest();
        request.member_no =getAccountNumber();
        request.mobile_no = getMemberNo();
        request.account_no = getAccountNumber();

        return new APIResponse<>(api.dashboardMinList(request).execute());
    }

    public APIResponse<TransactionCostResponse> transactionCost(String transactionType, String amount) throws IOException {
        TransactionCostRequest request = new TransactionCostRequest();
        request.amount = amount.replace(".0","").replace(".00","");;
        request.accountNo= getAccountNumber();
        request.transactionCode = Constants.SOURCE;


        return new APIResponse<>(api.transactionCost(request).execute());
    }

    public APIResponse<RepayLoanResponse> repayLoan(String accountNumber, String amount, String loanNo, String otp) throws IOException {
        LoanRepaymentRequest request = new LoanRepaymentRequest();
        request.doc_no = RefnoGenerator.createRefno();
        request.amount = amount;
        request.loan_no = loanNo;
        request.source_acc= accountNumber;
        request.app_type = Constants.SOURCE;
        request.otp_code = otp;

        return new APIResponse<>(api.repayLoan(request).execute());
    }

    public APIResponse<SecurityQuestionResponse> securityQuestion() throws IOException {
        return new APIResponse<>(api.securityQuestion().execute());
    }

    public APIResponse<StkPushResponse> stkPush(String accountNo, String mobile, String amount) throws IOException {
        StkPushRequest request = new StkPushRequest();
        request.accountNo= accountNo;
        request.mobileNo = mobile;
        request.amount = amount.replace(".0","").replace(".00","");;

        return new APIResponse<>(api.stkPush(request).execute());
    }

    public APIResponse<SendToMobileResponse> sendToMobile(String accountNumber, String amount, String phone, String otp) throws IOException {
        SendToMobileRequest request = new SendToMobileRequest();

        request.sourceAcc = accountNumber;
        request.transactionDate = ViewUtils.getTransactionDateyyyyMMdd();
        request.docNo = RefnoGenerator.createRefno();
        request.amount = amount.replace(".0","").replace(".00","");;
        request.appType = Constants.SOURCE;
        request.phone = phone;
        request.otp_code = otp;
        request.member_no = getAccountNumber();

        return new APIResponse<>(api.sendToMobile(request).execute());
    }

    public APIResponse<NextOfKinResponse> nextOfKin() throws IOException {
        NextOfKinRequest request = new NextOfKinRequest();
        request.memberNo = getMemberNo();

        return new APIResponse<>(api.nextOfKin(request).execute());
    }

    public APIResponse<ReportsResponse> getDetailedStatement(String dateFrom, String dateTo) throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();
        request.dateFrom = dateFrom;
        request.dateTo = dateTo;


        return new APIResponse<>(api.getDetailedStatement(request).execute());
    }

    public APIResponse<ReportsResponse> getLoansGuaranteed() throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();

        return new APIResponse<>(api.getLoansGuaranteed(request).execute());
    }

    public APIResponse<ReportsResponse> memberIsLoanGuaranteed() throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();

        return new APIResponse<>(api.memberIsLoanGuaranteed(request).execute());
    }

    public APIResponse<CountiesResponse> getCounties() throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();

        return new APIResponse<>(api.getCounties(request).execute());
    }

    public APIResponse<CountiesResponse> getBranchesOrClusters(boolean isBranch) throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();

        if (isBranch) {
            return new APIResponse<>(api.getBranches(request).execute());
        }else {
            return new APIResponse<>(api.getClusters(request).execute());
        }
    }

    public APIResponse<CountiesResponse> getRelationshipTypes() throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.memberNo = getMemberNo();

        return new APIResponse<>(api.getRelationshipTypes(request).execute());
    }

    public APIResponse<CountiesResponse> getSubCounty(String countyCode) throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.county = countyCode;

        return new APIResponse<>(api.getSubCounty(request).execute());
    }

    public APIResponse<CountiesResponse> getWards(String subCountyCode) throws IOException {
        ReportsRequest request = new ReportsRequest();
        request.subCountyCode = subCountyCode;

        return new APIResponse<>(api.getWards(request).execute());
    }

    public APIResponse<ReportsResponse> FnGetCoroselImages() throws IOException {

        return new APIResponse<>(api.FnGetCoroselImages().execute());
    }

    public APIResponse<GuarantorResponse> getLoansGuarantorRequests() throws IOException {
        GuarantorRequest request = new GuarantorRequest();
        request.memberNo = getLastNameMemberNo();

        return new APIResponse<>(api.getLoansGuarantorRequests(request).execute());
    }

    public APIResponse<GuarantorResponse> getOnlineLoans() throws IOException {
        GuarantorRequest request = new GuarantorRequest();
        request.memberNo = getLastNameMemberNo();

        return new APIResponse<>(api.getOnlineLoans(request).execute());
    }
    public APIResponse<AcceptOrRejectGuarantorResponse> AcceptOrRejectGuarantor(String memberNo, String loanNo, String type, String otp) throws IOException {
        AcceptOrRejectGuarantorRequest request = new AcceptOrRejectGuarantorRequest();
        request.memberNo = getLastNameMemberNo();
        request.loanNo = loanNo;
        request.otp = otp;
        if (type != null ) {
            if (type.equals("accept")){
                return new APIResponse<>(api.AcceptGuarantorship(request).execute());
            }else if (type.equals("reject")){
                return new APIResponse<>(api.RejectGuarantorship(request).execute());
            }
        }else {
            return null;
        }
        return null;
    }



}
