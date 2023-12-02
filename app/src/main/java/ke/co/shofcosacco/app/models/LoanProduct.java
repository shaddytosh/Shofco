package ke.co.shofcosacco.app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class LoanProduct implements Serializable, Parcelable {

     @SerializedName("product_code")
     public String productCode;
     @SerializedName("product_description")
     public String productDescription;
     @SerializedName("installment_period")
     public String installmentPeriod;
     @SerializedName("interest")
     public String interest;
     @SerializedName("maximum_loan_amt")
     public String maximumLoanAmt;
     @SerializedName("min_loan_amt")
     public String minLoanAmt;
     @SerializedName("repayment_method")
     public String repaymentMethod;


     protected LoanProduct(Parcel in) {
          productCode = in.readString();
          productDescription = in.readString();
          installmentPeriod = in.readString();
          interest = in.readString();
          maximumLoanAmt = in.readString();
          minLoanAmt = in.readString();
          repaymentMethod = in.readString();
     }

     public static final Creator<LoanProduct> CREATOR = new Creator<LoanProduct>() {
          @Override
          public LoanProduct createFromParcel(Parcel in) {
               return new LoanProduct(in);
          }

          @Override
          public LoanProduct[] newArray(int size) {
               return new LoanProduct[size];
          }
     };

     public String getProductCode() {
          return productCode;
     }

     public String getProductDescription() {
          return productDescription;
     }

     public String getInstallmentPeriod() {
          return installmentPeriod;
     }

     public String getInterest() {
          return interest;
     }

     public String getMaximumLoanAmt() {
          return maximumLoanAmt;
     }

     public String getMinLoanAmt() {
          return minLoanAmt;
     }

     public String getRepaymentMethod() {
          return repaymentMethod;
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          LoanProduct that = (LoanProduct) o;
          return Objects.equals(productCode, that.productCode) && Objects.equals(productDescription, that.productDescription) && Objects.equals(installmentPeriod, that.installmentPeriod) && Objects.equals(interest, that.interest) && Objects.equals(maximumLoanAmt, that.maximumLoanAmt) && Objects.equals(minLoanAmt, that.minLoanAmt) && Objects.equals(repaymentMethod, that.repaymentMethod);
     }

     @Override
     public int hashCode() {
          return Objects.hash(productCode, productDescription, installmentPeriod, interest, maximumLoanAmt, minLoanAmt, repaymentMethod);
     }

     @Override
     public int describeContents() {
          return 0;
     }

     @Override
     public void writeToParcel(@NonNull Parcel parcel, int i) {
          parcel.writeString(productCode);
          parcel.writeString(productDescription);
          parcel.writeString(installmentPeriod);
          parcel.writeString(interest);
          parcel.writeString(maximumLoanAmt);
          parcel.writeString(minLoanAmt);
          parcel.writeString(repaymentMethod);
     }
}
