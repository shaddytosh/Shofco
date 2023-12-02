package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ValidateResponse implements Serializable {
    @SerializedName("success")
    public String success;
    @SerializedName("description")
    public String description;
}
