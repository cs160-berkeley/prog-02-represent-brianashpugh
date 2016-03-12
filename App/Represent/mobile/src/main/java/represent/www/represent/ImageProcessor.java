package represent.www.represent;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Log;

/**
 * From http://stackoverflow.com/questions/11932805/cropping-circular-area-from-bitmap-in-android
 */
public class ImageProcessor {

    public Bitmap cropToCoverSize(Bitmap bitmap) {
        Bitmap bitmapCover = bitmap;
        Integer bitmapHeight = bitmap.getHeight();
        Integer bitmapWidth = bitmap.getWidth();
        Double targetRatio = 663.0/1080.0; // height to width
        if (bitmapHeight.doubleValue()/bitmapWidth.doubleValue() > targetRatio) { // too high
            Double targetHeight = bitmapWidth.doubleValue()*targetRatio;
            int targetHeightRounded = targetHeight.intValue();
            int toTrim = bitmapHeight - targetHeightRounded;
            try {
                bitmapCover = Bitmap.createBitmap(bitmap, 0, (toTrim + 1) / 2, bitmapWidth, bitmapHeight - toTrim);
            }
            catch (IllegalArgumentException e){

            }
        }
        else { // too wide
            Double targetWidth = bitmapHeight.doubleValue()/targetRatio;
            int targetWidthRounded = targetWidth.intValue();
            int toTrim = bitmapWidth - targetWidthRounded;
            bitmapCover = Bitmap.createBitmap(bitmap, (toTrim+1)/2, 0, bitmapWidth - toTrim, bitmapHeight);
        }
        Log.d("phone", "Trimmed Bitmap to have ratio " + (new Double(bitmapCover.getHeight())/(new Double(bitmapCover.getWidth()))));
        Log.d("phone", "Target ratio is " + targetRatio);
        return bitmapCover;
    }

    public Bitmap maskCircle(Bitmap bitmap) {
        Bitmap bitmapSquare = bitmap;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        if (bitmapHeight > bitmapWidth) {
            int toTrim = bitmapHeight - bitmapWidth;
            bitmapSquare = Bitmap.createBitmap(bitmap, 0, (toTrim+1)/2, bitmapWidth, bitmapHeight - (toTrim/2));
        }
        else if (bitmapWidth > bitmapHeight) {
            int toTrim = bitmapWidth - bitmapHeight;
            bitmapSquare = Bitmap.createBitmap(bitmap, (toTrim+1)/2, 0, bitmapWidth - (toTrim/2), bitmapWidth);
        }
        Bitmap output = Bitmap.createBitmap(bitmapSquare.getWidth(),
                bitmapSquare.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawARGB(0, 0, 0, 0);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        final Rect rect = new Rect(0, 0, bitmapSquare.getWidth(), bitmapSquare.getHeight());

        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmapSquare.getWidth() / 2, bitmapSquare.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapSquare, rect, rect, paint);

        return output;
    }
}
