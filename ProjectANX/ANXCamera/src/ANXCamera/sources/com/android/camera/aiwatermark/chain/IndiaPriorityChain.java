package com.android.camera.aiwatermark.chain;

import android.content.Context;
import com.android.camera.aiwatermark.handler.AbstractHandler;
import com.android.camera.aiwatermark.handler.IndiaASDHandler;
import com.android.camera.aiwatermark.handler.IndiaScenicSpotsHandler;
import com.android.camera.aiwatermark.handler.IndiaTimeHandler;

public class IndiaPriorityChain extends AbstractPriorityChain {
    public AbstractHandler createASDChain(Context context) {
        return new IndiaTimeHandler(true);
    }

    public AbstractHandler createChain(Context context) {
        IndiaScenicSpotsHandler indiaScenicSpotsHandler = new IndiaScenicSpotsHandler(true);
        IndiaASDHandler indiaASDHandler = new IndiaASDHandler(true, context);
        IndiaTimeHandler indiaTimeHandler = new IndiaTimeHandler(true);
        indiaScenicSpotsHandler.setNextHandler(indiaASDHandler);
        indiaASDHandler.setNextHandler(indiaTimeHandler);
        return indiaScenicSpotsHandler;
    }
}
