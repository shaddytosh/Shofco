package ke.co.shofcosacco.app.api.responses;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.Carousel;


public class CarouselResponse implements Serializable {
    @SerializedName("f39")
    public String statusCode;
    @SerializedName("f100")
    public String statusDesc;
    @SerializedName("f48")
    public List<Carousel> carouselList;
}
