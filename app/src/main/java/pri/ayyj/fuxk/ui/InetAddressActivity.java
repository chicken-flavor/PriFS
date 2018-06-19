package pri.ayyj.fuxk.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.base.AbsRecyclerAdapter;
import pri.ayyj.fuxk.base.ActivityBase;
import pri.ayyj.fuxk.util.SettingSpf;
import pri.ayyj.fuxk.base.TextWatcherAdapter;
import pri.ayyj.fuxk.util.Utils;
import pri.ayyj.fuxk.net.NetImpl;

/**
 * Created by yangyongjun on 2018/4/17 0017.
 * <p>
 * Address Activity
 */

@SuppressWarnings({"WeakerAccess", "unused", "FieldCanBeLocal"})
public class InetAddressActivity extends ActivityBase {

    @Bind(R.id.toolbar_left_tv)
    TextView toolbarLeftTv;
    @Bind(R.id.toolbar_title_tv)
    TextView toolbarTitleTv;
    @Bind(R.id.toolbar_right_tv)
    TextView toolbarRightTv;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.url_tv)
    TextView urlTv;

    @Bind(R.id.protocol_title_tv)
    TextView protocolTitleTv;
    @Bind(R.id.protocol_tv)
    TextView protocolTv;
    @Bind(R.id.protocol_iv)
    ImageView protocolIv;
    @Bind(R.id.line_protocol)
    LinearLayout lineProtocol;

    @Bind(R.id.domain_title_tv)
    TextView domainTitleTv;
    @Bind(R.id.domain_tv)
    TextView domainTv;
    @Bind(R.id.domain_iv)
    ImageView domainIv;

    @Bind(R.id.line_domain)
    LinearLayout lineDomain;
    @Bind(R.id.ip_tv)
    TextView ipTv;
    @Bind(R.id.ip_title_tv)
    TextView ipTitleTv;
    @Bind(R.id.line_ip)
    LinearLayout lineIp;

    @Bind(R.id.domain_list)
    RecyclerView domainList;

    @Bind(R.id.port_title_tv)
    TextView portTitleTv;
    @Bind(R.id.port_tv)
    TextView portTv;
    @Bind(R.id.line_port)
    LinearLayout linePort;

    @Bind(R.id.virtual_dir_title_tv)
    TextView virtualDirTitleTv;
    @Bind(R.id.virtual_dir_tv)
    TextView virtualDirTv;
    @Bind(R.id.virtual_dir_checkbox)
    CheckBox virtualDirCheckbox;
    @Bind(R.id.line_virtual_dir)
    LinearLayout lineVirtualDir;

    /**
     * URL
     */
    private String url;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 域名
     */
    private String domain;
    /**
     * IP
     */
    private String ip;
    /**
     * 端口
     */
    private String port;
    /**
     * url使用，ip或domain
     */
    private String useType;
    /**
     * 虚拟目录
     */
    private String virtualDir;
    /**
     * 是否使用虚拟目录
     */
    private String useVirsualDir;


    private RecyclerAdapter mAdapter;
    private List<String> domainDatas = new ArrayList<>();

    @Override
    protected int getResId() {
        return R.layout.activity_address;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        toolbarLeftTv.setText("地址设置");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbarRightTv.setText("保存");
        toolbarRightTv.setOnClickListener(v -> save());

        url = SettingSpf.get(SettingSpf.URL);
        protocol = SettingSpf.get(SettingSpf.PROTOCOL);
        domain = SettingSpf.get(SettingSpf.DOMAIN);
        ip = SettingSpf.get(SettingSpf.IP);
        port = SettingSpf.get(SettingSpf.PORT);
        useType = SettingSpf.get(SettingSpf.USE_TPYE);
        virtualDir = SettingSpf.get(SettingSpf.VIRTUAl_DIR);
        useVirsualDir = SettingSpf.get(SettingSpf.USE_VIRTUAl_DIR);

        urlTv.setText(url);
        protocolTv.setText(protocol);
        domainTv.setText(useType);
        ipTv.setText(ip);
        portTv.setText(port);
        virtualDirTv.setText(virtualDir);
        if (useType.equals("ip")) {
            lineIp.setVisibility(View.VISIBLE);
            linePort.setVisibility(View.VISIBLE);
            domainList.setVisibility(View.GONE);
        } else if (useType.equals("域名")) {
            lineIp.setVisibility(View.GONE);
            linePort.setVisibility(View.GONE);
            domainList.setVisibility(View.VISIBLE);
        }
        virtualDirCheckbox.setChecked(useVirsualDir.equals("y"));

        domainDatas.addAll(Arrays.asList(domain.split("\\.")));

        mAdapter = new RecyclerAdapter(this, domainDatas);
        domainList.setAdapter(mAdapter);
        domainList.setLayoutManager(new LinearLayoutManager(this));

        List<String> protocolList = Arrays.asList(getResources().getStringArray(R.array.protocol));
        lineProtocol.setOnClickListener(v -> createSingleDialog(R.array.protocol, protocolList.indexOf(protocol), pointer -> {
            protocol = protocolList.get(pointer);
            protocolTv.setText(protocol);
            formatUrl();
        }));

        List<String> useTypeList = Arrays.asList(getResources().getStringArray(R.array.domain));
        lineDomain.setOnClickListener(v -> createSingleDialog(R.array.domain, useTypeList.indexOf(useType), pointer -> {
            useType = useTypeList.get(pointer);
            if (useType.equals("ip")) {
                lineIp.setVisibility(View.VISIBLE);
                linePort.setVisibility(View.VISIBLE);
                domainList.setVisibility(View.GONE);
            } else if (useType.equals("域名")) {
                lineIp.setVisibility(View.GONE);
                linePort.setVisibility(View.GONE);
                domainList.setVisibility(View.VISIBLE);
            }
            domainTv.setText(useType);
            formatUrl();
        }));

        lineIp.setOnClickListener(v -> showIP());
        linePort.setOnClickListener(v -> showPort());
        lineVirtualDir.setOnClickListener(v -> {
            if (virtualDirCheckbox.isChecked()) {
                showVirtualDir();
            } else {
                virtualDirCheckbox.performClick();
            }
        });
        virtualDirCheckbox.setOnClickListener(v -> {
            useVirsualDir = virtualDirCheckbox.isChecked() ? "y" : "n";
            formatUrl();
        });
    }

    /**
     * 保存信息
     */
    private void save() {
        SettingSpf.save(SettingSpf.URL, url);
        SettingSpf.save(SettingSpf.PROTOCOL, protocol);
        SettingSpf.save(SettingSpf.DOMAIN, domain);
        SettingSpf.save(SettingSpf.IP, ip);
        SettingSpf.save(SettingSpf.PORT, port);
        SettingSpf.save(SettingSpf.USE_TPYE, useType);
        SettingSpf.save(SettingSpf.VIRTUAl_DIR, virtualDir);
        SettingSpf.save(SettingSpf.USE_VIRTUAl_DIR, useVirsualDir);

        NetImpl.get().setBaseUrl(url);
        Utils.toast("已保存");
        finish();
    }

    private void showIP() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_input, null);
        EditText et = view.findViewById(R.id.et);
        et.setText(ip);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setView(view)
                .setMessage("设置IP")
                .setPositiveButton("确定", (dialog1, which) -> {
                    String text = et.getText().toString();
                    if (text.split("\\.").length != 4)
                        return;

                    ip = text;
                    ipTv.setText(ip);
                    formatUrl();
                }).create();
        dialog.show();
    }

    private void showPort() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_input, null);
        EditText et = view.findViewById(R.id.et);
        et.setText(port);
        et.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setView(view)
                .setMessage("设置端口")
                .setPositiveButton("确定", (dialog1, which) -> {
                    port = et.getText().toString();
                    portTv.setText(port);
                    formatUrl();
                }).create();
        dialog.show();
    }

    private void showVirtualDir() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_input, null);
        EditText et = view.findViewById(R.id.et);
        et.setText(virtualDir);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setView(view)
                .setMessage("设置VirtualDir")
                .setPositiveButton("确定", (dialog1, which) -> {
                    virtualDir = et.getText().toString();
                    virtualDirTv.setText(virtualDir);
                    formatUrl();
                }).create();
        dialog.show();
    }

    /**
     * 创建单选弹窗
     */
    private void createSingleDialog(@ArrayRes int itemsId, int checkedItem, @NonNull OnSelectListener onSelectListener) {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(itemsId, checkedItem, (dialog, which) -> {
                    dialog.dismiss();
                    onSelectListener.select(which);
                })
                .show();
    }

    private void formatUrl() {
        domain = formatDomain();
        url = protocol + "://" +
                (useType.equals("ip") ? ip + ":" + port : domain) + "/" +
                (useVirsualDir.equals("y") ? virtualDir + "/" : "");

        urlTv.setText(url);
    }

    private String formatDomain() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < domainDatas.size(); i++) {
            if (i == domainDatas.size() - 1)
                sb.append(domainDatas.get(i));
            else sb.append(domainDatas.get(i)).append(".");
        }
        return sb.toString();
    }

    private class RecyclerAdapter extends AbsRecyclerAdapter<String, VHolder> {

        public RecyclerAdapter(Context context, List<String> datas) {
            super(context, datas);
        }

        @Override
        protected VHolder createViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.item_activity_address, null);
            return new VHolder(view);
        }

        @Override
        protected void bind(VHolder holder, String data, int position) {
            holder.et.setText(data);

            holder.et.addTextChangedListener(new TextWatcherAdapter() {
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    domainDatas.set(position, charSequence.toString());
                    formatUrl();
                }
            });

            holder.addTv.setOnClickListener(view -> {
                domainDatas.add(position + 1, "");
                mAdapter.notifyDataSetChanged();
                formatUrl();
            });

            holder.deleteTv.setOnClickListener(view -> {
                domainDatas.remove(position);
                mAdapter.notifyDataSetChanged();
                formatUrl();
            });
        }
    }

    static class VHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.et)
        EditText et;
        @Bind(R.id.add_tv)
        TextView addTv;
        @Bind(R.id.delete_tv)
        TextView deleteTv;
        @Bind(R.id.devider)
        View devider;

        public VHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnSelectListener {
        void select(int pointer);
    }
}
