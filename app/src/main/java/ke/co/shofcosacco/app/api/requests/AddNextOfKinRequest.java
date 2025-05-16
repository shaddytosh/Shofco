package ke.co.shofcosacco.app.api.requests;


import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class AddNextOfKinRequest implements Serializable {

    @SerializedName("names")
    private String names;

    @SerializedName("date_of_birth")
    private String dateOfBirth;

    @SerializedName("id_no")
    private String idNo;

    @SerializedName("mobile_no")
    private String mobileNo;

    @SerializedName("email_address")
    private String emailAddress;

    @SerializedName("town")
    private String town;

    @SerializedName("address")
    private String address;

    @SerializedName("passport_base64")
    private String passportBase64;

    @SerializedName("signature_base64")
    private String signatureBase64;

    @SerializedName("physical_address")
    private String physicalAddress;

    @SerializedName("county")
    private String county;

    @SerializedName("sub_county_code")
    private String subCountyCode;

    @SerializedName("ward_name")
    private String wardName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("marital_status")
    private String maritalStatus;
    @SerializedName("id_front_base64")
    private String idFrontBase64;

    @SerializedName("id_back_base64")
    private String idBackBase64;
    @SerializedName("branch")
    private String branch;


    @SerializedName("cluster")
    private String cluster;
    @SerializedName("disability")
    private String disability;
    @SerializedName("specify_disability")
    private String specifyDisability;
    @SerializedName("introducer_name")
    private String introducerName;
    @SerializedName("introducer_id")
    private String introducerId;
    @SerializedName("introducer_phone_no")
    private String introducerPhoneNo;



    public AddNextOfKinRequest(String names, String dateOfBirth, String idNo, String mobileNo,
                               String emailAddress, String town, String address, String idFrontBase64, String idBackBase64,
                               String signatureBase64, String passportBase64, String physicalAddress, String county,
                               String subCountyCode, String wardName, String gender,
                               String maritalStatus, List<NextOfKin> nextOfKin,String branch, String cluster, String disability, String specifyDisability, String introducerName,
                               String introducerId, String introducerPhoneNo) {
        this.names = names;
        this.dateOfBirth = dateOfBirth;
        this.idNo = idNo;
        this.mobileNo = mobileNo;
        this.emailAddress = emailAddress;
        this.town = town;
        this.address = address;
        this.idFrontBase64 = idFrontBase64;
        this.idBackBase64 = idBackBase64;
        this.signatureBase64 = signatureBase64;
        this.passportBase64 = passportBase64;
        this.physicalAddress = physicalAddress;
        this.county = county;
        this.subCountyCode = subCountyCode;
        this.wardName = wardName;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.nextOfKin = nextOfKin;
        this.branch = branch;
        this.cluster = cluster;
        this.disability = disability;
        this.specifyDisability = specifyDisability;
        this.introducerName = introducerName;
        this.introducerId = introducerId;
        this.introducerPhoneNo = introducerPhoneNo;
    }

    @SerializedName("next_of_kin")
    private List<NextOfKin> nextOfKin;

    // Getters and Setters
    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassportBase64() {
        return passportBase64;
    }

    public void setPassportBase64(String passportBase64) {
        this.passportBase64 = passportBase64;
    }

    public String getSignatureBase64() {
        return signatureBase64;
    }

    public void setSignatureBase64(String signatureBase64) {
        this.signatureBase64 = signatureBase64;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSubCountyCode() {
        return subCountyCode;
    }

    public void setSubCountyCode(String subCountyCode) {
        this.subCountyCode = subCountyCode;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public List<NextOfKin> getNextOfKin() {
        return nextOfKin;
    }

    public void setNextOfKin(List<NextOfKin> nextOfKin) {
        this.nextOfKin = nextOfKin;
    }

    // Inner class for Next of Kin
    public static class NextOfKin implements Serializable {

        @SerializedName("name")
        private String name;

        @SerializedName("date_of_birth")
        private String dateOfBirth;

        @SerializedName("id_no")
        private String idNo;

        @SerializedName("telephone")
        private String telephone;

        @SerializedName("email")
        private String email;

        @SerializedName("town")
        private String town;

        @SerializedName("address")
        private String address;

        public NextOfKin(String name, String dateOfBirth, String idNo, String telephone, String email, String town, String address, String allocation) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.idNo = idNo;
            this.telephone = telephone;
            this.email = email;
            this.town = town;
            this.address = address;
            this.allocation = allocation;
        }

        @SerializedName("allocation")
        private String allocation;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getIdNo() {
            return idNo;
        }

        public void setIdNo(String idNo) {
            this.idNo = idNo;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAllocation() {
            return allocation;
        }

        public void setAllocation(String allocation) {
            this.allocation = allocation;
        }
    }
}
