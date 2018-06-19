package pri.ayyj.fuxk.base;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.List;

import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.util.Utils;

/**
 * Created by yangyongjun on 2018/2/27 0027.
 * <p>
 * the base of activity
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class ActivityBase extends AppCompatActivity {

    /**
     * 权限请求回调
     */
    public interface CheckBack {
        /**
         * 回调方法，只有成功才会回调
         */
        void requestSuccess();
    }

    /**
     * {@link Activity#startActivityForResult} 回调
     */
    public interface ResultCallBack {
        /**
         * 回调方法，只有成功才会回调
         */
        void requestResult(int requestCode, int resultCode, Intent data);
    }

    /**
     * 获取相关权限请求码
     */
    protected static final int REQUEST_PERMISSION = 438;
    /**
     * 结束请求码，返回收到此码则finish掉
     */
    protected static final int REQUEST_FINISH = 788;
    /**
     * 结束响应码
     */
    protected static final int RESULT_FINISH = 789;

    private String[] mRequestPermissions;
    private List<String> mDeniedPermissions = new ArrayList<>();

    /**
     * 权限请求回调
     */
    private CheckBack mCheckBack;
    /**
     * Start Activity For Result 回调
     */
    private ResultCallBack mResultCallBack;
    /**
     * 吉吉格尔（自己个儿）
     */
    protected Activity activity;

    /**
     * 标识第一次进入，该值应在一个确定场景标识为false
     */
    protected boolean first = true;
    /**
     * NONE
     */
    public static final int STATUS_DEFAULT = 0;
    /**
     * 透明
     */
    public static final int STATUS_TRANSPARENT = 1;
    /**
     * 沉浸
     */
    public static final int STATUS_IMMERSIVE = 1 << 1;
    /**
     * 亮色
     */
    public static final int STATUS_LIGHT = 1 << 2;

    public Bundle bundle;

    @LayoutRes
    protected abstract int getResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResId());
        activity = this;
        bundle = getIntent().getExtras();

        if (bundle == null)
            newBundle();
    }

    /**
     * 刷新Bundle
     */
    protected Bundle newBundle() {
        return bundle = new Bundle();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (statusBarMode() > 0 && hasFocus) {
            int option = 0;
            int mode = statusBarMode();
            View decorView = getWindow().getDecorView();

            if ((mode | STATUS_IMMERSIVE) == mode) {
                if (Build.VERSION.SDK_INT >= 19) {
                    option = option | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
            }
            if ((mode | STATUS_TRANSPARENT) == mode) {
                if (Build.VERSION.SDK_INT >= 21) {
                    option = option | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;

                    getWindow().setStatusBarColor(Color.TRANSPARENT);
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null)
                        actionBar.hide();
                }
            }
            if ((mode | STATUS_LIGHT) == mode) {
                if (Build.VERSION.SDK_INT >= 23) {
                    option = option | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else if (Build.VERSION.SDK_INT >= 21) {
                    getWindow().setStatusBarColor(Color.BLACK);
                }
            }

            if (option != 0)
                decorView.setSystemUiVisibility(option);
        }
    }

    /**
     * 带过渡效果的转场
     */
    public final void startActivityWithTransition(Intent intent, View sharedElement, String sharedElementName) {
        if (Build.VERSION.SDK_INT < 21) {
            super.startActivity(intent);
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElement, getString(R.string.transition));
            super.startActivity(intent, options.toBundle());
        }
    }

    @SafeVarargs
    public final void startActivityWithTransition(Intent intent, Pair<View, String>... sharedElements) {
        if (Build.VERSION.SDK_INT < 21) {
            super.startActivity(intent);
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements);
            super.startActivity(intent, options.toBundle());
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.isLongPress()) {
            setResult(RESULT_FINISH);
            finish();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean hideInputMethod(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm != null && imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 设置状态栏效果，需要复写
     */
    protected int statusBarMode() {
        return STATUS_LIGHT;
    }

    /**
     * 提供有回调的 {@link Activity#startActivityForResult} 方法，供 Activity 外部使用
     */
    public final void startActivityForResult(@NonNull ResultCallBack resultCallBack, Intent intent, int requestCode) {
        mResultCallBack = resultCallBack;
        startActivityForResult(intent, requestCode);
    }

    public final void waitForResult(@NonNull ResultCallBack resultCallBack) {
        mResultCallBack = resultCallBack;
    }

    //<editor-fold desc="权限请求">

    /**
     * 检查权限
     */
    public final void startCheckPermission(@NonNull CheckBack checkBack, @NonNull String... permissions) {
        if (permissions.length < 1)
            return;

        mDeniedPermissions.clear();
        mPermissionName = "";
        mCheckBack = checkBack;
        mRequestPermissions = permissions;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean denyExits = false;
            for (String p : mRequestPermissions) {
                // 检查该权限是否已经获取
                if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                    catchPermissionName(p);
                    denyExits = true;
                }
            }
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (denyExits) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            } else {
                mCheckBack.requestSuccess();
                mCheckBack = null;
            }
        } else {
            mCheckBack.requestSuccess();
            mCheckBack = null;
        }
    }

    private String mPermissionName;

    private void catchPermissionName(String permission) {
        mDeniedPermissions.add(permission);
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                mPermissionName += " [定位] ";
                break;
            case Manifest.permission.RECORD_AUDIO:
                mPermissionName += " [录音] ";
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                mPermissionName += " [写入] ";
                break;
            case Manifest.permission.CAMERA:
                mPermissionName += " [相机] ";
                break;
            default:
                mPermissionName += " [] ";
                break;
        }
    }

    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setMessage("应用需要" + mPermissionName + "权限")
                .setPositiveButton("立即开启", (dialog, which) -> {
                    dialog.dismiss();
                    startRequestPermission();
                })
                .setNegativeButton("取消", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    /**
     * 提交请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, mRequestPermissions, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults.length > 0) {
                    mDeniedPermissions.clear();
                    mPermissionName = "";

                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean remindAgain = false;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (shouldShowRequestPermissionRationale(permissions[i])) {
                            catchPermissionName(permissions[i]);
                            remindAgain = true;
                        }
                    }

                    if (remindAgain) {
                        showDialogTipUserGoToAppSettting();
                    } else {
                        mDeniedPermissions.clear();
                        mPermissionName = "";
                        // 再次判断是否有权限
                        boolean bool = false;
                        for (int i = 0; i < grantResults.length; i++) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                catchPermissionName(permissions[i]);
                                bool = true;
                            }
                        }
                        if (!bool) {
                            Utils.toast("授权成功");
                            mCheckBack.requestSuccess();
                            mCheckBack = null;
                        } else startRequestPermission();
                    }
                } else {
                    Utils.toast("授权成功");
                    mCheckBack.requestSuccess();
                    mCheckBack = null;
                }
            }
        }
    }

    /**
     * 权限申请 Dialog
     */
    private AlertDialog mDialog;

    private void showDialogTipUserGoToAppSettting() {
        mDialog = new AlertDialog.Builder(this)
                .setTitle(mPermissionName + "权限被禁止")
                .setMessage("请在 -应用设置-权限- 允许使用以上权限")
                .setPositiveButton("立即开启", (dialog, which) -> goToAppSetting())
                .setNegativeButton("取消", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    /**
     * 跳转设置界面
     */
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult((requestCode, resultCode, data) -> {
            if (requestCode == REQUEST_PERMISSION) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mDeniedPermissions.clear();
                    mPermissionName = "";

                    for (String p : mRequestPermissions) {
                        // 检查该权限是否已经获取
                        int i = ContextCompat.checkSelfPermission(this, p);
                        if (i != PackageManager.PERMISSION_GRANTED) {
                            catchPermissionName(p);
                        }
                    }
                    if (mDeniedPermissions.size() > 0) {
                        // 提示用户应该去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        if (mDialog != null && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Utils.toast("授权成功");
                        mCheckBack.requestSuccess();
                        mCheckBack = null;
                    }
                }
            }
        }, intent, REQUEST_PERMISSION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mResultCallBack != null) {
            mResultCallBack.requestResult(requestCode, resultCode, data);
            mResultCallBack = null;
        }

        if (requestCode == REQUEST_FINISH) {
            // 监听返回键长按事件，清空回退栈（快速回退）
            if (resultCode == RESULT_FINISH) {
                setResult(RESULT_FINISH);
                finish();
            }
        }
    }
    //</editor-fold>
}

