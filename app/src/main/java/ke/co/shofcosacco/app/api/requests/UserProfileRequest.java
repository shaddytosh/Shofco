package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserProfileRequest implements Serializable {
    @SerializedName("staff_no")
    public String staffNo;
}
