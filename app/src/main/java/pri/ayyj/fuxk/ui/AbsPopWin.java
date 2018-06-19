package pri.ayyj.fuxk.ui;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import pri.ayyj.fuxk.App;
import pri.ayyj.fuxk.R;

/**
 * Created by yangyongjun on 2018/4/4 0004.
 * <p>
 * Abs PopupWindow
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbsPopWin extends PopupWindow {

    protected Context context;

    /**
     * PopupWindow 自定义顶级 View
     */
    protected View view;

    private boolean mAlphaEnable = true;

    public void setAlphaEnable(boolean alphaEnable) {
        mAlphaEnable = alphaEnable;
    }

    public AbsPopWin(Context context) {
        super(context);
        this.context = context;
        init();
    }

    /**
     * 获取布局资源Id
     */
    @LayoutRes
    protected abstract int getLayoutResId();

    /**
     * 初始化
     *
     * @param decorView PopupWindow 自定义顶级 View
     */
    protected abstract void initView(View decorView);

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        view = LayoutInflater.from(context).inflate(getLayoutResId(), null);
        // 导入布局
        setContentView(view);
        // 设置动画效果
        setAnimationStyle(R.style.FilterPopupAnima);
        // 防止虚拟键挡住
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // 设置弹出窗体的 宽，高
        setHeight(App.HEIGHT * 2 / 3);
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        // 设置可触
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        setBackgroundDrawable(dw);
        // 单击弹出窗以外处 关闭弹出窗
        view.setOnTouchListener((v, event) -> {
            int height = view.getTop();
            int y = (int) event.getY();
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (y > height) {
                    dismiss();
                    if (mAlphaEnable)
                        setWindowAlpha(false);
                    return true;
                }
            }
            return false;
        });

        initView(view);
    }

//    protected void requestFullScreen() {
//        Activity activity = (Activity) context;
//        int h = activity.getWindowManager().getDefaultDisplay().getHeight();
//        int w = activity.getWindowManager().getDefaultDisplay().getWidth();
//        // 设置SelectPicPopupWindow的View
////        setContentView(conentView);
//        // 设置SelectPicPopupWindow弹出窗体的宽
////        setWidth((int) activity.getResources().getDimension(R.dimen.px_to_dp_232));
//        // 设置SelectPicPopupWindow弹出窗体的高
////        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        // 设置SelectPicPopupWindow弹出窗体可点击
////        setFocusable(true);
////        setOutsideTouchable(true);
//        // 刷新状态
//        update();
//        // 实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0000000000);
//        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
//        this.setBackgroundDrawable(dw);
//    }

    /**
     * 动态设置Activity背景透明度
     */
    public void setWindowAlpha(boolean isopen) {
        if (!mAlphaEnable)
            return;

        final Window window = ((Activity) context).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ValueAnimator animator;
        if (isopen) {
            animator = ValueAnimator.ofFloat(1.0f, 0.5f);
        } else {
            animator = ValueAnimator.ofFloat(0.5f, 1.0f);
        }
        animator.setDuration(400);
        animator.addUpdateListener(animation -> {
            lp.alpha = (float) animation.getAnimatedValue();
            window.setAttributes(lp);
        });
        animator.start();
    }
}
