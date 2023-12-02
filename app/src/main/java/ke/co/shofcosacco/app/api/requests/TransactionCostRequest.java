package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TransactionCostRequest implements Serializable {
    @SerializedName("account_no")
    public String accountNo;
    @SerializedName("amount")
    public String amount;
    @SerializedName("transaction_code")
    public String transactionCode;

}
