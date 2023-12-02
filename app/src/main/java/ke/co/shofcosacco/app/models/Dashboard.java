package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Dashboard implements Serializable {

     @SerializedName("description")
     public String description;
     @SerializedName("transaction_date")
     public String transaction_date;
     @SerializedName("document_no")
     public String document_no;
     @SerializedName("amount")
     public String amount;
     @SerializedName("trans_type")
     public String trans_type;

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Dashboard dashboard = (Dashboard) o;
          return Objects.equals(description, dashboard.description) && Objects.equals(transaction_date, dashboard.transaction_date) && Objects.equals(document_no, dashboard.document_no) && Objects.equals(amount, dashboard.amount) && Objects.equals(trans_type, dashboard.trans_type);
     }

     @Override
     public int hashCode() {
          return Objects.hash(description, transaction_date, document_no, amount, trans_type);
     }


}
