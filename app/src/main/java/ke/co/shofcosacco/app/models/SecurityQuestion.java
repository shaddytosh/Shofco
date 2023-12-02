package ke.co.shofcosacco.app.models;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class SecurityQuestion implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("code")
    public String code;
    @SerializedName("description")
    public String description;

    @NonNull
    @Override
    public String toString() {
        return description+"";
    }
}
