package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class LoanStatement implements Serializable {

     @SerializedName("loan_no")
     public String loanNo;
     @SerializedName("loan_desc")
     public String loanDesc;
     @SerializedName("transaction_date")
     public String transactionDate;
     @SerializedName("document_no")
     public String documentNo;
     @SerializedName("amount")
     public String amount;
     @SerializedName("trans_type")
     public String transType;


     public String getLoanNo() {
          return loanNo;
     }

     public String getLoanDesc() {
          return loanDesc;
     }

     public String getTransactionDate() {
          return transactionDate;
     }

     public String getDocumentNo() {
          return documentNo;
     }

     public String getAmount() {
          return amount;
     }

     public String getTransType() {
          return transType;
     }


     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          LoanStatement that = (LoanStatement) o;
          return Objects.equals(loanNo, that.loanNo) && Objects.equals(loanDesc, that.loanDesc) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(documentNo, that.documentNo) && Objects.equals(amount, that.amount) && Objects.equals(transType, that.transType);
     }

     @Override
     public int hashCode() {
          return Objects.hash(loanNo, loanDesc, transactionDate, documentNo, amount, transType);
     }



}
