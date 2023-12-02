package ke.co.shofcosacco.app.models;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;


public class NextOfKin implements Serializable {

    @SerializedName("name")
    public String name;
    @SerializedName("mobile_no")
    public String mobile_no;
    @SerializedName("id_no")
    public String id_no;
    @SerializedName("email")
    public String email;

    @SerializedName("allocation")
    public String allocation;
    @SerializedName("beneficiary")
    public String beneficiary;
    @SerializedName("type")
    public String type;
    @SerializedName("relationship")
    public String relationship;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NextOfKin nextOfKin = (NextOfKin) o;
        return Objects.equals(name, nextOfKin.name) && Objects.equals(mobile_no, nextOfKin.mobile_no) && Objects.equals(id_no, nextOfKin.id_no) && Objects.equals(email, nextOfKin.email) && Objects.equals(allocation, nextOfKin.allocation) && Objects.equals(beneficiary, nextOfKin.beneficiary) && Objects.equals(type, nextOfKin.type) && Objects.equals(relationship, nextOfKin.relationship);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, mobile_no, id_no, email, allocation, beneficiary, type, relationship);
    }



}
