package pri.ayyj.fuxk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pri.ayyj.fuxk.base.ActivityBase;
import pri.ayyj.fuxk.ui.FileFragment;
import pri.ayyj.fuxk.ui.InetAddressActivity;
import pri.ayyj.fuxk.ui.SimpleSwitchPopWin;
import pri.ayyj.fuxk.util.Utils;

/**
 * @author ayyj
 */
public class MainActivity extends ActivityBase implements Toolbar.OnMenuItemClickListener {

    @Bind(R.id.toolbar_left_iv)
    ImageView toolbarLeftIv;
    @Bind(R.id.toolbar_left_tv)
    TextView toolbarLeftTv;
    @Bind(R.id.toolbar_title_tv)
    TextView toolbarTitleTv;
    @Bind(R.id.toolbar_right_tv)
    TextView toolbarRightTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv)
    TextView tv;
    @Bind(R.id.content)
    RelativeLayout content;

    static FragmentManager fm;
    static List<String> tags = new ArrayList<>();

    @Override
    protected int getResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        toolbarLeftTv.setText(R.string.app_name);
        toolbarLeftIv.setOnClickListener(v -> onBackPressed());
//        toolbar.inflateMenu(R.menu.main_toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);
        tv.setOnClickListener(v -> startActivity(new Intent(this, InetAddressActivity.class)));
        toolbarLeftTv.setOnClickListener(v -> showDriveDialog());

        fm = getSupportFragmentManager();
    }

    private SimpleSwitchPopWin mSwitchPopup;

    private void showDriveDialog() {
        if (mSwitchPopup == null) {
            mSwitchPopup = new SimpleSwitchPopWin(this);
            List<String> datas = Arrays.asList(getResources().getStringArray(R.array.drive));
            mSwitchPopup.setDatas(datas);
            mSwitchPopup.setOnSelectListener((pointer, item) -> {
                mSwitchPopup.dismiss();
                content.postDelayed(() -> {
                    popAllFragment();
                    addFragment(datas.get(pointer), "", "");
                }, 300);
            });
            mSwitchPopup.setOnDismissListener(() -> mSwitchPopup.setWindowAlpha(false));
        }
        mSwitchPopup.show(content);
        mSwitchPopup.setWindowAlpha(true);
    }

    public static void addFragment(String drive, String path, String tag) {
        FragmentTransaction trans = fm.beginTransaction();
        FileFragment f = new FileFragment();
        Bundle bundle = new Bundle();
        bundle.putString("drive", drive);
        bundle.putString("path", path);
        f.setArguments(bundle);
        trans.add(R.id.content, f, tag);
        trans.addToBackStack(tag);
        tags.add(tag);
        trans.commit();
    }

    public static void addFragment(String path, String tag) {
        addFragment("", path, tag);
    }

    public static void popFragment() {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

    public static void popAllFragment() {
        while (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            int backStackEntryCount = fm.getBackStackEntryCount();
//            if (backStackEntryCount > 0) {
//                fm.popBackStackImmediate();
//                FragmentManager.BackStackEntry backStack = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
//                String tag = backStack.getName();
//            } else {
//                //回退栈中只剩一个时,退出应用
//                finish();
//            }
//        }
//        return true;
//    }

    private long mLastMillils;

    @Override
    public void onBackPressed() {
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else if (System.currentTimeMillis() - mLastMillils > 1500) {
            Utils.toast("double click oh yeah ~");
            mLastMillils = System.currentTimeMillis();
        } else super.onBackPressed();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.d:
                break;
            case R.id.e:
                break;
            case R.id.f:
                break;
            case R.id.g:
                break;
            case R.id.h:
                break;
            default:
                Utils.toast("找不到");
                return true;
        }
        toolbar.postDelayed(() -> {

        }, 300);
        return true;
    }
}
