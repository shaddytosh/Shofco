package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ke.co.shofcosacco.app.models.Eligibility;


public class EligibilityResponse implements Serializable {
    @SerializedName("status")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("f48")
    public Eligibility eligibility;

}
