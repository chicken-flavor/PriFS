package pri.ayyj.fuxk.util;

import android.content.Context;
import android.location.LocationManager;
import android.support.annotation.StringRes;
import android.util.Log;

import pri.ayyj.fuxk.ui.CustomToast;

/**
 * Created by yangyongjun on 2018/2/27 0027.
 * <p>
 * 通用工具类
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Utils {

    public static void toast(String msg) {
        CustomToast.INSTANCE.showToast(msg);
    }

    public static void toast(@StringRes int resId) {
        CustomToast.INSTANCE.showToast(resId);
    }

    public static void toastNotClear(String msg) {
        CustomToast.INSTANCE.showToastWithoutClearText(msg);
    }

    public static void log(Object obj, String msg) {
        Log.e(obj.getClass().getName(), msg);
    }

    /**
     * 判断手机GPS是否开启
     */
    public static boolean isGpsOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null &&
                (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)/*gps*/ || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)/*agps*/);
    }
}
