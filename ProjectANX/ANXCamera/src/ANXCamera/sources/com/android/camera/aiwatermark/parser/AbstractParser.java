package com.android.camera.aiwatermark.parser;

import com.android.camera.aiwatermark.data.WatermarkItem;
import java.util.ArrayList;

public abstract class AbstractParser {
    private static final String TAG = "AbstractParser";
    protected WatermarkItem markItem = null;
    protected ArrayList watermarkItems = new ArrayList();

    public ArrayList parseByPattern(int i) {
        new ArrayList();
        return i != 1 ? parseXml() : parseJson();
    }

    public abstract ArrayList parseJson();

    public abstract ArrayList parseXml();
}
