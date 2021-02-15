package com.ss.android.ugc.effectmanager.common.listener;

import java.io.InputStream;

public interface IJsonConverter {
    Object convertJsonToObj(InputStream inputStream, Class cls);

    String convertObjToJson(Object obj);
}
