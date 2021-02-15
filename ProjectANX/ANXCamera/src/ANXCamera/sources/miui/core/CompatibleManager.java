package miui.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import com.miui.internal.util.PackageConstants;
import com.miui.internal.util.ResourcesUtils;
import miui.module.Dependency;
import miui.module.Dependency.Level;

public class CompatibleManager {
    private Context mContext;
    private Manifest mManifest;
    private String mPackageName;

    public CompatibleManager(Context context, String str, String str2, Bundle bundle) {
        this.mContext = context;
        this.mManifest = parseManifest(str, str2, bundle);
        this.mPackageName = str2;
    }

    public CompatibleManager(Context context, Manifest manifest) {
        this.mContext = context;
        this.mManifest = manifest;
        this.mPackageName = manifest.getModule().getName();
    }

    private Manifest getManifest(PackageManager packageManager, String str) {
        return ManifestParser.createFromPackage(packageManager, str).parse(null);
    }

    private boolean isCompatible(Level level, int i) {
        boolean z = level.getMinLevel() <= i && i <= level.getMaxLevel();
        if (!z) {
            StringBuilder sb = new StringBuilder();
            sb.append("current is ");
            sb.append(i);
            sb.append(" but needs [");
            sb.append(level.getMinLevel());
            sb.append("-");
            sb.append(level.getMaxLevel());
            sb.append("]");
            Log.w("miuisdk", sb.toString());
        }
        return z;
    }

    private Manifest parseManifest(String str, String str2, Bundle bundle) {
        return ManifestParser.createFromResources(ResourcesUtils.createResources(str), str2, bundle).parse(null);
    }

    public int getLevel() {
        return this.mManifest.getModule().getLevel();
    }

    public boolean isCompatible() {
        StringBuilder sb;
        String str = "miuisdk";
        if (!isCompatible(this.mManifest.getSdkDependency().getLevel(), PackageConstants.CURRENT_SDK_LEVEL)) {
            sb = new StringBuilder();
            sb.append(this.mPackageName);
            sb.append(" is not compatible with sdk");
        } else {
            PackageManager packageManager = this.mContext.getPackageManager();
            if (packageManager == null) {
                Log.w(str, "package manager is not ready yet");
                return true;
            }
            for (String str2 : this.mManifest.getDependencies().keySet()) {
                Dependency dependency = this.mManifest.getDependency(str2);
                if ((dependency.getType() & 2) != 0) {
                    Manifest manifest = getManifest(packageManager, str2);
                    if (manifest == null || !isCompatible(dependency.getLevel(), manifest.getModule().getLevel())) {
                        sb = new StringBuilder();
                        sb.append(this.mPackageName);
                        sb.append(" is not compatible with module ");
                        sb.append(str2);
                    }
                }
            }
            return true;
        }
        Log.w(str, sb.toString());
        return false;
    }

    public boolean isCurrent() {
        return this.mManifest.getSdkDependency().getLevel().getTargetLevel() == PackageConstants.CURRENT_SDK_LEVEL;
    }
}
