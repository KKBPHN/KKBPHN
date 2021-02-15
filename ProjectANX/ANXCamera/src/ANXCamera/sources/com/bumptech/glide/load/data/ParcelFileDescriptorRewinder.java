package com.bumptech.glide.load.data;

import android.os.Build.VERSION;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.io.IOException;

public final class ParcelFileDescriptorRewinder implements DataRewinder {
    private final InternalRewinder rewinder;

    @RequiresApi(21)
    public final class Factory implements com.bumptech.glide.load.data.DataRewinder.Factory {
        @NonNull
        public DataRewinder build(@NonNull ParcelFileDescriptor parcelFileDescriptor) {
            return new ParcelFileDescriptorRewinder(parcelFileDescriptor);
        }

        @NonNull
        public Class getDataClass() {
            return ParcelFileDescriptor.class;
        }
    }

    @RequiresApi(21)
    final class InternalRewinder {
        private final ParcelFileDescriptor parcelFileDescriptor;

        InternalRewinder(ParcelFileDescriptor parcelFileDescriptor2) {
            this.parcelFileDescriptor = parcelFileDescriptor2;
        }

        /* access modifiers changed from: 0000 */
        public ParcelFileDescriptor rewind() {
            try {
                Os.lseek(this.parcelFileDescriptor.getFileDescriptor(), 0, OsConstants.SEEK_SET);
                return this.parcelFileDescriptor;
            } catch (ErrnoException e) {
                throw new IOException(e);
            }
        }
    }

    @RequiresApi(21)
    public ParcelFileDescriptorRewinder(ParcelFileDescriptor parcelFileDescriptor) {
        this.rewinder = new InternalRewinder(parcelFileDescriptor);
    }

    public static boolean isSupported() {
        return VERSION.SDK_INT >= 21;
    }

    public void cleanup() {
    }

    @RequiresApi(21)
    @NonNull
    public ParcelFileDescriptor rewindAndGet() {
        return this.rewinder.rewind();
    }
}
