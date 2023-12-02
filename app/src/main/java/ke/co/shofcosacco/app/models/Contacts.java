package ke.co.shofcosacco.app.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Contacts implements Serializable {

     public String name;

     public Contacts(String name, String phone) {
          this.name = name;
          this.phone = phone;
     }

     public String phone;


     @NonNull
     @Override
     public String toString() {
          return name+"\n"+phone;
     }
}
