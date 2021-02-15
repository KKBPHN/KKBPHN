package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader.LoadData;
import java.io.InputStream;

public class ResourceLoader implements ModelLoader {
    private static final String TAG = "ResourceLoader";
    private final Resources resources;
    private final ModelLoader uriLoader;

    public final class AssetFileDescriptorFactory implements ModelLoaderFactory {
        private final Resources resources;

        public AssetFileDescriptorFactory(Resources resources2) {
            this.resources = resources2;
        }

        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, multiModelLoaderFactory.build(Uri.class, AssetFileDescriptor.class));
        }

        public void teardown() {
        }
    }

    public class FileDescriptorFactory implements ModelLoaderFactory {
        private final Resources resources;

        public FileDescriptorFactory(Resources resources2) {
            this.resources = resources2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, multiModelLoaderFactory.build(Uri.class, ParcelFileDescriptor.class));
        }

        public void teardown() {
        }
    }

    public class StreamFactory implements ModelLoaderFactory {
        private final Resources resources;

        public StreamFactory(Resources resources2) {
            this.resources = resources2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, multiModelLoaderFactory.build(Uri.class, InputStream.class));
        }

        public void teardown() {
        }
    }

    public class UriFactory implements ModelLoaderFactory {
        private final Resources resources;

        public UriFactory(Resources resources2) {
            this.resources = resources2;
        }

        @NonNull
        public ModelLoader build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceLoader(this.resources, UnitModelLoader.getInstance());
        }

        public void teardown() {
        }
    }

    public ResourceLoader(Resources resources2, ModelLoader modelLoader) {
        this.resources = resources2;
        this.uriLoader = modelLoader;
    }

    @Nullable
    private Uri getResourceUri(Integer num) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("android.resource://");
            sb.append(this.resources.getResourcePackageName(num.intValue()));
            sb.append('/');
            sb.append(this.resources.getResourceTypeName(num.intValue()));
            sb.append('/');
            sb.append(this.resources.getResourceEntryName(num.intValue()));
            return Uri.parse(sb.toString());
        } catch (NotFoundException e) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Received invalid resource id: ");
                sb2.append(num);
                Log.w(str, sb2.toString(), e);
            }
            return null;
        }
    }

    public LoadData buildLoadData(@NonNull Integer num, int i, int i2, @NonNull Options options) {
        Uri resourceUri = getResourceUri(num);
        if (resourceUri == null) {
            return null;
        }
        return this.uriLoader.buildLoadData(resourceUri, i, i2, options);
    }

    public boolean handles(@NonNull Integer num) {
        return true;
    }
}
