package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class NextOfKinRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;

}
