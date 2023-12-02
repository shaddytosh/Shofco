package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ChangePasswordRequest implements Serializable {
    @SerializedName("username")
    public String username;
    @SerializedName("old_password")
    public String oldPassword;
    @SerializedName("new_password")
    public String newPassword;
    @SerializedName("otp_code")
    public String otpCode;

}
