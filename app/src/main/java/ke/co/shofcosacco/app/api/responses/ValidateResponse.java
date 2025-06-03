package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ValidateResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;

    @SerializedName("free_deposits")
    public String freeDeposits;

    @SerializedName("name")
    public String memberName;

    @SerializedName("member_no")
    public String memberNo;
}
