package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ForgotPasswordRequest implements Serializable {
    @SerializedName("username")
    public String username;
    @SerializedName("description")
    public String description;
}
