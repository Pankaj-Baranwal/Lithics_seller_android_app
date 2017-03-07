package com.fame.plumbum.lithicsin;

import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.fame.plumbum.lithicsin.utils.BitmapCache;

/**
 * Created by pankaj on 25/2/17.
 */

public class Singleton extends MultiDexApplication {

    public final String TAG = Singleton.class.getSimpleName();
    private static Singleton mInstance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        requestQueue = getRequestQueue();
    }

    public static synchronized Singleton getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getApplicationContext());

        return requestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(this.requestQueue,
                    new BitmapCache());
        }
        return this.imageLoader;
    }

    public <T>void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }

    public <T>void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }


}
