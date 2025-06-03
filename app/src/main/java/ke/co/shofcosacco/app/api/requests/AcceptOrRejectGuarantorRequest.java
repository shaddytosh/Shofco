package ke.co.shofcosacco.app.api.requests;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AcceptOrRejectGuarantorRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("loan_app_no")
    public String loanNo;
    @SerializedName("otp")
    public String otp;
}
