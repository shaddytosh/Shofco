package ke.co.shofcosacco.app.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

public abstract class TextValidator implements TextWatcher {
    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public final void afterTextChanged(Editable s) {
        validate(s.toString());
    }

    public abstract void validate(String s);

    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
    public static boolean isEmptyOrSpace(String input) {
        return input == null || isEmpty(input.trim());
    }
    public static boolean isPhoneNumber(String text) {
        return text != null && Patterns.PHONE.matcher(text).matches();
    }
    public static boolean isDigitsOnly(String text) {
        return text != null && text.matches("\\d+");
    }
    public static boolean isLongerThan(String text, int length) {
        return text != null && text.length() > length;
    }
    public static boolean isEmail(String text) {
        return text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    public static boolean isNationalID(String text) {
        return text != null && text.length()==7 || text != null && text.length()==8 || text != null && text.length()==10;
    }

}
