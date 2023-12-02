package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class HashPinRequest implements Serializable {
    @SerializedName("phone")
    public String phone;
    @SerializedName("pin")
    public String pin;
    @SerializedName("new_pin")
    public String newPin;
}
