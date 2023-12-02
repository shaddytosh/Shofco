package ke.co.shofcosacco.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class LoanBalance implements Serializable , Parcelable {

     @SerializedName("loan_no")
     public String loanNo;
     @SerializedName("loan_name")
     public String loanName;
     @SerializedName("loan_balance")
     public String loanBalance;
     @SerializedName("loan_issue_date")
     public String loanIssueDate;
     @SerializedName("loan_application_date")
     public String loanApplicationDate;
     @SerializedName("loan_installment")
     public String loanInstallment;

     protected LoanBalance(Parcel in) {
          loanNo = in.readString();
          loanName = in.readString();
          loanBalance = in.readString();
          loanIssueDate = in.readString();
          loanApplicationDate = in.readString();
          loanInstallment = in.readString();
     }

     public static final Creator<LoanBalance> CREATOR = new Creator<LoanBalance>() {
          @Override
          public LoanBalance createFromParcel(Parcel in) {
               return new LoanBalance(in);
          }

          @Override
          public LoanBalance[] newArray(int size) {
               return new LoanBalance[size];
          }
     };

     public String getLoanNo() {
          return loanNo;
     }

     public String getLoanName() {
          return loanName;
     }

     public String getLoanBalance() {
          return loanBalance;
     }

     public String getLoanIssueDate() {
          return loanIssueDate;
     }

     public String getLoanApplicationDate() {
          return loanApplicationDate;
     }

     public String getLoanInstallment() {
          return loanInstallment;
     }


     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          LoanBalance that = (LoanBalance) o;
          return Objects.equals(loanNo, that.loanNo) && Objects.equals(loanName, that.loanName) && Objects.equals(loanBalance, that.loanBalance) && Objects.equals(loanIssueDate, that.loanIssueDate) && Objects.equals(loanApplicationDate, that.loanApplicationDate) && Objects.equals(loanInstallment, that.loanInstallment);
     }

     @Override
     public int hashCode() {
          return Objects.hash(loanNo, loanName, loanBalance, loanIssueDate, loanApplicationDate, loanInstallment);
     }


     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(@NonNull Parcel parcel, int i) {
          parcel.writeString(loanNo);
          parcel.writeString(loanName);
          parcel.writeString(loanBalance);
          parcel.writeString(loanIssueDate);
          parcel.writeString(loanApplicationDate);
          parcel.writeString(loanInstallment);
     }
}
