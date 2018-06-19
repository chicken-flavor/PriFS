package pri.ayyj.fuxk.net;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pri.ayyj.fuxk.App.DEBUG;

/**
 * Created by yangyongjun on 2018/3/7 0007.
 * <p>
 * 网络操作接口，基于Retrofit
 */

@SuppressWarnings("unused")
public interface INet {
    /**
     * 错误实体
     */
    class ErrorBean {
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    /**
     * 设置base url
     *
     * @param baseUrl base url
     */
    void setBaseUrl(String baseUrl);

    /**
     * 默认请求，不基于BeanBase
     */
    default <T> void defaultRequest(Call<ResponseBody> call, Class<T> clazz, @NonNull final OnResponse<T> listener) {
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (DEBUG)
                    Log.e(clazz.getSimpleName(), "Http Result: " + response.raw().toString());

                ResponseBody body = response.body();
                ResponseBody errorBody = response.errorBody();
                int responseCode = response.code();
                String responseMsg = response.message();

                String bodyString = "";
                String errorBodyString = "";

                T t;
                try {
                    if (DEBUG)
                        Log.e(clazz.getSimpleName(), "Http Code: " + responseCode);

                    if (body != null) {
                        bodyString = new String(body.bytes());
                        if (DEBUG)
                            Log.e(clazz.getSimpleName(), "Body String: " + bodyString);

                        t = new Gson().fromJson(bodyString, clazz);
                        listener.response(t, true, "", null);
                    } else {
                        if (errorBody != null) {
                            errorBodyString = new String(errorBody.bytes());
                        }
                        if (DEBUG) {
                            Log.e(clazz.getSimpleName(), "Error Code: " + responseCode);
                            Log.e(clazz.getSimpleName(), "Error Body String: " + errorBodyString);
                        }

                        ErrorBean error = new Gson().fromJson(errorBodyString, ErrorBean.class);
                        listener.response(null, false, ((error == null || TextUtils.isEmpty(error.getError())) ? "请求出现错误" : error.getError()) + "[" + responseCode + "]", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        JSONObject obj = new JSONObject(bodyString);
                        listener.response(null, false, obj.getString("Message"), e);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                        listener.response(null, false, "数据解析失败", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @Nullable Throwable tr) {
                if (DEBUG)
                    Log.e(clazz.getSimpleName(), "Http Result: " + (tr != null ? tr.getMessage() : ""));

                listener.response(null, false, "请求出现错误", tr);
            }
        });
    }

    /**
     * 获取 Retrofit 接口代理 Sevice
     *
     * @param service 定义的Service接口
     * @param <T>     class
     * @return class instance
     */
    <T> T getService(Class<T> service);
}
