package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class LoginRequest implements Serializable {
    @SerializedName("username")
    public String memberNo;
    @SerializedName("password")
    public String password;

}
