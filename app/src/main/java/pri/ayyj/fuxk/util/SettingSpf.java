package pri.ayyj.fuxk.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import pri.ayyj.fuxk.App;

/**
 * Created by yangyongjun on 2018/4/17 0017.
 * <p>
 * 设置
 */

@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class SettingSpf {

    private static SharedPreferences sShared = App.getInstance().getSharedPreferences("_setting", Activity.MODE_PRIVATE);
    private static SharedPreferences.Editor sEdit = sShared.edit();

    public static void save(String key, String value) {
        sEdit.putString(key, value);
        sEdit.commit();
    }

    public static String get(String key) {
        return sShared.getString(key, "");
    }

    /**
     * 完整URL
     */
    public static final String URL = "url";
    /**
     * 协议
     */
    public static final String PROTOCOL = "protocol";
    /**
     * IP
     */
    public static final String IP = "ip";
    /**
     * 端口
     */
    public static final String PORT = "port";
    /**
     * 域名
     */
    public static final String DOMAIN = "domain";
    /**
     * url使用，ip或domain
     */
    public static final String USE_TPYE = "use_type";
    /**
     * 虚拟路径
     */
    public static final String VIRTUAl_DIR = "virtual_dir";
    /**
     * 是否使用虚拟路径，"y" or "n"
     */
    public static final String USE_VIRTUAl_DIR = "use_virtual_dir";
    /**
     * GPS获取频率
     */
    public static final String GPS_FREQ = "gps_freq";
    /**
     * 所在地区
     */
    public static final String LOCATION = "location";


    /**
     * 工作时间 格式：xx:xx|xx:xx|xx:xx|xx:xx (上午起始到终止，下午起始到终止)
     */
    public static final String WORKING_TIME = "working_time";
    /**
     * 配置信息
     */
    public static final String CONFIG_LATLNG = "is_latlng_from_map";

    public static final String CONFIG_RENT_DEV = "is_dev_rent";

    public static final String CONFIG_FUCTION = "function";

    public static final String CONFIG_SYNC = "sync";

    // test! test! app升级
    public static final String TEST_UPDATE_APK = "test_update_apk";

    static {
        // 首次检查
        boolean check = TextUtils.isEmpty(get(URL));
        if (check) {
            save(URL, "http://192.168.6.125:7001/");
            save(PROTOCOL, "http");
            save(IP, "192.168.0.110");
            save(PORT, "7001");
            save(DOMAIN, "www.xxx.yyy");
            save(VIRTUAl_DIR, "WebApi");
            save(USE_TPYE, "ip");/*域名*/
            save(USE_VIRTUAl_DIR, "n");/*n*/
        }
    }
}
