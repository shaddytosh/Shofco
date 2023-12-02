package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ResetPinRequest implements Serializable {
    @SerializedName("username")
    public String memberNo;
    @SerializedName("id_no")
    public String idNo;
    @SerializedName("new_password")
    public String password;
    @SerializedName("otp_code")
    public String otpCode;
    @SerializedName("security_code")
    public String security_code;
    @SerializedName("security_answer")
    public String security_answer;
    @SerializedName("old_password")
    public String old_password;
}
