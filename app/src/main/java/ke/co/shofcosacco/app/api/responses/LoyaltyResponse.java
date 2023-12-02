package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import ke.co.shofcosacco.app.models.Loyalty;


public class LoyaltyResponse implements Serializable {
    @SerializedName("f39")
    public String statusCode;
    @SerializedName("f100")
    public String statusDesc;
    @SerializedName("f48")
    public Loyalty loyalty;
}
