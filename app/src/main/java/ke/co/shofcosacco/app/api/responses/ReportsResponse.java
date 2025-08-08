package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class ReportsResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("base_report")
    public String baseReport;

    @SerializedName("description")
    public String description;
    @SerializedName("current_version")
    public String currentVersion;

    @SerializedName("force_update")
    public boolean forceUpdate = false;

    @SerializedName("attached_files")
    public List<Carousel> carouselList;


    public static class Carousel implements Serializable {
        @SerializedName("url")
        public String url;
    }
}
