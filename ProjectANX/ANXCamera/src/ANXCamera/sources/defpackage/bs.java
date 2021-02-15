package defpackage;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/* renamed from: bs reason: default package */
public final class bs {
    public Uri a;
    public Bitmap b;
    public Long c;
    public be d;
    public Integer e;

    public static br a() {
        return new br();
    }

    public final Bundle O000000o(z zVar) {
        Bundle bundle = new Bundle();
        Uri uri = this.a;
        if (uri != null) {
            bundle.putParcelable("uri", uri);
        }
        Bitmap bitmap = this.b;
        if (bitmap != null) {
            int i = (zVar.a & 4) != 0 ? zVar.d : 33554432;
            if (bitmap.getByteCount() > i) {
                Log.w("LensMetadata", String.format("Input bitmap is %d bytes, which is larger than our maximum of %d bytes. Downsampling...", new Object[]{Integer.valueOf(bitmap.getByteCount()), Integer.valueOf(i)}));
                float sqrt = (float) Math.sqrt((double) (((float) i) / ((float) bitmap.getByteCount())));
                Matrix matrix = new Matrix();
                matrix.setScale(sqrt, sqrt);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            bundle.putParcelable("bitmap", bitmap);
        }
        bundle.putInt("lens_transition_type", 0);
        return bundle;
    }

    public final br b() {
        return new br(this);
    }

    public final Bundle c() {
        Bundle bundle = new Bundle();
        Long l = this.c;
        if (l != null) {
            bundle.putLong("activity_launch_timestamp_nanos", l.longValue());
        }
        be beVar = this.d;
        if (beVar != null) {
            bundle.putByteArray("lens_initial_parameters", beVar.m());
        }
        Integer num = this.e;
        if (num != null) {
            bundle.putInt("lens_intent_type", num.intValue());
        }
        return bundle;
    }
}
