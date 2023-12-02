package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class DailyReport implements Serializable {

     @SerializedName("doc_no")
     public String docNo;
     @SerializedName("account_name")
     public String accountName;
     @SerializedName("account_no")
     public String accountNo;
     @SerializedName("telephone")
     public String telephone;
     @SerializedName("transaction_date")
     public String transactionDate;
     @SerializedName("amount")
     public String amount;


     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          DailyReport that = (DailyReport) o;
          return Objects.equals(docNo, that.docNo) && Objects.equals(accountName, that.accountName) && Objects.equals(accountNo, that.accountNo) && Objects.equals(telephone, that.telephone) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(amount, that.amount);
     }

     @Override
     public int hashCode() {
          return Objects.hash(docNo, accountName, accountNo, telephone, transactionDate, amount);
     }



     public String getDocNo() {
          return docNo;
     }

     public String getAccountName() {
          return accountName;
     }

     public String getAccountNo() {
          return accountNo;
     }

     public String getTelephone() {
          return telephone;
     }

     public String getTransactionDate() {
          return transactionDate;
     }

     public String getAmount() {
          return amount;
     }


}
