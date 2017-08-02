package info.scottb.scrollendlessly;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class that provides a Volley Request Queue.
 * Volley Request Queues are used to efficiently get data from the internet (e.g. our JSON content)
 */

public class VolleyRequestQueue {

    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mAppContext;

    private VolleyRequestQueue(Context context) {
        // Ensure that we're only storing a reference to the Application Context, otherwise we'll
        // create a memory leak.
        mAppContext = context.getApplicationContext();
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mAppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}