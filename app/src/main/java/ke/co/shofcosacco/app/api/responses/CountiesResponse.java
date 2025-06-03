package ke.co.shofcosacco.app.api.responses;


import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import ke.co.shofcosacco.app.models.AccountBalanceBosa;


public class CountiesResponse implements Serializable {
    @SerializedName("success")
    public String statusCode;
    @SerializedName("description")
    public String statusDesc;
    @SerializedName("counties")
    public List<Counties> counties;
    @SerializedName("sub_counties")
    public List<SubCounties> subCounties;
    @SerializedName("wards")
    public List<Wards> wards;

    @SerializedName("branches")
    public List<Branch> branches;

    @SerializedName("relation_types")
    public List<RelationshipType> relationTypes;

    @SerializedName("clusters")
    public List<Cluster> clusters;

    public static class Counties implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("county_name")
        public String countyName;

        @NonNull
        @Override
        public String toString() {
            return countyName;
        }
    }

    public static class RelationshipType implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("type_name")
        public String typeName;

        @NonNull
        @Override
        public String toString() {
            return typeName;
        }
    }

    public static class SubCounties implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("county_name")
        public String countyName;

        @NonNull
        @Override
        public String toString() {
            return countyName;
        }
    }

    public static class Wards implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("county_name")
        public String countyName;

        @NonNull
        @Override
        public String toString() {
            return countyName;
        }
    }

    public static class Branch implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("branch_name")
        public String branchName;

        @NonNull
        @Override
        public String toString() {
            return branchName;
        }
    }

    public static class Cluster implements Serializable {
        @SerializedName("code")
        public String code;
        @SerializedName("cluster_name")
        public String clusterName;

        @NonNull
        @Override
        public String toString() {
            return clusterName;
        }
    }

}
