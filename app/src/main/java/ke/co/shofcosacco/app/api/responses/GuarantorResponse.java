package ke.co.shofcosacco.app.api.responses;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;


public class GuarantorResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;
    @SerializedName("mobile_no")
    public String mobile_no;
    @SerializedName("member_no")
    public String member_no;

    @SerializedName("guarantors")
    public List<GuarantorRequest> guarantors;
    @SerializedName("loan_list")
    public List<Loans> loanlist;


    public  class Loans  implements Serializable{
        @SerializedName("application_no")
        public String applicationNo;
        @SerializedName("loan_type")
        public String loanType;
        @SerializedName("loan_purpose")
        public String loanPurpose;
        @SerializedName("requested_Amount")
        public String requestedAmount;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Loans loans = (Loans) o;
            return Objects.equals(applicationNo, loans.applicationNo) && Objects.equals(loanType, loans.loanType) && Objects.equals(loanPurpose, loans.loanPurpose) && Objects.equals(requestedAmount, loans.requestedAmount) && Objects.equals(installement, loans.installement) && Objects.equals(guarantors, loans.guarantors);
        }

        @Override
        public int hashCode() {
            return Objects.hash(applicationNo, loanType, loanPurpose, requestedAmount, installement, guarantors);
        }

        @SerializedName("installement")
        public String installement;

        @SerializedName("guarantors")
        public List<GuarantorRequest> guarantors;
    }


    public  class GuarantorRequest  implements Serializable, Parcelable {

        protected GuarantorRequest(Parcel in) {
            memberNo = in.readString();
            entryNo = in.readString();
            approvalStatus = in.readString();
            loanType = in.readString();
            loanPurpose = in.readString();
            name = in.readString();
            requestedAmount = in.readString();
            installement = in.readString();
            idNo = in.readString();
            applicationNo = in.readString();
        }

        public  final Creator<GuarantorRequest> CREATOR = new Creator<GuarantorRequest>() {
            @Override
            public GuarantorRequest createFromParcel(Parcel in) {
                return new GuarantorRequest(in);
            }

            @Override
            public GuarantorRequest[] newArray(int size) {
                return new GuarantorRequest[size];
            }
        };

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GuarantorRequest that = (GuarantorRequest) o;
            return Objects.equals(memberNo, that.memberNo) && Objects.equals(entryNo, that.entryNo) && Objects.equals(approvalStatus, that.approvalStatus) && Objects.equals(loanType, that.loanType) && Objects.equals(name, that.name) && Objects.equals(requestedAmount, that.requestedAmount) && Objects.equals(idNo, that.idNo) && Objects.equals(applicationNo, that.applicationNo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memberNo, entryNo, approvalStatus, loanType, name, requestedAmount, idNo, applicationNo);
        }

        @SerializedName("member_no")
        private String memberNo;

        @SerializedName("entry_no")
        private String entryNo;

        @SerializedName("approval_status")
        private String approvalStatus;

        @SerializedName("loan_type")
        private String loanType;
        @SerializedName("loan_purpose")
        private String loanPurpose;

        @SerializedName("name")
        private String name;

        @SerializedName("requested_amount")
        private String requestedAmount;

        @SerializedName("installement")
        private String installement;

        @SerializedName("id_no")
        private String idNo;
        @SerializedName("application_no")
        private String applicationNo;

        // Getters and Setters
        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getEntryNo() {
            return entryNo;
        }

        public void setEntryNo(String entryNo) {
            this.entryNo = entryNo;
        }

        public String getApprovalStatus() {
            return approvalStatus;
        }

        public void setApprovalStatus(String approvalStatus) {
            this.approvalStatus = approvalStatus;
        }

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRequestedAmount() {
            return requestedAmount;
        }

        public void setRequestedAmount(String requestedAmount) {
            this.requestedAmount = requestedAmount;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getApplicationNo() {
            return applicationNo;
        }

        public void setApplicationNo(String applicationNo) {
            this.applicationNo = applicationNo;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeString(memberNo);
            parcel.writeString(entryNo);
            parcel.writeString(approvalStatus);
            parcel.writeString(loanType);
            parcel.writeString(loanPurpose);
            parcel.writeString(name);
            parcel.writeString(requestedAmount);
            parcel.writeString(installement);
            parcel.writeString(idNo);
            parcel.writeString(applicationNo);
        }
    }


}
