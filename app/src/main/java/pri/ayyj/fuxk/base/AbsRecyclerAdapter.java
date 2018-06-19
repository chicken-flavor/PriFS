package pri.ayyj.fuxk.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yangyongjun on 2018/3/29 0029.
 * <p>
 * Abs Adapter
 *
 * @param <T>  实体
 * @param <VH> ViewHolder
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AbsRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private LayoutInflater mInflater;
    private List<T> mDatas;

    public AbsRecyclerAdapter(Context context, List<T> datas) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
    }

    public List<T> getDatas() {
        return mDatas;
    }

    protected abstract VH createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    protected abstract void bind(VH holder, T data, int position);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder(mInflater, parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bind(holder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}
