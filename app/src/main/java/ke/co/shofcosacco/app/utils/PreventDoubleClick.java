package ke.co.shofcosacco.app.utils;

import android.view.View;

public class PreventDoubleClick {

    private static final long DELAY_IN_MS = 500;

    public static void preventMultiClick(final View view) {
        if (!view.isClickable()) {
            return;
        }
        view.setClickable(false);
        view.postDelayed(() -> view.setClickable(true), DELAY_IN_MS);
    }
}
