package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/**
 * Sample POST Request Body
 * {"f60":"KES","f13":"1005","f12":"100528","mti":"0100","f11":"0528","f68":"Login Request",
 "56":"b8052157a996a9bed25628a34e960d8960791ccf8496b9a1635e13068159b663","f2":"254798939583",
 "f37":"AAA0AA1MKU","f123":"APP","f3":"201010","f7":"20230809100528","f90":"7"
 }
 **/

public class MainRequest implements Serializable {
    @SerializedName("mti")
    public String messageType;
    @SerializedName("f2")
    public String phoneNumber;
    @SerializedName("f3")
    public String transactionCode;
    @SerializedName("f4")
    public String amount;
    @SerializedName("f5")
    public String transactionType; // ACCOUNTBALANCE FUNDSTRANFER LOANBALANCE LOANREPAYMENT MINISTATEMENT MPESA
    @SerializedName("f7")
    public String transactionDate; //date(YmdHis)
    @SerializedName("f11")
    public String minuteSecond; //date(is)
    @SerializedName("f12")
    public String hourMinuteSecond;//date(His)
    @SerializedName("f13")
    public String hourMinute;
    @SerializedName("f37")
    public String referenceNumber;
    @SerializedName("f56")
    public String password;
    @SerializedName("f60")
    public String currency;
    @SerializedName("f64")
    public String oldPin;
    @SerializedName("f65")
    public String loanReason;
    @SerializedName("f68")
    public String narration;
    @SerializedName("f90")
    public String userId;
    @SerializedName("f91")
    public String type;
    @SerializedName("f92")
    public String memberNo;
    @SerializedName("f93")
    public String balCode; // or Account Number
    @SerializedName("f94")
    public String period;
    @SerializedName("f100")
    public String transType;
    @SerializedName("f101")
    public String accountNumber;
    @SerializedName("102")
    public String sourceAccount;
    @SerializedName("f103")
    public String destinationAccount; // or Repaying Phone number
    @SerializedName("f123")
    public String source;

}
