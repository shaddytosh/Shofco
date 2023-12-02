package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Eligibility implements Serializable {

     @SerializedName("status")
     public String status;
     @SerializedName("description")
     public String description;
     @SerializedName("amount")
     public String amount;
     @SerializedName("interest")
     public String interest;

}
