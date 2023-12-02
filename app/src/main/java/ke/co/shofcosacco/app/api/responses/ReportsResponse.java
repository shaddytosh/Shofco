package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ReportsResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("base_report")
    public String baseReport;

}
