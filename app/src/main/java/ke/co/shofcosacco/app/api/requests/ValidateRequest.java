package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ValidateRequest implements Serializable {
    @SerializedName("username")
    public String username;
    @SerializedName("id_no")
    public String idNumber;
    @SerializedName("member_no")
    public String memberNo;
}
