package com.kehinde.bakingapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by kehinde on 6/3/17.
 */

public class ServiceUtil {

    public static boolean isOnline(Context context){

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

    public static void loadImageFromResourceInto(Context c, ImageView imageView, String url)
    {
        Glide.with(c).asBitmap().load(url).into(imageView);
    }
}
