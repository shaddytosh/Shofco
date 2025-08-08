package ke.co.shofcosacco.ui.onboarding;


import android.content.Context;
import android.content.SharedPreferences;

public class Onboarding {
    private static final String PREF_NAME = "onboarding_prefs";

    // Unique keys (namespaced)
    private static final String KEY_FIRST_NAME = "onboarding_first_name";
    private static final String KEY_LAST_NAME = "onboarding_last_name";
    private static final String KEY_NATIONAL_ID = "onboarding_national_id";
    private static final String KEY_TELEPHONE = "onboarding_telephone";
    private static final String KEY_EMAIL = "onboarding_email";
    private static final String KEY_TOWN = "onboarding_town";
    private static final String KEY_ADDRESS = "onboarding_address";
    private static final String KEY_DOB = "onboarding_dob";
    private static final String KEY_GENDER = "onboarding_gender";
    private static final String KEY_STATUS = "onboarding_status";

    private final SharedPreferences sharedPreferences;

    public Onboarding(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Save values
    public void saveUserData1(String firstName, String lastName, String nationalId, String telephone,
                             String email, String town, String address, String dob,
                             String gender, String status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_NATIONAL_ID, nationalId);
        editor.putString(KEY_TELEPHONE, telephone);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOWN, town);
        editor.putString(KEY_ADDRESS, address);
        editor.putString(KEY_DOB, dob);
        editor.putString(KEY_GENDER, gender);
        editor.putString(KEY_STATUS, status);
        editor.apply();
    }

    // Getters
    public String getFirstName() { return sharedPreferences.getString(KEY_FIRST_NAME, ""); }
    public String getLastName() { return sharedPreferences.getString(KEY_LAST_NAME, ""); }
    public String getNationalId() { return sharedPreferences.getString(KEY_NATIONAL_ID, ""); }
    public String getTelephone() { return sharedPreferences.getString(KEY_TELEPHONE, ""); }
    public String getEmail() { return sharedPreferences.getString(KEY_EMAIL, ""); }
    public String getTown() { return sharedPreferences.getString(KEY_TOWN, ""); }
    public String getAddress() { return sharedPreferences.getString(KEY_ADDRESS, ""); }
    public String getDateOfBirth() { return sharedPreferences.getString(KEY_DOB, ""); }
    public String getGender() { return sharedPreferences.getString(KEY_GENDER, ""); }
    public String getStatus() { return sharedPreferences.getString(KEY_STATUS, ""); }

    // Clear all saved data
    public void clear() {
        sharedPreferences.edit().clear().apply();
    }

    // Check if data exists


    public boolean hasData1() {
        return !getFirstName().isEmpty()
                && !getLastName().isEmpty()
                && !getNationalId().isEmpty()
                && !getTelephone().isEmpty()
                && !getTown().isEmpty()
                && !getAddress().isEmpty()
                && !getDateOfBirth().isEmpty()
                && !getGender().isEmpty()
                && !getStatus().isEmpty();
    }

}
