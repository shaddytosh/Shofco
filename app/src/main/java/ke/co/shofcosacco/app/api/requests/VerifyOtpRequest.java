package ke.co.shofcosacco.app.api.requests;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class VerifyOtpRequest implements Serializable {
    @SerializedName("id_number")
    public String idNumber;
    @SerializedName("otp")
    public String otp;
    @SerializedName("source")
    public String source;


}
