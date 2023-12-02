package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class StatementRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("bal_code")
    public String balCode;

}
