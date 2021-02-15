package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.ParcelFileDescriptor;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.data.InputStreamRewinder;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.InputStream;
import java.util.List;

interface ImageReader {

    public final class InputStreamImageReader implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final InputStreamRewinder dataRewinder;
        private final List parsers;

        InputStreamImageReader(InputStream inputStream, List list, ArrayPool arrayPool) {
            Preconditions.checkNotNull(arrayPool);
            this.byteArrayPool = arrayPool;
            Preconditions.checkNotNull(list);
            this.parsers = list;
            this.dataRewinder = new InputStreamRewinder(inputStream, arrayPool);
        }

        @Nullable
        public Bitmap decodeBitmap(Options options) {
            return BitmapFactory.decodeStream(this.dataRewinder.rewindAndGet(), null, options);
        }

        public int getImageOrientation() {
            return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
        }

        public ImageType getImageType() {
            return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder.rewindAndGet(), this.byteArrayPool);
        }

        public void stopGrowingBuffers() {
            this.dataRewinder.fixMarkLimits();
        }
    }

    @RequiresApi(21)
    public final class ParcelFileDescriptorImageReader implements ImageReader {
        private final ArrayPool byteArrayPool;
        private final ParcelFileDescriptorRewinder dataRewinder;
        private final List parsers;

        ParcelFileDescriptorImageReader(ParcelFileDescriptor parcelFileDescriptor, List list, ArrayPool arrayPool) {
            Preconditions.checkNotNull(arrayPool);
            this.byteArrayPool = arrayPool;
            Preconditions.checkNotNull(list);
            this.parsers = list;
            this.dataRewinder = new ParcelFileDescriptorRewinder(parcelFileDescriptor);
        }

        @Nullable
        public Bitmap decodeBitmap(Options options) {
            return BitmapFactory.decodeFileDescriptor(this.dataRewinder.rewindAndGet().getFileDescriptor(), null, options);
        }

        public int getImageOrientation() {
            return ImageHeaderParserUtils.getOrientation(this.parsers, this.dataRewinder, this.byteArrayPool);
        }

        public ImageType getImageType() {
            return ImageHeaderParserUtils.getType(this.parsers, this.dataRewinder, this.byteArrayPool);
        }

        public void stopGrowingBuffers() {
        }
    }

    @Nullable
    Bitmap decodeBitmap(Options options);

    int getImageOrientation();

    ImageType getImageType();

    void stopGrowingBuffers();
}
