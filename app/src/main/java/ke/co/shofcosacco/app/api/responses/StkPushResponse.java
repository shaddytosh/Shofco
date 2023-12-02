package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class StkPushResponse implements Serializable {
    @SerializedName("Success")
    public String success;
    @SerializedName("description")
    public String description;


}
