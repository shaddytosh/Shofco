package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.SourceAccount;


public class SourceAccountResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("source_account_fosa")
    public List<SourceAccount> sourceAccounts;


}
