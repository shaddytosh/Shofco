package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.DestinationBosaAccount;
import ke.co.shofcosacco.app.models.DestinationFosaAccount;


public class DestinationAccountResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("bosa_accounts")
    public List<DestinationBosaAccount> destinationBosaAccounts;
    @SerializedName("fosa_accounts")
    public List<DestinationFosaAccount> destinationFosaAccounts;

}
