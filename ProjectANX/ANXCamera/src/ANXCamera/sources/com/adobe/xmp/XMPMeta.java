package com.adobe.xmp;

import com.adobe.xmp.options.IteratorOptions;
import com.adobe.xmp.options.ParseOptions;
import com.adobe.xmp.options.PropertyOptions;
import com.adobe.xmp.properties.XMPProperty;
import java.util.Calendar;

public interface XMPMeta extends Cloneable {
    void appendArrayItem(String str, String str2, PropertyOptions propertyOptions, String str3, PropertyOptions propertyOptions2);

    void appendArrayItem(String str, String str2, String str3);

    Object clone();

    int countArrayItems(String str, String str2);

    void deleteArrayItem(String str, String str2, int i);

    void deleteProperty(String str, String str2);

    void deleteQualifier(String str, String str2, String str3, String str4);

    void deleteStructField(String str, String str2, String str3, String str4);

    boolean doesArrayItemExist(String str, String str2, int i);

    boolean doesPropertyExist(String str, String str2);

    boolean doesQualifierExist(String str, String str2, String str3, String str4);

    boolean doesStructFieldExist(String str, String str2, String str3, String str4);

    String dumpObject();

    XMPProperty getArrayItem(String str, String str2, int i);

    XMPProperty getLocalizedText(String str, String str2, String str3, String str4);

    String getObjectName();

    String getPacketHeader();

    XMPProperty getProperty(String str, String str2);

    byte[] getPropertyBase64(String str, String str2);

    Boolean getPropertyBoolean(String str, String str2);

    Calendar getPropertyCalendar(String str, String str2);

    XMPDateTime getPropertyDate(String str, String str2);

    Double getPropertyDouble(String str, String str2);

    Integer getPropertyInteger(String str, String str2);

    Long getPropertyLong(String str, String str2);

    String getPropertyString(String str, String str2);

    XMPProperty getQualifier(String str, String str2, String str3, String str4);

    XMPProperty getStructField(String str, String str2, String str3, String str4);

    void insertArrayItem(String str, String str2, int i, String str3);

    void insertArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions);

    XMPIterator iterator();

    XMPIterator iterator(IteratorOptions iteratorOptions);

    XMPIterator iterator(String str, String str2, IteratorOptions iteratorOptions);

    void normalize(ParseOptions parseOptions);

    void setArrayItem(String str, String str2, int i, String str3);

    void setArrayItem(String str, String str2, int i, String str3, PropertyOptions propertyOptions);

    void setLocalizedText(String str, String str2, String str3, String str4, String str5);

    void setLocalizedText(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions);

    void setObjectName(String str);

    void setProperty(String str, String str2, Object obj);

    void setProperty(String str, String str2, Object obj, PropertyOptions propertyOptions);

    void setPropertyBase64(String str, String str2, byte[] bArr);

    void setPropertyBase64(String str, String str2, byte[] bArr, PropertyOptions propertyOptions);

    void setPropertyBoolean(String str, String str2, boolean z);

    void setPropertyBoolean(String str, String str2, boolean z, PropertyOptions propertyOptions);

    void setPropertyCalendar(String str, String str2, Calendar calendar);

    void setPropertyCalendar(String str, String str2, Calendar calendar, PropertyOptions propertyOptions);

    void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime);

    void setPropertyDate(String str, String str2, XMPDateTime xMPDateTime, PropertyOptions propertyOptions);

    void setPropertyDouble(String str, String str2, double d);

    void setPropertyDouble(String str, String str2, double d, PropertyOptions propertyOptions);

    void setPropertyInteger(String str, String str2, int i);

    void setPropertyInteger(String str, String str2, int i, PropertyOptions propertyOptions);

    void setPropertyLong(String str, String str2, long j);

    void setPropertyLong(String str, String str2, long j, PropertyOptions propertyOptions);

    void setQualifier(String str, String str2, String str3, String str4, String str5);

    void setQualifier(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions);

    void setStructField(String str, String str2, String str3, String str4, String str5);

    void setStructField(String str, String str2, String str3, String str4, String str5, PropertyOptions propertyOptions);

    void sort();
}
