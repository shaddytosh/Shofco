
package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.AccountBalanceBosa;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;


public class AccountBalanceResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("account_balances_bosa")
    public List<AccountBalanceBosa> accountBalancesBosaBosa;
    @SerializedName("account_balances")
    public List<AccountBalanceFosa> accountBalancesFosaFosa;

}
