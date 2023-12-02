package ke.co.shofcosacco.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

public class PdfUtils {
    public static void shareBase64Pdf(Context context, String base64Pdf, String loanNo) {
        // Convert Base64 string to bytes
        byte[] pdfBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pdfBytes = Base64.getDecoder().decode(base64Pdf);
        }

        String filename = loanNo + "_" + getCurrentDateTime() + ".pdf";

        // Create a PDF file
        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            outputStream.write(pdfBytes);
            outputStream.close();

            Toast.makeText(context, filename + " saved to " + pdfFile.getPath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Open the PDF file with an intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdfFile);
        intent.setDataAndType(uri, "application/pdf");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Share PDF"));
    }

    private static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public static String getPdfPath(Context context, String base64Pdf, String loanNo) {
        // Convert Base64 string to bytes
        byte[] pdfBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            pdfBytes = Base64.getDecoder().decode(base64Pdf);
        }

        String filename = loanNo + "_" + getCurrentDateTime() + ".pdf";

        // Create a PDF file
        File pdfFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filename);

        try {
            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            outputStream.write(pdfBytes);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdfFile.getPath();
    }
}
