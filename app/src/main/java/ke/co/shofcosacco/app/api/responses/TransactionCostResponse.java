package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TransactionCostResponse implements Serializable {
    @SerializedName("status")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("charges")
    public String charges;

}
