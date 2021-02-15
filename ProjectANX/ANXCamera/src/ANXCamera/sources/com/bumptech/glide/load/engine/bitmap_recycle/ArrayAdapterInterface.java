package com.bumptech.glide.load.engine.bitmap_recycle;

interface ArrayAdapterInterface {
    int getArrayLength(Object obj);

    int getElementSizeInBytes();

    String getTag();

    Object newArray(int i);
}
