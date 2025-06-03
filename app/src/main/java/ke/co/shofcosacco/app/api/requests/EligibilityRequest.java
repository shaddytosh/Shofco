package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class EligibilityRequest implements Serializable {
    @SerializedName("product_code")
    public String productCode;
    @SerializedName("account_no")
    public String accountNo;
    @SerializedName("period")
    public String period;
    @SerializedName("amount")
    public String amount;
}
