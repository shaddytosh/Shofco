package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TransferResponse implements Serializable {
    @SerializedName("status")
    public String success;
    @SerializedName("description")
    public String description;
}
