package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RegisterOneRequest implements Serializable {
    @SerializedName("names")
    public String names;
    @SerializedName("nationalId")
    public String nationalId;
    @SerializedName("telephone")
    public String telephone;
    @SerializedName("email")
    public String email;
    @SerializedName("dob")
    public String dob;

}
