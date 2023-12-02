package ke.co.shofcosacco.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionCost implements Serializable {

     @SerializedName("status")
     public String status;
     @SerializedName("description")
     public String description;
     @SerializedName("charges")
     public String charges;

}
