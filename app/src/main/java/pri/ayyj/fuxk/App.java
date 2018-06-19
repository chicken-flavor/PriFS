package pri.ayyj.fuxk;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.WindowManager;


public class App extends Application {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static final boolean DEBUG = BuildConfig.DEBUG;

    private static App sInstance;

    public static App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initParam();
    }

    /**
     * DisplayMetrics
     */
    public static DisplayMetrics DM;
    /**
     * 屏幕宽度（像素）
     */
    public static int HEIGHT;
    /**
     * 屏幕高度（像素）
     */
    public static int WIDTH;
    /**
     * 系统状态栏高度
     */
    public static int STATUS_BAR_HEIGHT;

    /**
     * 获取屏幕参数
     */
    private void initParam() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DM = new DisplayMetrics();
        if (wm == null)
            return;

        wm.getDefaultDisplay().getMetrics(DM);

        WIDTH = DM.widthPixels;
        HEIGHT = DM.heightPixels;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            // 根据资源ID获取相应尺寸值
            STATUS_BAR_HEIGHT = getResources().getDimensionPixelSize(resourceId);
        }
    }
}
