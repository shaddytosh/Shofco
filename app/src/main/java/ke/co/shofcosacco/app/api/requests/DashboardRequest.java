package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DashboardRequest implements Serializable {
    @SerializedName("member_no")
    public String member_no;
    @SerializedName("mobile_no")
    public String mobile_no;
    @SerializedName("account_no")
    public String account_no;


}
