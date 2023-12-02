package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LoanApplicationRequest implements Serializable {
    @SerializedName("product_code")
    public String productCode;
    @SerializedName("account_no")
    public String accountNo;
    @SerializedName("period")
    public String period;
    @SerializedName("amount")
    public String amount;
    @SerializedName("app_type")
    public String appType;
    @SerializedName("doc_no")
    public String docNo;
    @SerializedName("otp_code")
    public String otp_code;

}
