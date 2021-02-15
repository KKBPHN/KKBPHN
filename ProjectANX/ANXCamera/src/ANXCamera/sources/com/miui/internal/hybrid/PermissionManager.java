package com.miui.internal.hybrid;

import android.net.Uri;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PermissionManager {
    private Config mConfig;
    private Map mValidMap = new HashMap();

    public PermissionManager(Config config) {
        this.mConfig = config;
    }

    private boolean initPermission(String str) {
        Uri parse = Uri.parse(str);
        String str2 = "*";
        String host = ComposerHelper.COMPOSER_PATH.equals(parse.getScheme()) ? str2 : parse.getHost();
        boolean z = false;
        for (Entry value : this.mConfig.getPermissions().entrySet()) {
            Permission permission = (Permission) value.getValue();
            String uri = permission.getUri();
            String host2 = str2.equals(uri) ? str2 : Uri.parse(uri).getHost();
            if (permission.isApplySubdomains()) {
                String str3 = "\\.";
                String[] split = host2.split(str3);
                String[] split2 = host.split(str3);
                if (split2.length >= split.length) {
                    int i = 1;
                    while (true) {
                        if (i > split.length) {
                            z = true;
                            continue;
                            break;
                        } else if (!split2[split2.length - i].equals(split[split.length - i])) {
                            z = false;
                            continue;
                            break;
                        } else {
                            i++;
                        }
                    }
                } else {
                    continue;
                }
            } else {
                z = host.equals(host2);
                continue;
            }
            if (z) {
                break;
            }
        }
        return z;
    }

    public boolean isValid(String str) {
        if (!this.mValidMap.containsKey(str)) {
            this.mValidMap.put(str, Boolean.valueOf(initPermission(str)));
        }
        return ((Boolean) this.mValidMap.get(str)).booleanValue();
    }
}
