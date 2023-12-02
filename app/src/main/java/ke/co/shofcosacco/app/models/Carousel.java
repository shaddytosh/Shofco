package ke.co.shofcosacco.app.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Carousel implements Serializable {

    public String imageUrl;

    public Carousel(String imageUrl, String caption) {
        this.imageUrl = imageUrl;
        this.caption = caption;
    }

    public String caption;

}
