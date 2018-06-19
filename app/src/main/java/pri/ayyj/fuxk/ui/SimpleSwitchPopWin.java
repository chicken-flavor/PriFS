package pri.ayyj.fuxk.ui;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;

import butterknife.ButterKnife;
import pri.ayyj.fuxk.App;
import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.base.AbsRecyclerAdapter;

/**
 * Created by yangyongjun on 2018/5/4 0004.
 * <p>
 * 底部滑出导航分发 弹窗
 */

public class SimpleSwitchPopWin extends SelectPopWin {

    public SimpleSwitchPopWin(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.popwin_simple_switch;
    }

    @Override
    protected void initView(View decorView) {
        ButterKnife.bind(this, decorView);

        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.SwitchPopupAnima);
        view.setOnTouchListener(null);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter = new AbsRecyclerAdapter<String, VHolder>(context, new ArrayList<>()) {

            @Override
            protected VHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.item_popwin_simple_switch, null);
                return new VHolder(view);
            }

            @Override
            protected void bind(VHolder holder, String data, int position) {
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) holder.tv.getLayoutParams();
                p.width = App.WIDTH;
                holder.tv.setLayoutParams(p);

                holder.tv.setText(data);
                holder.tv.setOnClickListener(v -> {
                    if (onSelectListener != null)
                        onSelectListener.select(position, data);
                });
            }
        });
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
