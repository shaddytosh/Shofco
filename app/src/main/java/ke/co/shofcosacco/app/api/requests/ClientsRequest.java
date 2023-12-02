package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ClientsRequest implements Serializable {
    @SerializedName("staff_no")
    public String staffNo;
}
