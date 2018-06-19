package pri.ayyj.fuxk.net;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import pri.ayyj.fuxk.util.SettingSpf;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetImpl implements INet {

    private static final String TAG = " == NetDelegate == ";

    private static volatile NetImpl sInstance;

    //    private String mBaseUrl = "http://apis.juhe.cn/mobile/";
    private String mBaseUrl = SettingSpf.get(SettingSpf.URL);
    private Retrofit mRetrofit;

    public static NetImpl get() {
        if (sInstance == null) {
            synchronized (NetImpl.class) {
                if (sInstance == null)
                    sInstance = new NetImpl();
            }
        }
        return sInstance;
    }

    private NetImpl() {
        if (mRetrofit == null)
            initRetrofit();
    }

    private synchronized void initRetrofit() {
        mRetrofit = new Retrofit
                .Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build())
                .build();
    }

    @Override
    public void setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        mRetrofit = mRetrofit
                .newBuilder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .create()))
                .client(new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.MINUTES)
                        .build())
                .build();
    }

    @Override
    public <T> T getService(Class<T> service) {
        if (mRetrofit == null) {
            initRetrofit();
        }
        return mRetrofit.create(service);
    }
}
