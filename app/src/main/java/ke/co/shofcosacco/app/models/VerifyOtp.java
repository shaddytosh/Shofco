package ke.co.shofcosacco.app.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class VerifyOtp implements Serializable {

    @SerializedName("user_id")
    public String userId;

}
