package ke.co.shofcosacco.app.models;


public class NextOfKinNew {

    // Fields
    private String name;
    private String dateOfBirth;
    private String idNo;
    private String telephone;
    private String email;
    private String town;
    private String address;
    private String allocation;

    public String getRelationshipTypeCode() {
        return relationshipTypeCode;
    }

    private String relationshipTypeCode;

    // Constructor
    public NextOfKinNew(String name, String dateOfBirth, String idNo, String telephone, String email,
                        String town, String address, String allocation, String relationshipTypeCode) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.idNo = idNo;
        this.telephone = telephone;
        this.email = email;
        this.town = town;
        this.address = address;
        this.allocation = allocation;
        this.relationshipTypeCode = relationshipTypeCode;
    }

    // Getter and Setter methods

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

    // Display information
    @Override
    public String toString() {
        return "Next of Kin: " + name + "\n" +
                "Date of Birth: " + dateOfBirth + "\n" +
                "ID No: " + idNo + "\n" +
                "Telephone: " + telephone + "\n" +
                "Email: " + email + "\n" +
                "Town: " + town + "\n" +
                "Address: " + address + "\n" +
                "Allocation: " + allocation;
    }
}
