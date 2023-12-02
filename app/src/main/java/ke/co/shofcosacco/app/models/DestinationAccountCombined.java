package ke.co.shofcosacco.app.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

import ke.co.shofcosacco.app.utils.Constants;

public class DestinationAccountCombined implements Serializable {

     public String accountNo;
     public String accountName;
     public String balance;
     public String balCode;
     public String type;

     public DestinationAccountCombined(String accountNo, String accountName, String balance, String balCode, String type) {
          this.accountNo = accountNo;
          this.accountName = accountName;
          this.balance = balance;
          this.balCode = balCode;
          this.type = type;
     }

     @NonNull
     @Override
     public String toString() {
          if (type.equals("FOSA")) {
               return balCode + " (" + Constants.CURRENCY + " " + balance + ")";
          } else {
               return accountName + " (" + Constants.CURRENCY + " " + balance + ")";
          }
     }
}

