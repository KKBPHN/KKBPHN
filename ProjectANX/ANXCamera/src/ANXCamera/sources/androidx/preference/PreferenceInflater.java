package androidx.preference;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.InflateException;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

class PreferenceInflater {
    private static final HashMap CONSTRUCTOR_MAP = new HashMap();
    private static final Class[] CONSTRUCTOR_SIGNATURE = {Context.class, AttributeSet.class};
    private static final String EXTRA_TAG_NAME = "extra";
    private static final String INTENT_TAG_NAME = "intent";
    private final Object[] mConstructorArgs = new Object[2];
    private final Context mContext;
    private String[] mDefaultPackages;
    private PreferenceManager mPreferenceManager;

    public PreferenceInflater(Context context, PreferenceManager preferenceManager) {
        this.mContext = context;
        init(preferenceManager);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0078, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0079, code lost:
        r0 = new java.lang.StringBuilder();
        r0.append(r13.getPositionDescription());
        r0.append(r1);
        r0.append(r11);
        r12 = new android.view.InflateException(r0.toString());
        r12.initCause(r10);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0097, code lost:
        throw r12;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0098, code lost:
        r10 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0099, code lost:
        throw r10;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0078 A[ExcHandler: Exception (r10v5 'e' java.lang.Exception A[CUSTOM_DECLARE]), Splitter:B:2:0x000d] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private Preference createItem(@NonNull String str, @Nullable String[] strArr, AttributeSet attributeSet) {
        Class cls;
        int i;
        ClassNotFoundException e;
        Constructor constructor = (Constructor) CONSTRUCTOR_MAP.get(str);
        String str2 = ": Error inflating class ";
        if (constructor == null) {
            try {
                ClassLoader classLoader = this.mContext.getClassLoader();
                if (strArr != null) {
                    if (strArr.length != 0) {
                        int length = strArr.length;
                        cls = null;
                        i = 0;
                        e = null;
                        while (i < length) {
                            String str3 = strArr[i];
                            StringBuilder sb = new StringBuilder();
                            sb.append(str3);
                            sb.append(str);
                            cls = Class.forName(sb.toString(), false, classLoader);
                            break;
                        }
                        if (cls == null) {
                            if (e == null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(attributeSet.getPositionDescription());
                                sb2.append(str2);
                                sb2.append(str);
                                throw new InflateException(sb2.toString());
                            }
                            throw e;
                        }
                        constructor = cls.getConstructor(CONSTRUCTOR_SIGNATURE);
                        constructor.setAccessible(true);
                        CONSTRUCTOR_MAP.put(str, constructor);
                    }
                }
                cls = Class.forName(str, false, classLoader);
                constructor = cls.getConstructor(CONSTRUCTOR_SIGNATURE);
                constructor.setAccessible(true);
                CONSTRUCTOR_MAP.put(str, constructor);
            } catch (ClassNotFoundException e2) {
                e = e2;
                i++;
            } catch (Exception e3) {
            }
        }
        Object[] objArr = this.mConstructorArgs;
        objArr[1] = attributeSet;
        return (Preference) constructor.newInstance(objArr);
    }

    private Preference createItemFromTag(String str, AttributeSet attributeSet) {
        try {
            return -1 == str.indexOf(46) ? onCreateItem(str, attributeSet) : createItem(str, null, attributeSet);
        } catch (InflateException e) {
            throw e;
        } catch (ClassNotFoundException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append(attributeSet.getPositionDescription());
            sb.append(": Error inflating class (not found)");
            sb.append(str);
            InflateException inflateException = new InflateException(sb.toString());
            inflateException.initCause(e2);
            throw inflateException;
        } catch (Exception e3) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(attributeSet.getPositionDescription());
            sb2.append(": Error inflating class ");
            sb2.append(str);
            InflateException inflateException2 = new InflateException(sb2.toString());
            inflateException2.initCause(e3);
            throw inflateException2;
        }
    }

    private void init(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        StringBuilder sb = new StringBuilder();
        sb.append(Preference.class.getPackage().getName());
        String str = ".";
        sb.append(str);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(SwitchPreference.class.getPackage().getName());
        sb2.append(str);
        setDefaultPackages(new String[]{sb.toString(), sb2.toString()});
    }

    @NonNull
    private PreferenceGroup onMergeRoots(PreferenceGroup preferenceGroup, @NonNull PreferenceGroup preferenceGroup2) {
        if (preferenceGroup != null) {
            return preferenceGroup;
        }
        preferenceGroup2.onAttachedToHierarchy(this.mPreferenceManager);
        return preferenceGroup2;
    }

    private void rInflate(XmlPullParser xmlPullParser, Preference preference, AttributeSet attributeSet) {
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            if ((next == 3 && xmlPullParser.getDepth() <= depth) || next == 1) {
                return;
            }
            if (next == 2) {
                String name = xmlPullParser.getName();
                String str = "Error parsing preference";
                if (INTENT_TAG_NAME.equals(name)) {
                    try {
                        preference.setIntent(Intent.parseIntent(getContext().getResources(), xmlPullParser, attributeSet));
                    } catch (IOException e) {
                        XmlPullParserException xmlPullParserException = new XmlPullParserException(str);
                        xmlPullParserException.initCause(e);
                        throw xmlPullParserException;
                    }
                } else {
                    String str2 = EXTRA_TAG_NAME;
                    if (str2.equals(name)) {
                        getContext().getResources().parseBundleExtra(str2, attributeSet, preference.getExtras());
                        try {
                            skipCurrentTag(xmlPullParser);
                        } catch (IOException e2) {
                            XmlPullParserException xmlPullParserException2 = new XmlPullParserException(str);
                            xmlPullParserException2.initCause(e2);
                            throw xmlPullParserException2;
                        }
                    } else {
                        Preference createItemFromTag = createItemFromTag(name, attributeSet);
                        ((PreferenceGroup) preference).addItemFromInflater(createItemFromTag);
                        rInflate(xmlPullParser, createItemFromTag, attributeSet);
                    }
                }
            }
        }
    }

    private static void skipCurrentTag(XmlPullParser xmlPullParser) {
        int depth = xmlPullParser.getDepth();
        while (true) {
            int next = xmlPullParser.next();
            if (next == 1) {
                return;
            }
            if (next == 3 && xmlPullParser.getDepth() <= depth) {
                return;
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public String[] getDefaultPackages() {
        return this.mDefaultPackages;
    }

    public Preference inflate(int i, @Nullable PreferenceGroup preferenceGroup) {
        XmlResourceParser xml = getContext().getResources().getXml(i);
        try {
            return inflate((XmlPullParser) xml, preferenceGroup);
        } finally {
            xml.close();
        }
    }

    public Preference inflate(XmlPullParser xmlPullParser, @Nullable PreferenceGroup preferenceGroup) {
        int next;
        PreferenceGroup onMergeRoots;
        synchronized (this.mConstructorArgs) {
            AttributeSet asAttributeSet = Xml.asAttributeSet(xmlPullParser);
            this.mConstructorArgs[0] = this.mContext;
            do {
                try {
                    next = xmlPullParser.next();
                    if (next == 2) {
                        break;
                    }
                } catch (InflateException e) {
                    throw e;
                } catch (XmlPullParserException e2) {
                    InflateException inflateException = new InflateException(e2.getMessage());
                    inflateException.initCause(e2);
                    throw inflateException;
                } catch (IOException e3) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(xmlPullParser.getPositionDescription());
                    sb.append(": ");
                    sb.append(e3.getMessage());
                    InflateException inflateException2 = new InflateException(sb.toString());
                    inflateException2.initCause(e3);
                    throw inflateException2;
                }
            } while (next != 1);
            if (next == 2) {
                onMergeRoots = onMergeRoots(preferenceGroup, (PreferenceGroup) createItemFromTag(xmlPullParser.getName(), asAttributeSet));
                rInflate(xmlPullParser, onMergeRoots, asAttributeSet);
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(xmlPullParser.getPositionDescription());
                sb2.append(": No start tag found!");
                throw new InflateException(sb2.toString());
            }
        }
        return onMergeRoots;
    }

    /* access modifiers changed from: protected */
    public Preference onCreateItem(String str, AttributeSet attributeSet) {
        return createItem(str, this.mDefaultPackages, attributeSet);
    }

    public void setDefaultPackages(String[] strArr) {
        this.mDefaultPackages = strArr;
    }
}
