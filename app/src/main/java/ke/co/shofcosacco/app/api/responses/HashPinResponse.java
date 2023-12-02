package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class HashPinResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("pin")
    public String pin;
    @SerializedName("new_pin")
    public String newPin;
}
