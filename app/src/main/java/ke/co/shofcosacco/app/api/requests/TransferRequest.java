package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TransferRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("source_acc")
    public String source_acc;
    @SerializedName("destination_acc")
    public String destination_acc;
    @SerializedName("amount")
    public String amount;
    @SerializedName("transtype")
    public String transType;

    @SerializedName("otp_code")
    public String otp_code;

}
