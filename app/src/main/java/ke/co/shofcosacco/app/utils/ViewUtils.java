package ke.co.shofcosacco.app.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.location.LocationManagerCompat;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ViewUtils {

    public static String getCurrencyAmount(String amount){

        if (amount!=null){
            double mAmount= Double.parseDouble(amount.replace(",",""));
            NumberFormat format = NumberFormat.getCurrencyInstance();
            format.setMaximumFractionDigits(0);
            format.setCurrency(Currency.getInstance("KES"));

            return format.format(mAmount).replace(".00","");
        }
        return null;
    }


    public static String getTransactionDate(){//f7
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static String getTransactionDateyyyyMMdd(){//f7
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static String getMinuteSecond() {//f11
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("mmss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static String getHourMinuteSecond() {//f23
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    public static String getHourMinute() {//f13
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HHmm", Locale.getDefault());
        return dateFormat.format(currentDate);
    }


    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return LocationManagerCompat.isLocationEnabled(locationManager);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static<T extends TextView> String getText(@NonNull T textView){
        return textView.getText()==null?"":textView.getText().toString().trim();
    }

    public static String formatQueryTIme(String rawUnixTime){
        long unixTime = Long.parseLong(rawUnixTime); // current Unix time in seconds
        Date date = new Date(unixTime * 1000L); // convert Unix time to milliseconds

        String formattedTime;
        String beautifiedDate=null;


        long diffSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - unixTime * 1000L);
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - unixTime * 1000L);


        if (diffSeconds <= 59) {
            formattedTime = "Just now";
            beautifiedDate=formattedTime;
        }else if (diffMinutes <= 59) {
            formattedTime = diffMinutes + " min ago";
            beautifiedDate=formattedTime;
        } else {


            // Check if the date is today
            Calendar calendar = Calendar.getInstance();
            int currentDay = calendar.get(Calendar.DAY_OF_YEAR);
            int currentYear = calendar.get(Calendar.YEAR);
            calendar.setTime(date);
            int messageDay = calendar.get(Calendar.DAY_OF_YEAR);
            int messageYear = calendar.get(Calendar.YEAR);


            DateFormat dateFormat;
            if ((currentDay - messageDay) == 1) {
                // The message was sent today
                dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                formattedTime = dateFormat.format(date);
                beautifiedDate="Yesterday at "+formattedTime;

            }else if (currentDay == messageDay) {
                // The message was sent today
                dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
                formattedTime = dateFormat.format(date);
                beautifiedDate=formattedTime;
            }else  if ((currentYear - messageYear) == 1) {
                // The message was sent today
                dateFormat = new SimpleDateFormat("yyyy MM dd, h:mm a", Locale.getDefault());
                formattedTime = dateFormat.format(date);
                beautifiedDate=formattedTime;

            }else {
                // The message was sent on a previous day
                dateFormat = new SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault());
                formattedTime = dateFormat.format(date);
                beautifiedDate=formattedTime;

            }
        }

        return beautifiedDate;
    }

    public static String maskPhoneNumber(String phoneNumber){

        return "0" + phoneNumber.substring(3, 7) + "XXX" + phoneNumber.substring(9);
    }

    public static void setLocale(Context context,String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    private Bitmap drawableToBitmap2(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
    }


}
