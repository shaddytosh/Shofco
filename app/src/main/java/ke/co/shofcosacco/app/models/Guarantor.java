package ke.co.shofcosacco.app.models;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;
public class Guarantor implements Serializable {
    @SerializedName("loan_no")
    public String loanNo;
    @SerializedName("client_name")
    public String clientName;
    @SerializedName("loan_name")
    public String loanName;
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("amount")
    public String amount;
    @SerializedName("period")
    public String period;




    public String getLoanNo() {
        return loanNo;
    }
    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getLoanName() {
        return loanName;
    }
    public void setLoanName(String loanName) {
        this.loanName = loanName;
    }
    public String getMemberNo() {
        return memberNo;
    }
    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }
    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getPeriod() {
        return period;
    }
    public void setPeriod(String period) {
        this.period = period;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guarantor guarantor = (Guarantor) o;
        return Objects.equals(loanNo, guarantor.loanNo) && Objects.equals(clientName, guarantor.clientName) && Objects.equals(loanName, guarantor.loanName) && Objects.equals(memberNo, guarantor.memberNo) && Objects.equals(amount, guarantor.amount) && Objects.equals(period, guarantor.period);
    }
    @Override
    public int hashCode() {
        return Objects.hash(loanNo, clientName, loanName, memberNo, amount, period);
    }
}
