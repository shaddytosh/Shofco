package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ke.co.shofcosacco.app.models.LoanApplication;


public class LoanApplicationResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("status")
    public String status;
    @SerializedName("f48")
    public LoanApplication loanApplication;

}
