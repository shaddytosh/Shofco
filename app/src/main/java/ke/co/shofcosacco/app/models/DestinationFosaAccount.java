package ke.co.shofcosacco.app.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ke.co.shofcosacco.app.utils.Constants;

public class DestinationFosaAccount implements Serializable {

     @SerializedName("account_no")
     public String accountNo;
     @SerializedName("account_name")
     public String accountName;
     @SerializedName("balance")
     public String balance;
     @SerializedName("bal_code")
     public String balCode;

     public String getAccountName() {
          return accountName;
     }

     public String getBalance() {
          return balance;
     }

     public String getBalCode() {
          return balCode;
     }


     @NonNull
     @Override
     public String toString() {
          return getBalCode()+" ("+ Constants.CURRENCY+" " +getBalance()+")";
     }
}

