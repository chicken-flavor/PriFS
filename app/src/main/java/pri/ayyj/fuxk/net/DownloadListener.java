package pri.ayyj.fuxk.net;

public interface DownloadListener {

    void onStartDownload();

    void onProgress(int progress);

    void onFinishDownload();

    void onFail(String errorInfo);

}
