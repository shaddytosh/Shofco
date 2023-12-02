package ke.co.shofcosacco.app.utils;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditTextValidator {

    public static void afterTextChanged(EditText editText, AfterTextChangedListener afterTextChangedListener) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                afterTextChangedListener.onAfterTextChanged(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    public interface AfterTextChangedListener {
        void onAfterTextChanged(String s);
    }

    public static void isValid(EditText editText, Handler handler, TextInputLayout inputLayout,
                               Validator validator, String message) {
        afterTextChanged(editText, s -> {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> isValid(editText, validator, inputLayout, message), 1000);
        });
    }

    public static void isValid(AutoCompleteTextView autoCompleteTextView, Handler handler,
                               TextInputLayout inputLayout, Validator validator, String message) {
        afterTextChanged(autoCompleteTextView, s -> {
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(() -> isValid(autoCompleteTextView, validator, inputLayout, message), 1000);
        });
    }

    public static boolean isValid(EditText editText, Validator validator, TextInputLayout inputLayout, String message) {
        String text = editText.getText().toString();
        if (validator.isValid(text)) {
            if (inputLayout.isErrorEnabled()) {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
            return true;
        } else {
            editText.getParent().requestChildFocus(editText, inputLayout);
            if (!inputLayout.isErrorEnabled()) {
                inputLayout.setErrorEnabled(true);
            }
            inputLayout.setError(message);
            return false;
        }
    }

    public static boolean isValid(AutoCompleteTextView autoCompleteTextView, Validator validator,
                                  TextInputLayout inputLayout, String message) {
        String text = autoCompleteTextView.getText().toString();
        if (validator.isValid(text)) {
            if (inputLayout.isErrorEnabled()) {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
            return true;
        } else {
            autoCompleteTextView.getParent().requestChildFocus(autoCompleteTextView, inputLayout);
            if (!inputLayout.isErrorEnabled()) {
                inputLayout.setErrorEnabled(true);
            }
            inputLayout.setError(message);
            return false;
        }
    }

    public static boolean isValid(TextView textView, Validator validator, TextInputLayout inputLayout, String message) {
        String text = textView.getText().toString();
        if (validator.isValid(text)) {
            if (inputLayout.isErrorEnabled()) {
                inputLayout.setError(null);
                inputLayout.setErrorEnabled(false);
            }
            return true;
        } else {
            textView.getParent().requestChildFocus(textView, inputLayout);
            if (!inputLayout.isErrorEnabled()) {
                inputLayout.setErrorEnabled(true);
            }
            inputLayout.setError(message);
            return false;
        }
    }

    public interface Validator {
        boolean isValid(String s);
    }

    public static boolean isValidEmail(String s) {
        return !s.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches();
    }

    public static boolean isValidCode(String s) {
        return !s.isEmpty() && s.length() == 4;
    }

    public static boolean isNotGreaterThanQuantity(String s, String quantity) {
        if (quantity != null && !quantity.trim().isEmpty() && s != null && !s.trim().isEmpty()) {
            return Double.parseDouble(s) <= Double.parseDouble(quantity);
        }
        return true;
    }

    public static boolean isNotGreaterThanSellingPrice(String s, String price) {
        if (price != null && !price.trim().isEmpty() && s != null && !s.trim().isEmpty()) {
            return Double.parseDouble(s) < Double.parseDouble(price);
        }
        return true;
    }

    public static boolean isAdded(String name, ArrayList<String> listStores) {
        return listStores.contains(name);
    }
}