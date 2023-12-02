package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RepayLoanResponse implements Serializable {
    @SerializedName("status")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;


}
