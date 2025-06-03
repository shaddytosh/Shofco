package ke.co.shofcosacco.app.api.requests;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class LoanApplicationRequest implements Serializable {
    @SerializedName("product_code")
    public String productCode;
    @SerializedName("account_no")
    public String accountNo;
    @SerializedName("period")
    public String period;
    @SerializedName("amount")
    public String amount;
    @SerializedName("app_type")
    public String appType;
    @SerializedName("doc_no")
    public String docNo;
    @SerializedName("otp_code")
    public String otp_code;

    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("username")
    public String username;

    @SerializedName("loan_amount")
    public String loanAmount;
    @SerializedName("repayment_period")
    public String repaymentPeriod;

    @SerializedName("guarantor_request")
    public List<Guarantors> guarantorsList;



    public static class Guarantors implements Serializable ,Parcelable{

        @SerializedName("member_no")
        private String memberNo;
        @SerializedName("free_deposits")
        private String freeDeposits;

        public Guarantors(String memberNo,String freeDeposits) {
            this.memberNo = memberNo;
            this.freeDeposits = freeDeposits;


        }


        protected Guarantors(Parcel in) {
            memberNo = in.readString();
            freeDeposits = in.readString();
        }

        public static final Creator<Guarantors> CREATOR = new Creator<Guarantors>() {
            @Override
            public Guarantors createFromParcel(Parcel in) {
                return new Guarantors(in);
            }

            @Override
            public Guarantors[] newArray(int size) {
                return new Guarantors[size];
            }
        };

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getFreeDeposits() {
            return freeDeposits;
        }

        public void setFreeDeposits(String freeDeposits) {
            this.freeDeposits = freeDeposits;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(memberNo);
            parcel.writeString(freeDeposits);
        }
    }


}
