package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {

     @SerializedName("success")
     public String success;
     @SerializedName("description")
     public String description;
     @SerializedName("name")
     public String name;
     @SerializedName("mobile_no")
     public String mobile_no;
     @SerializedName("member_no")
     public String member_no;
     @SerializedName("id_no")
     public String id_no;
     @SerializedName("email")
     public String email;
     @SerializedName("status")
     public String status;



}
