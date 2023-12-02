package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RegisterRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("id_no")
    public String idNo;
    @SerializedName("password")
    public String password;
    @SerializedName("otp_code")
    public String otpCode;
    @SerializedName("security_code")
    public String security_code;
    @SerializedName("security_answer")
    public String security_answer;
    @SerializedName("msisdn")
    public String msisdn;
    @SerializedName("imei")
    public String imei;

}
