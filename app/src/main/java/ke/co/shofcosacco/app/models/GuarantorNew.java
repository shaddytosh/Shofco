package ke.co.shofcosacco.app.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class GuarantorNew implements Parcelable {

    // Fields
    private String memberNo;

    private String memberName;
    private String freeDeposits;

    public GuarantorNew(String memberName, String memberNo, String freeDeposits) {
        this.memberNo = memberNo;
        this.memberName = memberName;
        this.freeDeposits = freeDeposits;


    }

    protected GuarantorNew(Parcel in) {
        memberNo = in.readString();
        memberName = in.readString();
        freeDeposits = in.readString();
    }

    public static final Creator<GuarantorNew> CREATOR = new Creator<GuarantorNew>() {
        @Override
        public GuarantorNew createFromParcel(Parcel in) {
            return new GuarantorNew(in);
        }

        @Override
        public GuarantorNew[] newArray(int size) {
            return new GuarantorNew[size];
        }
    };

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }


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


    // Display information
    @Override
    public String toString() {
        return "Guarantor: " + getMemberName() + "\n" +
                "Free Deposits: KES" + getFreeDeposits() + "\n"  ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(memberNo);
        parcel.writeString(memberName);
        parcel.writeString(freeDeposits);
    }
}
