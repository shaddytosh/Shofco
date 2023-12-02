package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Clients implements Serializable {


     @SerializedName("name")
     public String name;
     @SerializedName("mobile_no")
     public String mobileNo;
     @SerializedName("member_no")
     public String memberNo;
     @SerializedName("email")
     public String email;
     @SerializedName("status")
     public String status;
     @SerializedName("id_no")
     public String idNo;
     @SerializedName("total_loan_bal")
     public String totalLoanBal;


     @Override
     public boolean equals(Object o) {

          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Clients clients = (Clients) o;
          return Objects.equals(name, clients.name) && Objects.equals(mobileNo, clients.mobileNo) && Objects.equals(memberNo, clients.memberNo) && Objects.equals(email, clients.email) && Objects.equals(status, clients.status) && Objects.equals(idNo, clients.idNo) && Objects.equals(totalLoanBal, clients.totalLoanBal);
     }

     @Override
     public int hashCode() {
          return Objects.hash(name, mobileNo, memberNo, email, status, idNo, totalLoanBal);
     }

}
