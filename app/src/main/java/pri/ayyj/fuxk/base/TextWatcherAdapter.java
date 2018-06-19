package pri.ayyj.fuxk.base;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by yangyongjun on 2018/3/19 0019.
 * <p>
 * TextWatcher 适配器
 */

public abstract class TextWatcherAdapter implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
