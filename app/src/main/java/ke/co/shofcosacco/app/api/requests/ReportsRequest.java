package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReportsRequest implements Serializable {
    @SerializedName("member_no")
    public String memberNo;
    @SerializedName("date_from")
    public String dateFrom;
    @SerializedName("date_to")
    public String dateTo;


    @SerializedName("county")
    public String county;

    @SerializedName("sub_county_code")
    public String subCountyCode;

}
