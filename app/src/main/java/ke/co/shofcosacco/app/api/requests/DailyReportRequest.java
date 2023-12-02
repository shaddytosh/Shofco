package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class DailyReportRequest implements Serializable {
    @SerializedName("staff_no")
    public String staffNo;
    @SerializedName("date_from")
    public String dateFrom;
    @SerializedName("date_to")
    public String dateTo;

}
