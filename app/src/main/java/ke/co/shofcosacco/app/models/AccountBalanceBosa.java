package ke.co.shofcosacco.app.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import ke.co.shofcosacco.app.utils.Constants;

public class AccountBalanceBosa implements Serializable {

     @SerializedName("account_name")
     public String accountName;
     @SerializedName("balances")
     public String balances;
     @SerializedName("bal_code")
     public String balCode;

     public String getAccountName() {
          return accountName;
     }

     public String getBalances() {
          return balances;
     }

     public String getBalCode() {
          return balCode;
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          AccountBalanceBosa that = (AccountBalanceBosa) o;
          return Objects.equals(accountName, that.accountName) && Objects.equals(balances, that.balances) && Objects.equals(balCode, that.balCode);
     }

     @Override
     public int hashCode() {
          return Objects.hash(accountName, balances, balCode);
     }

     @NonNull
     @Override
     public String toString() {
          return getAccountName()+" ("+ Constants.CURRENCY+" " +balances+")";
     }

}
