package pri.ayyj.fuxk.ui;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.base.AbsRecyclerAdapter;

/**
 * Created by yangyongjun on 2018/4/11 0011.
 * <p>
 * 选择框
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class SelectPopWin extends AbsPopWin {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    protected AbsRecyclerAdapter<String, VHolder> adapter;

    protected OnSelectListener onSelectListener;

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public SelectPopWin(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.popwin_select;
    }

    @Override
    protected void initView(View decorView) {
        ButterKnife.bind(this, decorView);

        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.FilterPopupAnima);
        view.setOnTouchListener(null);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter = new AbsRecyclerAdapter<String, VHolder>(context, new ArrayList<>()) {
            @Override
            protected VHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
                View view = inflater.inflate(R.layout.item_popwin_select, null);
                return new VHolder(view);
            }

            @Override
            protected void bind(VHolder holder, String data, int position) {
                holder.tv.setText(data);
                holder.tv.setOnClickListener(v -> {
                    if (onSelectListener != null)
                        onSelectListener.select(position, data);
                });
            }
        });
    }

    static class VHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv)
        TextView tv;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setDatas(List<String> datas) {
        adapter.getDatas().clear();
        adapter.getDatas().addAll(datas);
        adapter.notifyDataSetChanged();
    }

    public interface OnSelectListener {
        void select(int pointer, String item);
    }
}
