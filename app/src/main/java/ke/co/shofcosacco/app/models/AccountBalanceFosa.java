package ke.co.shofcosacco.app.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

import ke.co.shofcosacco.app.utils.Constants;

public class AccountBalanceFosa implements Serializable {

     @SerializedName("account_no")
     public String accountNo;
     @SerializedName("account_name")
     public String accountName;
     @SerializedName("account_status")
     public String accountStatus;
     @SerializedName("balance")
     public String balance;
     @SerializedName("bal_code")
     public String balCode;

     public String getAccountNo() {
          return accountNo;
     }

     public void setAccountNo(String accountNo) {
          this.accountNo = accountNo;
     }

     public String getAccountName() {
          return accountName;
     }

     public void setAccountName(String accountName) {
          this.accountName = accountName;
     }

     public String getAccountStatus() {
          return accountStatus;
     }

     public void setAccountStatus(String accountStatus) {
          this.accountStatus = accountStatus;
     }

     public String getBalance() {
          return balance;
     }

     public void setBalance(String balance) {
          this.balance = balance;
     }

     public String getBalCode() {
          return balCode;
     }

     public void setBalCode(String balCode) {
          this.balCode = balCode;
     }



     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          AccountBalanceFosa that = (AccountBalanceFosa) o;
          return Objects.equals(accountNo, that.accountNo) && Objects.equals(accountName, that.accountName) && Objects.equals(accountStatus, that.accountStatus) && Objects.equals(balance, that.balance) && Objects.equals(balCode, that.balCode);
     }

     @Override
     public int hashCode() {
          return Objects.hash(accountNo, accountName, accountStatus, balance, balCode);
     }

     @NonNull
     public String toString() {
          if (balCode.equals("ORD")) {
               return "ORDINARY (" + Constants.CURRENCY + " " + balance + ")";
          } else {
               return getBalCode() + " (" + Constants.CURRENCY + " " + balance + ")";
          }
     }

}
