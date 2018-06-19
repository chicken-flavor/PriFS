package pri.ayyj.fuxk.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.VideoView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pri.ayyj.fuxk.R;
import pri.ayyj.fuxk.base.ActivityBase;

public class VideoActivity extends ActivityBase {

    @Bind(R.id.video_view)
    VideoView videoView;

    @Override
    protected int getResId() {
        return R.layout.activity_video;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        String url = bundle.getString("url", "");
        if (TextUtils.isEmpty(url))
            finish();

        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
    }
}
