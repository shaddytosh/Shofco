package ke.co.shofcosacco.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class BlurUtils {

    public static Bitmap blurBitmap(Context context, Bitmap inputBitmap, float radius) {
        RenderScript renderScript = RenderScript.create(context);

        // Create an allocation from Bitmap
        Allocation input = Allocation.createFromBitmap(renderScript, inputBitmap);

        // Create an allocation with the same size
        Allocation output = Allocation.createTyped(renderScript, input.getType());

        // Use the Intrinsic Gausian blur script on the input allocation
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        script.setInput(input);
        script.setRadius(radius);
        script.forEach(output);

        // Copy the output allocation to the bitmap
        output.copyTo(inputBitmap);

        // Destroy everything to free memory
        input.destroy();
        output.destroy();
        script.destroy();
        renderScript.destroy();

        return inputBitmap;
    }
}
