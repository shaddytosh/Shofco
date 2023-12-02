package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class  DestinationAccount implements Serializable {

     @SerializedName("bosa_accounts")
     public List<DestinationBosaAccount> destinationBosaAccounts;
     @SerializedName("fosa_accounts")
     public List<DestinationFosaAccount> destinationFosaAccounts;

}

