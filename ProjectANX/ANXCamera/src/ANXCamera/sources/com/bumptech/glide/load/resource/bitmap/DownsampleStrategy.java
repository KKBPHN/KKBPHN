package com.bumptech.glide.load.resource.bitmap;

import android.os.Build.VERSION;
import com.bumptech.glide.load.Option;

public abstract class DownsampleStrategy {
    public static final DownsampleStrategy AT_LEAST = new AtLeast();
    public static final DownsampleStrategy AT_MOST = new AtMost();
    public static final DownsampleStrategy CENTER_INSIDE = new CenterInside();
    public static final DownsampleStrategy CENTER_OUTSIDE = new CenterOutside();
    public static final DownsampleStrategy DEFAULT = CENTER_OUTSIDE;
    public static final DownsampleStrategy FIT_CENTER = new FitCenter();
    static final boolean IS_BITMAP_FACTORY_SCALING_SUPPORTED = (VERSION.SDK_INT >= 19);
    public static final DownsampleStrategy NONE = new None();
    public static final Option OPTION = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DownsampleStrategy", DEFAULT);

    class AtLeast extends DownsampleStrategy {
        AtLeast() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int min = Math.min(i2 / i4, i / i3);
            if (min == 0) {
                return 1.0f;
            }
            return 1.0f / ((float) Integer.highestOneBit(min));
        }
    }

    class AtMost extends DownsampleStrategy {
        AtMost() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.MEMORY;
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            int ceil = (int) Math.ceil((double) Math.max(((float) i2) / ((float) i4), ((float) i) / ((float) i3)));
            int i5 = 1;
            int max = Math.max(1, Integer.highestOneBit(ceil));
            if (max >= ceil) {
                i5 = 0;
            }
            return 1.0f / ((float) (max << i5));
        }
    }

    class CenterInside extends DownsampleStrategy {
        CenterInside() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return getScaleFactor(i, i2, i3, i4) == 1.0f ? SampleSizeRounding.QUALITY : DownsampleStrategy.FIT_CENTER.getSampleSizeRounding(i, i2, i3, i4);
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.min(1.0f, DownsampleStrategy.FIT_CENTER.getScaleFactor(i, i2, i3, i4));
        }
    }

    class CenterOutside extends DownsampleStrategy {
        CenterOutside() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return Math.max(((float) i3) / ((float) i), ((float) i4) / ((float) i2));
        }
    }

    class FitCenter extends DownsampleStrategy {
        FitCenter() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return DownsampleStrategy.IS_BITMAP_FACTORY_SCALING_SUPPORTED ? SampleSizeRounding.QUALITY : SampleSizeRounding.MEMORY;
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            if (DownsampleStrategy.IS_BITMAP_FACTORY_SCALING_SUPPORTED) {
                return Math.min(((float) i3) / ((float) i), ((float) i4) / ((float) i2));
            }
            int max = Math.max(i2 / i4, i / i3);
            float f = 1.0f;
            if (max != 0) {
                f = 1.0f / ((float) Integer.highestOneBit(max));
            }
            return f;
        }
    }

    class None extends DownsampleStrategy {
        None() {
        }

        public SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4) {
            return SampleSizeRounding.QUALITY;
        }

        public float getScaleFactor(int i, int i2, int i3, int i4) {
            return 1.0f;
        }
    }

    public enum SampleSizeRounding {
        MEMORY,
        QUALITY
    }

    public abstract SampleSizeRounding getSampleSizeRounding(int i, int i2, int i3, int i4);

    public abstract float getScaleFactor(int i, int i2, int i3, int i4);
}
