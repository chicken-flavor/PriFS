package pri.ayyj.fuxk.net;

import android.support.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public interface OnResponse<T> {

    /**
     * @param t        接受实体
     * @param success  是否成功
     * @param error    失败时的错误信息
     * @param tr       失败时的异常信息
     */
    void response(T t, boolean success, String error, @Nullable Throwable tr);
}