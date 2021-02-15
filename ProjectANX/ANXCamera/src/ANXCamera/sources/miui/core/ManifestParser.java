package miui.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import com.miui.internal.util.ResourcesUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import miui.R;
import miui.module.Dependency;
import miui.module.Dependency.Level;
import miui.module.Module;
import org.xmlpull.v1.XmlPullParserException;

public class ManifestParser {
    private static final String ELEMENT_DEPENDENCIES = "dependencies";
    private static final String ELEMENT_DEPENDENCY = "dependency";
    private static final String ELEMENT_MANIFEST = "manifest";
    private static final String ELEMENT_MODULE = "module";
    private static final String ELEMENT_MODULES = "modules";
    private static final String ELEMENT_USES_SDK = "uses-sdk";
    private static final String MANIFEST_FILE_NAME = "miui_manifest";
    private static final String META_DATA_KEY = "com.miui.sdk.manifest";
    private static final Map PACKAGE_RESOURCE_MAP = new HashMap(3);
    private XmlResourceParser mParser;
    private Resources mResources;

    static {
        PACKAGE_RESOURCE_MAP.put("com.miui.core", "miui");
        PACKAGE_RESOURCE_MAP.put("com.miui.system", "miui.system");
        PACKAGE_RESOURCE_MAP.put("com.miui.rom", "android.miui");
    }

    private ManifestParser(Resources resources, XmlResourceParser xmlResourceParser) {
        this.mResources = resources;
        this.mParser = xmlResourceParser;
    }

    private Manifest buildCompleteManifest(Manifest manifest) {
        if (manifest.getModule() == null) {
            manifest.setModule(new Module());
        }
        if (manifest.getSdkDependency() == null) {
            Level level = new Level();
            level.setMinLevel(getDefaultMinLevel(level));
            level.setTargetLevel(getDefaultTargetLevel(level));
            level.setMaxLevel(getDefaultMaxLevel(level));
            manifest.setSdkDependency(buildCompleteSdkDependency(level));
        }
        return manifest;
    }

    private Dependency buildCompleteSdkDependency(Level level) {
        Dependency dependency = new Dependency();
        dependency.setName("com.miui.core");
        dependency.setType(2);
        dependency.setLevel(level);
        return dependency;
    }

    public static ManifestParser create(Context context) {
        Bundle bundle;
        try {
            bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            bundle = null;
        }
        return createFromResources(context.getResources(), context.getPackageName(), bundle);
    }

    public static ManifestParser createFromArchive(PackageManager packageManager, String str) {
        ApplicationInfo applicationInfo = packageManager.getPackageArchiveInfo(str, 128).applicationInfo;
        return createFromResources(ResourcesUtils.createResources(str), applicationInfo.packageName, applicationInfo.metaData);
    }

    public static ManifestParser createFromPackage(PackageManager packageManager, String str) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 128);
            return createFromResources(ResourcesUtils.createResources(applicationInfo.sourceDir), str, applicationInfo.metaData);
        } catch (NameNotFoundException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("cannot find package ");
            sb.append(str);
            Log.w("miuisdk", sb.toString());
            return createFromXmlParser(null, null);
        }
    }

    public static ManifestParser createFromResId(Context context, int i) {
        Resources resources = context.getResources();
        return createFromXmlParser(resources, i == 0 ? null : resources.getXml(i));
    }

    public static ManifestParser createFromResources(Resources resources, String str, Bundle bundle) {
        int i = bundle != null ? bundle.getInt(META_DATA_KEY) : 0;
        if (i == 0) {
            String str2 = (String) PACKAGE_RESOURCE_MAP.get(str);
            if (str2 != null) {
                str = str2;
            }
            i = resources.getIdentifier(MANIFEST_FILE_NAME, "xml", str);
        }
        return createFromXmlParser(resources, i == 0 ? null : resources.getXml(i));
    }

    public static ManifestParser createFromXmlParser(Resources resources, XmlResourceParser xmlResourceParser) {
        return new ManifestParser(resources, xmlResourceParser);
    }

    private int getDefaultMaxLevel(Level level) {
        return Integer.MAX_VALUE;
    }

    private int getDefaultMinLevel(Level level) {
        return 1;
    }

    private int getDefaultTargetLevel(Level level) {
        return level.getMinLevel();
    }

    private void parseDependenciesElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
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
                    if (ELEMENT_DEPENDENCY.equals(xmlResourceParser.getName())) {
                        parseDependencyElement(manifest, resources, xmlResourceParser);
                    }
                }
            }
        }
    }

    private void parseDependencyElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.MiuiManifestModule);
        String string = obtainAttributes.getString(R.styleable.MiuiManifestModule_name);
        Dependency dependency = new Dependency();
        dependency.setName(string);
        dependency.setType(2);
        Level level = new Level();
        level.setMinLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_minLevel, getDefaultMinLevel(level)));
        level.setTargetLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_targetLevel, getDefaultTargetLevel(level)));
        level.setMaxLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_maxLevel, getDefaultMaxLevel(level)));
        dependency.setLevel(level);
        manifest.addDependency(string, dependency);
        obtainAttributes.recycle();
    }

    private void parseManifestElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.MiuiManifest);
        Module module = new Module();
        module.setName(obtainAttributes.getString(R.styleable.MiuiManifest_name));
        module.setLevel(obtainAttributes.getInteger(R.styleable.MiuiManifest_level, 0));
        module.setContent(obtainAttributes.getInteger(R.styleable.MiuiManifest_moduleContent, 0));
        manifest.setModule(module);
        obtainAttributes.recycle();
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
                    if (ELEMENT_USES_SDK.equals(name)) {
                        parseUsesSdkElement(manifest, resources, xmlResourceParser);
                    } else if (ELEMENT_MODULES.equals(name)) {
                        parseModulesElement(manifest, resources, xmlResourceParser);
                    } else if (ELEMENT_DEPENDENCIES.equals(name)) {
                        parseDependenciesElement(manifest, resources, xmlResourceParser);
                    }
                }
            }
        }
    }

    private void parseModuleElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.MiuiManifestModule);
        String string = obtainAttributes.getString(R.styleable.MiuiManifestModule_name);
        Dependency dependency = new Dependency();
        dependency.setName(string);
        dependency.setType(obtainAttributes.getInt(R.styleable.MiuiManifestModule_dependencyType, 0));
        Level level = new Level();
        level.setMinLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_minLevel, getDefaultMinLevel(level)));
        level.setTargetLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_targetLevel, getDefaultTargetLevel(level)));
        level.setMaxLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestModule_maxLevel, getDefaultMaxLevel(level)));
        dependency.setLevel(level);
        manifest.addDependency(string, dependency);
        obtainAttributes.recycle();
    }

    private void parseModulesElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
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
                    if (ELEMENT_MODULE.equals(xmlResourceParser.getName())) {
                        parseModuleElement(manifest, resources, xmlResourceParser);
                    }
                }
            }
        }
    }

    private void parseUsesSdkElement(Manifest manifest, Resources resources, XmlResourceParser xmlResourceParser) {
        TypedArray obtainAttributes = resources.obtainAttributes(xmlResourceParser, R.styleable.MiuiManifestUsesSdk);
        Level level = new Level();
        level.setMinLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestUsesSdk_minLevel, getDefaultMinLevel(level)));
        level.setTargetLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestUsesSdk_targetLevel, getDefaultTargetLevel(level)));
        level.setMaxLevel(obtainAttributes.getInteger(R.styleable.MiuiManifestUsesSdk_maxLevel, getDefaultMaxLevel(level)));
        manifest.setSdkDependency(buildCompleteSdkDependency(level));
        obtainAttributes.recycle();
    }

    public Manifest parse(Map map) {
        if (map == null) {
            new HashMap();
        }
        Manifest manifest = new Manifest();
        XmlResourceParser xmlResourceParser = this.mParser;
        if (xmlResourceParser != null) {
            try {
                Resources resources = this.mResources;
                while (true) {
                    int next = xmlResourceParser.next();
                    if (next == 2 || next == 1) {
                    }
                }
                if (ELEMENT_MANIFEST.equals(xmlResourceParser.getName())) {
                    parseManifestElement(manifest, resources, xmlResourceParser);
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (Throwable th) {
                this.mParser.close();
                throw th;
            }
            this.mParser.close();
        }
        buildCompleteManifest(manifest);
        return manifest;
    }
}
