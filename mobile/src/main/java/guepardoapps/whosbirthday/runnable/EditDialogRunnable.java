package guepardoapps.whosbirthday.runnable;

import android.support.annotation.NonNull;

public class EditDialogRunnable implements Runnable {
    private static final String TAG = EditDialogRunnable.class.getSimpleName();

    protected String[] _data = new String[]{};

    public void SetData(@NonNull String[] data) {
        _data = data;
    }

    public String[] GetData() {
        return _data;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}
