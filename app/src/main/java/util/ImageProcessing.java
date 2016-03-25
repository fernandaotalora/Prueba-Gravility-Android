package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by User on 23/03/2016.
 */
public class ImageProcessing {

    /**
     * Encode a Bitmap into Base64
     * @param bitmap Bitmap
     * @param compress int, compression quality, from 0 to 100
     * @return String
     */
    public static String encodeBitmapToBase64(Bitmap bitmap, int compress) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, compress, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    /**
     * Decode a Bitmap from Base64
     * @param encoded String
     * @return Bitmap
     */
    public static Bitmap decodeBitmapFromBase64(String encoded) {
        byte[] decodedString = Base64.decode(encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
