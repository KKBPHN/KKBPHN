package com.iqiyi.android.qigsaw.core.extension;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitAABInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.extension.fakecomponents.FakeActivity;
import com.iqiyi.android.qigsaw.core.extension.fakecomponents.FakeReceiver;
import com.iqiyi.android.qigsaw.core.extension.fakecomponents.FakeService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public class AABExtension {
    private static final String TAG = "Split:AABExtension";
    private static final AtomicReference sAABCompatReference = new AtomicReference(null);
    private final List aabApplications = new ArrayList();
    private final AABExtensionManager extensionManager = new AABExtensionManagerImpl(new SplitComponentInfoProvider(getSplitNames()));
    private final Map sSplitContentProviderMap = new HashMap();

    private AABExtension() {
    }

    public static AABExtension getInstance() {
        if (sAABCompatReference.get() == null) {
            sAABCompatReference.set(new AABExtension());
        }
        return (AABExtension) sAABCompatReference.get();
    }

    private Set getSplitNames() {
        String[] dynamicFeatures = SplitBaseInfoProvider.getDynamicFeatures();
        HashSet hashSet = new HashSet();
        if (dynamicFeatures != null && dynamicFeatures.length > 0) {
            hashSet.addAll(Arrays.asList(dynamicFeatures));
        }
        return hashSet;
    }

    public void activateSplitProviders(ClassLoader classLoader, String str) {
        List<ContentProviderProxy> list = (List) this.sSplitContentProviderMap.get(str);
        if (list != null) {
            for (ContentProviderProxy activateRealContentProvider : list) {
                activateRealContentProvider.activateRealContentProvider(classLoader);
            }
        }
    }

    public void activeApplication(Application application, Context context) {
        this.extensionManager.activeApplication(application, context);
    }

    public void createAndActiveSplitApplication(Context context, boolean z) {
        if (!z) {
            Set<String> installedSplitsForAAB = new SplitAABInfoProvider(context).getInstalledSplitsForAAB();
            if (!installedSplitsForAAB.isEmpty()) {
                for (String str : installedSplitsForAAB) {
                    try {
                        Application createApplication = createApplication(AABExtension.class.getClassLoader(), str);
                        if (createApplication != null) {
                            activeApplication(createApplication, context);
                            this.aabApplications.add(createApplication);
                        }
                    } catch (AABExtensionException e) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to create ");
                        sb.append(str);
                        sb.append(" application");
                        SplitLog.w(TAG, sb.toString(), (Throwable) e);
                    }
                }
            }
        }
    }

    public Application createApplication(ClassLoader classLoader, String str) {
        return this.extensionManager.createApplication(classLoader, str);
    }

    public Class getFakeComponent(String str) {
        if (this.extensionManager.isSplitActivity(str)) {
            return FakeActivity.class;
        }
        if (this.extensionManager.isSplitService(str)) {
            return FakeService.class;
        }
        if (this.extensionManager.isSplitReceiver(str)) {
            return FakeReceiver.class;
        }
        return null;
    }

    public String getSplitNameForActivityName(@NonNull String str) {
        for (Entry entry : this.extensionManager.getSplitActivitiesMap().entrySet()) {
            String str2 = (String) entry.getKey();
            List list = (List) entry.getValue();
            if (list != null && list.contains(str)) {
                return str2;
            }
        }
        return null;
    }

    public void onApplicationCreate() {
        if (!this.aabApplications.isEmpty()) {
            for (Application onCreate : this.aabApplications) {
                onCreate.onCreate();
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void put(String str, ContentProviderProxy contentProviderProxy) {
        List list = (List) this.sSplitContentProviderMap.get(str);
        if (list == null) {
            list = new ArrayList();
            this.sSplitContentProviderMap.put(str, list);
        }
        list.add(contentProviderProxy);
    }
}
