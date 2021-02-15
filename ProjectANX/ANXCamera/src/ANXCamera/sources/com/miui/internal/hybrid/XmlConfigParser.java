package com.miui.internal.hybrid;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

public class XmlConfigParser implements ConfigParser {
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_ORIGIN = "origin";
    private static final String ATTRIBUTE_SRC = "src";
    private static final String ATTRIBUTE_SUBDOMAINS = "subdomains";
    private static final String ATTRIBUTE_VALUE = "value";
    private static final String CONFIG_FILE_NAME = "miui_hybrid_config";
    private static final String ELEMENT_ACCESS = "access";
    private static final String ELEMENT_CONTENT = "content";
    private static final String ELEMENT_FEATURE = "feature";
    private static final String ELEMENT_PARAM = "param";
    private static final String ELEMENT_PREFERENCE = "preference";
    private static final String ELEMENT_WIDGET = "widget";
    private static final String KEY_SIGNATURE = "signature";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_VENDOR = "vendor";
    private static final String META_DATA_KEY = "com.miui.sdk.hybrid.config";
    private XmlResourceParser mParser;

    private XmlConfigParser(XmlResourceParser xmlResourceParser) {
        this.mParser = xmlResourceParser;
    }

    private Config buildCompleteConfig(Config config, Map map) {
        return config;
    }

    public static XmlConfigParser create(Context context) {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            int i = bundle != null ? bundle.getInt(META_DATA_KEY) : 0;
            if (i == 0) {
                i = context.getResources().getIdentifier(CONFIG_FILE_NAME, "xml", context.getPackageName());
            }
            return createFromResId(context, i);
        } catch (NameNotFoundException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public static XmlConfigParser createFromResId(Context context, int i) {
        try {
            return createFromXmlParser(context.getResources().getXml(i));
        } catch (NotFoundException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public static XmlConfigParser createFromXmlParser(XmlResourceParser xmlResourceParser) {
        return new XmlConfigParser(xmlResourceParser);
    }

    private Security getSecurity(Config config) {
        Security security = config.getSecurity();
        if (security != null) {
            return security;
        }
        Security security2 = new Security();
        config.setSecurity(security2);
        return security2;
    }

    private void parseAccessElement(Config config, XmlResourceParser xmlResourceParser) {
        Permission permission = new Permission();
        permission.setUri(xmlResourceParser.getAttributeValue(null, "origin"));
        permission.setApplySubdomains(xmlResourceParser.getAttributeBooleanValue(null, ATTRIBUTE_SUBDOMAINS, false));
        permission.setForbidden(false);
        config.addPermission(permission);
    }

    private void parseContentElement(Config config, XmlResourceParser xmlResourceParser) {
        config.setContent(xmlResourceParser.getAttributeValue(null, ATTRIBUTE_SRC));
    }

    private void parseFeatureElement(Config config, XmlResourceParser xmlResourceParser) {
        Feature feature = new Feature();
        feature.setName(xmlResourceParser.getAttributeValue(null, ATTRIBUTE_NAME));
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || (next == 3 && xmlResourceParser.getDepth() <= depth)) {
                config.addFeature(feature);
            } else if (next != 3) {
                if (next != 4) {
                    if ("param".equals(xmlResourceParser.getName())) {
                        parseParamElement(feature, xmlResourceParser);
                    }
                }
            }
        }
        config.addFeature(feature);
    }

    private void parseParamElement(Feature feature, XmlResourceParser xmlResourceParser) {
        feature.setParam(xmlResourceParser.getAttributeValue(null, ATTRIBUTE_NAME).toLowerCase(), xmlResourceParser.getAttributeValue(null, "value"));
    }

    private void parsePreferenceElement(Config config, XmlResourceParser xmlResourceParser) {
        String lowerCase = xmlResourceParser.getAttributeValue(null, ATTRIBUTE_NAME).toLowerCase();
        String attributeValue = xmlResourceParser.getAttributeValue(null, "value");
        if (KEY_SIGNATURE.equals(lowerCase)) {
            getSecurity(config).setSignature(attributeValue);
        } else if (KEY_TIMESTAMP.equals(lowerCase)) {
            getSecurity(config).setTimestamp(Long.parseLong(attributeValue));
        } else if ("vendor".equals(lowerCase)) {
            config.setVendor(attributeValue);
        } else {
            config.setPreference(lowerCase, attributeValue);
        }
    }

    private void parseWidgetElement(Config config, XmlResourceParser xmlResourceParser) {
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && xmlResourceParser.getDepth() <= depth) {
                return;
            }
            if (next != 3) {
                if (next != 4) {
                    String name = xmlResourceParser.getName();
                    if ("content".equals(name)) {
                        parseContentElement(config, xmlResourceParser);
                    } else if (ELEMENT_FEATURE.equals(name)) {
                        parseFeatureElement(config, xmlResourceParser);
                    } else if (ELEMENT_PREFERENCE.equals(name)) {
                        parsePreferenceElement(config, xmlResourceParser);
                    } else if (ELEMENT_ACCESS.equals(name)) {
                        parseAccessElement(config, xmlResourceParser);
                    }
                }
            }
        }
    }

    public Config parse(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        Config config = new Config();
        XmlResourceParser xmlResourceParser = this.mParser;
        if (xmlResourceParser != null) {
            while (true) {
                try {
                    int next = xmlResourceParser.next();
                    if (next == 2 || next == 1) {
                    }
                } catch (XmlPullParserException e) {
                    throw new HybridException(201, e.getMessage());
                } catch (IOException e2) {
                    throw new HybridException(201, e2.getMessage());
                } catch (Throwable th) {
                    this.mParser.close();
                    throw th;
                }
            }
            if (ELEMENT_WIDGET.equals(xmlResourceParser.getName())) {
                parseWidgetElement(config, xmlResourceParser);
            }
            this.mParser.close();
        }
        buildCompleteConfig(config, map);
        return config;
    }
}
