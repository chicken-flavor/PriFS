package pri.ayyj.fuxk.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.HTTP;
import retrofit2.http.Url;

public interface IGet {

    @HTTP(method = "GET")
    Call<ResponseBody> getCall(@Url String url);
}