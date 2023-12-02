package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Loyalty implements Serializable {

     @SerializedName("loyalty_account")
     public String loyaltyAccount;
     @SerializedName("account_type")
     public String accountType;
     @SerializedName("actual_balance")
     public String actualBalance;
     @SerializedName("available_balance")
     public String availableBalance;
}
