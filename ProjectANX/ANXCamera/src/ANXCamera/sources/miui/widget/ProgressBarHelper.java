package miui.widget;

import android.widget.ProgressBar;

public class ProgressBarHelper {
    public static int getMin(ProgressBar progressBar) {
        return progressBar.getMin();
    }

    public static int getRange(ProgressBar progressBar) {
        return progressBar.getMax() - progressBar.getMin();
    }
}
