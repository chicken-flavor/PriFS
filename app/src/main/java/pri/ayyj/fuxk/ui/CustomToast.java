package pri.ayyj.fuxk.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pri.ayyj.fuxk.App;
import pri.ayyj.fuxk.R;

/**
 * Created by jcyyj on 2017/8/4.
 * <p>
 * Toast
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public enum CustomToast {

    /* 实现单例 */
    @SuppressLint("StaticFieldLeak")
    INSTANCE;

    private Toast mToast;
    private TextView mToastTv;

    public void showToast(String content) {
        if (mToast == null) {
            Context context = App.getInstance().getApplicationContext();
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            View root = LayoutInflater.from(context).inflate(R.layout.toast, null);
            mToastTv = root.findViewById(R.id.toast_tv);
            mToast.setView(root);
        }
        mToastTv.setText(content);
        mToast.show();
    }

    @SuppressLint("SetTextI18n")
    public void showToastWithoutClearText(String content) {
        if (mToast == null) {
            Context context = App.getInstance().getApplicationContext();
            mToast = new Toast(context);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.setDuration(Toast.LENGTH_SHORT);
            View root = LayoutInflater.from(context).inflate(R.layout.toast, null);
            mToastTv = root.findViewById(R.id.toast_tv);
            mToast.setView(root);
            mToastTv.setText(content);
            mToast.show();
        } else {
            // 不为空则换行显示
            mToastTv.setText(mToastTv.getText().toString() + "\r\n" + content);
            mToast.show();
        }
    }

    public void showToast(@StringRes int stringResId) {
        Context context = App.getInstance().getApplicationContext();
        showToast(context.getString(stringResId));
    }

    public void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
            mToastTv = null;
        }
    }
}