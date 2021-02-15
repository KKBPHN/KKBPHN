package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import java.io.Closeable;

interface SplitInfoVersionDataStorage extends Closeable {
    SplitInfoVersionData readVersionData();

    boolean updateVersionData(SplitInfoVersionData splitInfoVersionData);
}
