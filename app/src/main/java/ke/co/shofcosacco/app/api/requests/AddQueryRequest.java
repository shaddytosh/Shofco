package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AddQueryRequest implements Serializable {
    @SerializedName("username")
    public String username;
    @SerializedName("feedback")
    public String feedback;
    @SerializedName("names")
    public String names;

}
