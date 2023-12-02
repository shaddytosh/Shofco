package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LoanRepaymentRequest implements Serializable {
    @SerializedName("source_acc")
    public String source_acc;
    @SerializedName("loan_no")
    public String loan_no;
    @SerializedName("amount")
    public String amount;
    @SerializedName("doc_no")
    public String doc_no;
    @SerializedName("app_type")
    public String app_type;
    @SerializedName("otp_code")
    public String otp_code;

}
