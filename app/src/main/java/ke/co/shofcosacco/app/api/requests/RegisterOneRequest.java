package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class RegisterOneRequest implements Serializable {
    @SerializedName("names")
    public String names;
    @SerializedName("id_no")
    public String nationalId;
    @SerializedName("mobile_no")
    public String telephone;
    @SerializedName("email_address")
    public String email;
    @SerializedName("date_of_birth")
    public String dob;

}