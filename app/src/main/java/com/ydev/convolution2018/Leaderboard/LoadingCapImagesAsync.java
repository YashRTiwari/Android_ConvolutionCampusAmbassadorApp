package com.ydev.convolution2018.Leaderboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by YRT on 16/12/2017.
 */

public class LoadingCapImagesAsync extends AsyncTask<String, Void, Bitmap> {

    private WeakReference<ImageView> imageViewWeakReference;
    private Context context;


    public LoadingCapImagesAsync(Context context, ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... param) {

        InputStream inputStream = null;
        Bitmap image = null;
        try {
            URL url = new URL(param[0]);
            inputStream = url.openStream();
            image = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception ignored) {

        }
        return image;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null && imageViewWeakReference != null) {

            ImageView imageView = imageViewWeakReference.get();
            if (imageView != null) {
                Bitmap image = bitmap;

                RoundedBitmapDrawable roundImage = RoundedBitmapDrawableFactory.create(context.getResources(), image);
                roundImage.setCircular(true);
                roundImage.setCornerRadius(48);

                Bitmap bitmap1 = (roundImage).getBitmap();
                imageView.setImageDrawable(roundImage);
                //Picasso.with(context).load(roundImage).fit().into(imageView);
            }
        }
    }
}
