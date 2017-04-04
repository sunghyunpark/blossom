package common;

import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;
import com.yssh1020.blossom.AppController;

/**
 * Created by sunghyun on 2017. 3. 27..
 * Picasso
 */

public class PicassoTransformations {


    public static Transformation resizeTransformation = new Transformation() {
        @Override
        public Bitmap transform(Bitmap source) {
            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (AppController.getInstance().getDISPLAY_WIDTH() * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, AppController.getInstance().getDISPLAY_WIDTH(), targetHeight, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "resizeTransformation#" + System.currentTimeMillis();
        }
    };
}