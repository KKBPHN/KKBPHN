package org.greenrobot.greendao.converter;

public interface PropertyConverter {
    Object convertToDatabaseValue(Object obj);

    Object convertToEntityProperty(Object obj);
}
