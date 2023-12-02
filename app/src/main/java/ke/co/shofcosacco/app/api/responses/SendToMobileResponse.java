package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ke.co.shofcosacco.app.models.Eligibility;


public class SendToMobileResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("f48")
    public Eligibility sendToMobile;

}
