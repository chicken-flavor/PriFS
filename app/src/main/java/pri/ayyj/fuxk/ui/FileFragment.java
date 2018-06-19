package pri.ayyj.fuxk.ui;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import pri.ayyj.fuxk.MainActivity;
import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.base.AbsRecyclerAdapter;
import pri.ayyj.fuxk.base.FragmentBase;
import pri.ayyj.fuxk.bean.SinglePath;
import pri.ayyj.fuxk.bean.SinglePathModel;
import pri.ayyj.fuxk.net.IDownload;
import pri.ayyj.fuxk.net.IGet;
import pri.ayyj.fuxk.net.INet;
import pri.ayyj.fuxk.net.NetImpl;
import pri.ayyj.fuxk.util.FileUtils;
import pri.ayyj.fuxk.util.SettingSpf;
import pri.ayyj.fuxk.util.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static android.support.v7.widget.RecyclerView.VERTICAL;
import static pri.ayyj.fuxk.util.SettingSpf.URL;

public class FileFragment extends FragmentBase {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerAdapter mAdapter;
    List<SinglePath.SingleFile> mDatas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(mAdapter = new RecyclerAdapter(getContext(), mDatas));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), VERTICAL));

        requestList();
    }

    private void requestList() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            MainActivity.popFragment();
            return;
        }

        String drive = bundle.getString("drive", "");
        String path = bundle.getString("path", "");
        INet net = NetImpl.get();
        IGet service = net.getService(IGet.class);
        Call<ResponseBody> call = service.getCall("api/file/list?" + (TextUtils.isEmpty(path) ? "drive=" + drive : "path=" + path));
        net.defaultRequest(call, SinglePathModel.class, (model, success, error, tr) -> {
            if (success) {
                mDatas.clear();
                mDatas.addAll(model.getResult().getDir());
                mDatas.addAll(model.getResult().getFile());
                mAdapter.notifyDataSetChanged();
            } else {
                if (!TextUtils.isEmpty(error)) {
                    Utils.toast(error);
                    if (tr != null)
                        recyclerView.postDelayed(() -> Utils.toast(tr.getMessage()), 1000);
                } else if (tr != null)
                    Utils.toast(tr.getMessage());
                else Utils.toast("不知道通知什么，反正错了");

                MainActivity.popFragment();
            }
        });
    }

    private static class DRunnable implements Runnable {

        private ResponseBody body;
        private String path;

        DRunnable(ResponseBody uri, String path) {
            this.body = uri;
            this.path = path;
        }

        @Override
        public void run() {
            Looper.prepare();
            FileUtils.writeFile(body, path);
            Looper.loop();
            Looper looper = Looper.myLooper();
            if (looper != null)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
                    looper.quitSafely();
                else
                    looper.quit();
        }
    }

    private void requestDownload(String uri, String path) {
        INet net = NetImpl.get();
        IDownload service = net.getService(IDownload.class);
        Call<ResponseBody> call = service.download("api/file/download?path=" + uri);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    if (response.body() == null)
                        return;

                    new Thread(new DRunnable(response.body(), path)).start();
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class RecyclerAdapter extends AbsRecyclerAdapter<SinglePath.SingleFile, VHolder> {
        RecyclerAdapter(Context context, List<SinglePath.SingleFile> datas) {
            super(context, datas);
        }

        @Override
        protected VHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_fragment_file, parent, false);
            return new VHolder(view);
        }

        @Override
        protected void bind(VHolder holder, SinglePath.SingleFile data, int position) {
            if (data.getType() == 0)
                holder.iconIv.setImageResource(R.drawable.ic_dir);
            else {
                holder.line.setOnLongClickListener(v -> {
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", SettingSpf.get(URL) + "api/file/download?path=" + data.getFullName());
                    cm.setPrimaryClip(mClipData);
                    return true;
                });
                holder.iconIv.setImageResource(R.drawable.ic_file);
                holder.menuIv.setVisibility(View.VISIBLE);
            }

            holder.textTv.setText(data.getName());
            holder.line.setOnClickListener(v -> {
                if (data.getType() == 0)
                    MainActivity.addFragment(data.getFullName(), "");
                else {
                    Utils.toast(data.getFullName());
//                    recyclerView.postDelayed(() -> Utils.toast(tips[0]), 1000);
                }
            });
            holder.menuIv.setOnClickListener(v -> {
                showSwitchPopWin(data);
            });
        }
    }

    private String[] getPathByType(int type) {
        String[] tips = {"", ""};
        switch (type) {
            case 0:
                tips[0] = "文本文件";
                tips[1] = FileUtils.TEXT_PATH;
                break;
            case 1:
                tips[0] = "图片文件";
                tips[1] = FileUtils.IMAGE_PATH;
                break;
            case 2:
                tips[0] = "音频文件";
                tips[1] = FileUtils.AUDIO_PATH;
                break;
            case 3:
                tips[0] = "视频文件";
                tips[1] = FileUtils.VIDEO_PATH;
                break;
            case -1:
            default:
                tips[0] = "未知类型";
                break;
        }

        return tips;
    }

    private SimpleSwitchPopWin mSwitchPopup;

    private void showSwitchPopWin(SinglePath.SingleFile data) {
        int type = FileUtils.getType(data.getExtension());
        if (mSwitchPopup == null) {
            mSwitchPopup = new SimpleSwitchPopWin(getActivity());
            List<String> datas = new ArrayList<>();
            datas.add("下载");
            datas.add("查看详情");
            if (type == 3)
                datas.add("播放");
            mSwitchPopup.setDatas(datas);
            mSwitchPopup.setOnSelectListener((pointer, item) -> {
                mSwitchPopup.dismiss();
                String[] tips = getPathByType(type);
                recyclerView.postDelayed(() -> {
                    switch (item) {
                        case "下载":
                            if (TextUtils.isEmpty(tips[1])) {
                                Utils.toast("不支持的文件类型");
                                data.MyGad = true;
                                return;
                            }
                            File file = new File(tips[1]);
                            file.mkdirs();
                            String filePath = tips[1] + System.currentTimeMillis() + data.getExtension();
                            requestDownload(data.getFullName(), filePath);
                            break;
                        case "查看详情":
                            showDetailsDialog(data, tips[0]);
                            break;
                        case "播放":
                            Intent intent = new Intent(getContext(), VideoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("url", SettingSpf.get(URL) + "api/file/download?path=" + data.getFullName() + "&type=1");
                            startActivity(intent.putExtras(bundle));
                            break;
                        case "老子非要下":
                            new File(FileUtils.ROOT_PATH + File.separator + "gad" + File.separator).mkdirs();
                            requestDownload(data.getFullName(),
                                    FileUtils.ROOT_PATH + File.separator + "gad" + File.separator +
                                            System.currentTimeMillis() + data.getExtension());
                            break;
                    }
                }, 300);
            });
            mSwitchPopup.setOnDismissListener(() -> mSwitchPopup.setWindowAlpha(false));
        } else {
            List<String> datas = new ArrayList<>();
            datas.add("下载");
            datas.add("查看详情");
            if (type == 3)
                datas.add("播放");
            if (data.MyGad)
                datas.add("老子非要下");
            mSwitchPopup.setDatas(datas);
        }
        mSwitchPopup.show(recyclerView);
        mSwitchPopup.setWindowAlpha(true);
    }

    private void showDetailsDialog(SinglePath.SingleFile data, String type) {
        String sb = "名称： " + data.getName() + '\n' +
                "路径： " + data.getFullName() + '\n' +
                "类型： " + type + '\n' +
                "大小： " + FileUtils.getSizeFormat(data.getLength());
        new AlertDialog.Builder(getContext())
                .setMessage(sb)
                .show();
    }

    static class VHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.line)
        LinearLayout line;
        @Bind(R.id.icon_iv)
        ImageView iconIv;
        @Bind(R.id.menu_iv)
        ImageView menuIv;
        @Bind(R.id.text_tv)
        TextView textTv;

        VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            R.layout.item_fragment_file;
        }
    }
}
