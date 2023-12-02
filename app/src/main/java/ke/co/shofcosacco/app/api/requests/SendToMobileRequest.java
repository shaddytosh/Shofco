package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SendToMobileRequest implements Serializable {
    @SerializedName("source_acc")
    public String sourceAcc;
    @SerializedName("amount")
    public String amount;
    @SerializedName("transaction_date")
    public String transactionDate;
    @SerializedName("app_type")
    public String appType;
    @SerializedName("doc_no")
    public String docNo;
    @SerializedName("phone")
    public String phone;
    @SerializedName("otp_code")
    public String otp_code;



}
