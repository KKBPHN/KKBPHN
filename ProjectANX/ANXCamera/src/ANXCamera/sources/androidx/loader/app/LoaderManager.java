package androidx.loader.app;

import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.content.Loader;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public abstract class LoaderManager {

    public interface LoaderCallbacks {
        @MainThread
        @NonNull
        Loader onCreateLoader(int i, @Nullable Bundle bundle);

        @MainThread
        void onLoadFinished(@NonNull Loader loader, Object obj);

        @MainThread
        void onLoaderReset(@NonNull Loader loader);
    }

    public static void enableDebugLogging(boolean z) {
        LoaderManagerImpl.DEBUG = z;
    }

    @NonNull
    public static LoaderManager getInstance(@NonNull LifecycleOwner lifecycleOwner) {
        return new LoaderManagerImpl(lifecycleOwner, ((ViewModelStoreOwner) lifecycleOwner).getViewModelStore());
    }

    @MainThread
    public abstract void destroyLoader(int i);

    @Deprecated
    public abstract void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    @Nullable
    public abstract Loader getLoader(int i);

    public boolean hasRunningLoaders() {
        return false;
    }

    @MainThread
    @NonNull
    public abstract Loader initLoader(int i, @Nullable Bundle bundle, @NonNull LoaderCallbacks loaderCallbacks);

    public abstract void markForRedelivery();

    @MainThread
    @NonNull
    public abstract Loader restartLoader(int i, @Nullable Bundle bundle, @NonNull LoaderCallbacks loaderCallbacks);
}
