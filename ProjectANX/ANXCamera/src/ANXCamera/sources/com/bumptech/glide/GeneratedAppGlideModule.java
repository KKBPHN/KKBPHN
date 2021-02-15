package com.bumptech.glide;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.manager.RequestManagerRetriever.RequestManagerFactory;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Set;

abstract class GeneratedAppGlideModule extends AppGlideModule {
    GeneratedAppGlideModule() {
    }

    @NonNull
    public abstract Set getExcludedModuleClasses();

    /* access modifiers changed from: 0000 */
    @Nullable
    public RequestManagerFactory getRequestManagerFactory() {
        return null;
    }
}
