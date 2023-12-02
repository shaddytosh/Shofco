package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class StkPushRequest implements Serializable {
    @SerializedName("mobile_no")
    public String mobileNo;
    @SerializedName("amount")
    public String amount;
    @SerializedName("account_no")
    public String accountNo;

}
