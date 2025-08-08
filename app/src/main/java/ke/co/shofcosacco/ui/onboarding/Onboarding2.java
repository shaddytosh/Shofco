package ke.co.shofcosacco.ui.onboarding;


import android.content.Context;
import android.content.SharedPreferences;

public class Onboarding2 {
    private static final String PREF_NAME = "onboarding_screen_two";

    private static final String KEY_BRANCH = "branch_code";
    private static final String KEY_CLUSTER = "cluster_code";
    private static final String KEY_DISABILITY = "disability";
    private static final String KEY_SPECIFY_DISABILITY = "specify_disability";
    private static final String KEY_INTRODUCER_NAME = "introducer_name";
    private static final String KEY_INTRODUCER_ID = "introducer_id";
    private static final String KEY_INTRODUCER_PHONE = "introducer_phone";

    private static final String KEY_EMPLOYMENT_STATUS = "employment_status";
    private static final String KEY_EMPLOYER_NAME = "employer_name";
    private static final String KEY_EMPLOYER_ADDRESS = "employer_address";
    private static final String KEY_EMPLOYER_INCOME = "employer_income";
    private static final String KEY_BUSINESS_NAME = "business_name";
    private static final String KEY_BUSINESS_LOCATION = "business_location";
    private static final String KEY_BUSINESS_INCOME = "business_income";

    private final SharedPreferences prefs;

    public Onboarding2(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void save(
            String branch, String cluster, String disability, String specifyDisability,
            String introducerName, String introducerId, String introducerPhone,
            String employmentStatus, String employerName, String employerAddress, String employerIncome,
            String businessName, String businessLocation, String businessIncome
    ) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_BRANCH, branch);
        editor.putString(KEY_CLUSTER, cluster);
        editor.putString(KEY_DISABILITY, disability);
        editor.putString(KEY_SPECIFY_DISABILITY, specifyDisability);
        editor.putString(KEY_INTRODUCER_NAME, introducerName);
        editor.putString(KEY_INTRODUCER_ID, introducerId);
        editor.putString(KEY_INTRODUCER_PHONE, introducerPhone);
        editor.putString(KEY_EMPLOYMENT_STATUS, employmentStatus);
        editor.putString(KEY_EMPLOYER_NAME, employerName);
        editor.putString(KEY_EMPLOYER_ADDRESS, employerAddress);
        editor.putString(KEY_EMPLOYER_INCOME, employerIncome);
        editor.putString(KEY_BUSINESS_NAME, businessName);
        editor.putString(KEY_BUSINESS_LOCATION, businessLocation);
        editor.putString(KEY_BUSINESS_INCOME, businessIncome);
        editor.apply();
    }

    public boolean hasData() {
        return prefs.contains(KEY_BRANCH) && !prefs.getString(KEY_BRANCH, "").isEmpty();
    }

    public String get(String key) {
        return prefs.getString(key, "");
    }

    public void clear() {
        prefs.edit().clear().apply();
    }

    public SharedPreferences getPrefs() {
        return prefs;
    }
}

