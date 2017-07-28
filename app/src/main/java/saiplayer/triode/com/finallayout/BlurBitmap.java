package saiplayer.triode.com.finallayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by dell on 23-Jul-17.
 */

public class BlurBitmap {

    private static final float BLUR_RADIUS=25f;

    public static Bitmap Blur (Context context,Bitmap inputBitmap){

        Bitmap outputImage=Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript=RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur=ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));

        Allocation input=Allocation.createFromBitmap(renderScript,inputBitmap);
        Allocation output=Allocation.createFromBitmap(renderScript,outputImage);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(input);
        intrinsicBlur.forEach(output);
        output.copyTo(outputImage);

        return outputImage;

    }

}
