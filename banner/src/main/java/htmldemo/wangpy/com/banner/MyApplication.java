package htmldemo.wangpy.com.banner;

import android.app.Application;
import android.content.Context;

/**
 * Created by dell on 2017/8/27.
 */

public class MyApplication extends Application {
    public static Context CONTEXT;
    public static boolean flag = false;

    @Override
    public void onCreate() {
        super.onCreate();
        setContext(this);
    }

    private static void setContext(Context mContext) {
        CONTEXT = mContext;
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
