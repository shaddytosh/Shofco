package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class MiniStatement implements Serializable {

     @SerializedName("description")
     public String description;
     @SerializedName("transaction_date")
     public String transactionDate;
     @SerializedName("document_no")
     public String documentNo;
     @SerializedName("amount")
     public String amount;
     @SerializedName("trans_type")
     public String transType;


     public String getDescription() {
          return description;
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
          MiniStatement that = (MiniStatement) o;
          return Objects.equals(description, that.description) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(documentNo, that.documentNo) && Objects.equals(amount, that.amount) && Objects.equals(transType, that.transType);
     }

     @Override
     public int hashCode() {
          return Objects.hash(description, transactionDate, documentNo, amount, transType);
     }



}
