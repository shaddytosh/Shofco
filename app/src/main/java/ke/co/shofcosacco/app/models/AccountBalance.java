package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AccountBalance implements Serializable {

     @SerializedName("account_balances_bosa")
     public List<AccountBalanceBosa> accountBalancesBosaBosa;
     @SerializedName("account_balances")
     public List<AccountBalanceFosa> accountBalancesFosaBosa;
}
