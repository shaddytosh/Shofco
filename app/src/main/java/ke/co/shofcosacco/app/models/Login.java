package ke.co.shofcosacco.app.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Login implements Serializable {

    @SerializedName("token")
    public String token;
}
