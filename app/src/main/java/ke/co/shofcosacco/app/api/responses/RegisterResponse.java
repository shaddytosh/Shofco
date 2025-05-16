package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RegisterResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;
    @SerializedName("amount")
    public String amount;
    @SerializedName("id_no")
    public String idNo;
    @SerializedName("mobile_no")
    public String mobileNo;
}
