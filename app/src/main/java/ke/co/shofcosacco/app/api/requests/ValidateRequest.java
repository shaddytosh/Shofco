package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ValidateRequest implements Serializable {
    @SerializedName("username")
    public String username;
}
