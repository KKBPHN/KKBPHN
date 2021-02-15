package com.google.lens.sdk;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.Log;
import com.android.camera.CameraSettings;

public class LensApi {
    static final Uri a = Uri.parse("googleapp://lens");
    public static final /* synthetic */ int b = 0;
    private final al c;
    private final aq d;
    private final KeyguardManager e;

    @Keep
    public interface LensAvailabilityCallback {
        void onAvailabilityStatusFetched(@LensAvailabilityStatus int i);
    }

    @Keep
    public @interface LensAvailabilityStatus {
        public static final int LENS_AVAILABILITY_UNKNOWN = -1;
        public static final int LENS_READY = 0;
        public static final int LENS_UNAVAILABLE = 1;
        public static final int LENS_UNAVAILABLE_AGSA_OUTDATED = 6;
        public static final int LENS_UNAVAILABLE_ASSISTANT_EYES_FLAG_DISABLED = 8;
        public static final int LENS_UNAVAILABLE_DEVICE_INCOMPATIBLE = 3;
        public static final int LENS_UNAVAILABLE_DEVICE_LOCKED = 5;
        public static final int LENS_UNAVAILABLE_FEATURE_UNAVAILABLE = 11;
        public static final int LENS_UNAVAILABLE_INVALID_CURSOR = 4;
        public static final int LENS_UNAVAILABLE_LOCALE_NOT_SUPPORTED = 2;
        public static final int LENS_UNAVAILABLE_SERVICE_BUSY_FAILURE = 10;
        public static final int LENS_UNAVAILABLE_SERVICE_UNAVAILABLE = 9;
        public static final int LENS_UNAVAILABLE_UNKNOWN_ERROR_CODE = 12;
    }

    @Keep
    public @interface LensFeature {
        public static final int LENS_AR_STICKERS = 1;
        public static final int LENS_CORE = 0;
    }

    @Keep
    public @interface LensLaunchStatus {
        public static final int LAUNCH_FAILURE_UNLOCK_FAILED = 1;
        public static final int LAUNCH_SUCCESS = 0;
    }

    @Keep
    public interface LensLaunchStatusCallback {
        void onLaunchStatusFetched(@LensLaunchStatus int i);
    }

    @Keep
    public LensApi(Context context) {
        this.e = (KeyguardManager) context.getSystemService("keyguard");
        this.c = new al(context);
        this.d = new aq(context, this.c);
    }

    private final void a(Activity activity, LensLaunchStatusCallback lensLaunchStatusCallback, Runnable runnable) {
        if (this.e.isKeyguardLocked()) {
            int i = VERSION.SDK_INT;
            if (i < 26) {
                StringBuilder sb = new StringBuilder(64);
                sb.append("Cannot start Lens when device is locked with Android ");
                sb.append(i);
                Log.e("LensApi", sb.toString());
                if (lensLaunchStatusCallback != null) {
                    lensLaunchStatusCallback.onLaunchStatusFetched(1);
                }
                return;
            }
            this.e.requestDismissKeyguard(activity, new bp(runnable, lensLaunchStatusCallback));
            return;
        }
        runnable.run();
        if (lensLaunchStatusCallback != null) {
            lensLaunchStatusCallback.onLaunchStatusFetched(0);
        }
    }

    private final boolean a(String str) {
        String str2 = this.c.f.c;
        if (TextUtils.isEmpty(str2)) {
            return true;
        }
        String str3 = "\\.";
        String[] split = str2.split(str3, -1);
        String[] split2 = str.split(str3, -1);
        int min = Math.min(split.length, split2.length);
        for (int i = 0; i < min; i++) {
            int parseInt = Integer.parseInt(split[i]);
            int parseInt2 = Integer.parseInt(split2[i]);
            if (parseInt < parseInt2) {
                return true;
            }
            if (parseInt > parseInt2) {
                return false;
            }
        }
        return split.length < split2.length;
    }

    public final void a(Activity activity) {
        aq aqVar = this.d;
        eb.c();
        if (aqVar.a.c()) {
            n nVar = (n) o.c.e();
            int i = m.cI;
            if (nVar.c) {
                nVar.b();
                nVar.c = false;
            }
            o oVar = (o) nVar.b;
            int i2 = i - 1;
            if (i != 0) {
                oVar.b = i2;
                oVar.a |= 1;
                o oVar2 = (o) nVar.h();
                try {
                    as asVar = aqVar.a;
                    byte[] m = oVar2.m();
                    eb.c();
                    eb.a(((ba) asVar).c(), "Attempted to use lensServiceSession before ready.");
                    g gVar = ((ba) asVar).g;
                    eb.O00000oO(gVar);
                    gVar.a(m);
                } catch (RemoteException | SecurityException e2) {
                    Log.e("LensServiceBridge", "Unable to send prewarm signal.", e2);
                }
            } else {
                throw null;
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(a);
        activity.startActivityForResult(intent, 0);
    }

    public final void a(Activity activity, bs bsVar) {
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        aq aqVar = this.d;
        bm bmVar = new bm(this, bsVar, elapsedRealtimeNanos, activity);
        eb.c();
        aqVar.O000000o((ap) new ao(aqVar, bmVar));
    }

    public final void a(bs bsVar) {
        aq aqVar = this.d;
        if (aqVar.O000000o(bsVar.O000000o(aqVar.c()))) {
            aq aqVar2 = this.d;
            aqVar2.c();
            Bundle c2 = bsVar.c();
            eb.c();
            if (aqVar2.a.c()) {
                n nVar = (n) o.c.e();
                int i = m.cM;
                if (nVar.c) {
                    nVar.b();
                    nVar.c = false;
                }
                o oVar = (o) nVar.b;
                int i2 = i - 1;
                if (i != 0) {
                    oVar.b = i2;
                    oVar.a |= 1;
                    o oVar2 = (o) nVar.h();
                    try {
                        aqVar2.a.O00000Oo(oVar2.m(), new k(c2));
                        aqVar2.a.a();
                        return;
                    } catch (RemoteException | SecurityException e2) {
                        Log.e("LensServiceBridge", "Failed to start Lens", e2);
                    }
                } else {
                    throw null;
                }
            }
            Log.e("LensApi", "Failed to start lens.");
        }
    }

    public final boolean a(Bitmap bitmap, bs bsVar) {
        String str = "LensApi";
        if (bitmap == null) {
            Log.w(str, "launchLensActivityWithBitmap: bitmap should not be null.");
        }
        if (this.e.isKeyguardLocked()) {
            Log.e(str, "Cannot start Lens with Bitmap when device is locked.");
            return false;
        } else if (this.d.g() != bh.b) {
            return false;
        } else {
            br b2 = bsVar.b();
            b2.O000000o(bitmap);
            a(b2.a());
            return true;
        }
    }

    public final boolean a(bs bsVar, PendingIntentConsumer pendingIntentConsumer) {
        if (this.d.f() == bh.b) {
            aq aqVar = this.d;
            aqVar.O000000o(bsVar.O000000o(aqVar.c()));
            aq aqVar2 = this.d;
            aqVar2.c();
            Bundle c2 = bsVar.c();
            eb.c();
            aqVar2.b = pendingIntentConsumer;
            if (aqVar2.a.c()) {
                n nVar = (n) o.c.e();
                int i = m.cN;
                if (nVar.c) {
                    nVar.b();
                    nVar.c = false;
                }
                o oVar = (o) nVar.b;
                int i2 = i - 1;
                if (i != 0) {
                    oVar.b = i2;
                    oVar.a |= 1;
                    o oVar2 = (o) nVar.h();
                    try {
                        aqVar2.a.O00000Oo(oVar2.m(), new k(c2));
                        return true;
                    } catch (RemoteException | SecurityException e2) {
                        Log.e("LensServiceBridge", "Failed to send Lens service client event", e2);
                    }
                } else {
                    throw null;
                }
            }
            Log.e("LensApi", "Failed to request pending intent.");
        }
        return false;
    }

    @Keep
    public void checkArStickersAvailability(LensAvailabilityCallback lensAvailabilityCallback) {
        this.c.O000000o(new bq(lensAvailabilityCallback, 1));
    }

    @Keep
    public void checkLensAvailability(LensAvailabilityCallback lensAvailabilityCallback) {
        if (this.e.isKeyguardLocked() && VERSION.SDK_INT < 26) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(5);
        } else if (a("8.3")) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(6);
        } else {
            this.c.O000000o(new bq(lensAvailabilityCallback, 0));
        }
    }

    @Keep
    public void checkPendingIntentAvailability(LensAvailabilityCallback lensAvailabilityCallback) {
        if (this.e.isKeyguardLocked() && VERSION.SDK_INT < 26) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(5);
        } else if (a("9.72")) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(6);
        } else {
            aq aqVar = this.d;
            bo boVar = new bo(lensAvailabilityCallback);
            eb.c();
            aqVar.O000000o((ap) new an(aqVar, boVar));
        }
    }

    @Keep
    public void checkPostCaptureAvailability(LensAvailabilityCallback lensAvailabilityCallback) {
        if (this.e.isKeyguardLocked() && VERSION.SDK_INT < 26) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(5);
        } else if (a("8.19")) {
            lensAvailabilityCallback.onAvailabilityStatusFetched(6);
        } else {
            aq aqVar = this.d;
            bn bnVar = new bn(lensAvailabilityCallback);
            eb.c();
            aqVar.O000000o((ap) new am(aqVar, bnVar));
        }
    }

    @Keep
    @Deprecated
    public void launchLensActivity(Activity activity) {
        a(activity, null, new bj(this, activity));
    }

    @Keep
    @Deprecated
    public void launchLensActivity(Activity activity, @LensFeature int i) {
        if (i == 0) {
            a(activity, null, new bl(this, activity));
        } else if (i != 1) {
            StringBuilder sb = new StringBuilder(34);
            sb.append("Invalid lens activity: ");
            sb.append(i);
            Log.w("LensApi", sb.toString());
        } else {
            int a2 = bh.a(this.c.f.e);
            if (a2 == 0) {
                a2 = bh.a;
            }
            if (a2 == bh.b) {
                Intent intent = new Intent();
                intent.setClassName(CameraSettings.GOOGLE_LENS_PACKAGE, "com.google.vr.apps.ornament.app.MainActivity");
                activity.startActivity(intent);
            }
        }
    }

    @Keep
    public void launchLensActivity(Activity activity, LensLaunchStatusCallback lensLaunchStatusCallback) {
        a(activity, lensLaunchStatusCallback, new bk(this, activity, bs.a().a()));
    }

    @Keep
    public boolean launchLensActivityWithBitmap(Bitmap bitmap) {
        if (this.e.isKeyguardLocked()) {
            Log.e("LensApi", "Cannot start Lens with Bitmap when device is locked.");
            return false;
        }
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        br a2 = bs.a();
        a2.a(Long.valueOf(elapsedRealtimeNanos));
        return a(bitmap, a2.a());
    }

    @Keep
    public boolean launchLensActivityWithBitmapForTranslate(Bitmap bitmap) {
        if ((this.d.d().a & 2) == 0) {
            Log.e("LensApi", "Translate is not supported.");
            return false;
        }
        bb bbVar = (bb) be.c.e();
        bd bdVar = bd.a;
        if (bbVar.c) {
            bbVar.b();
            bbVar.c = false;
        }
        be beVar = (be) bbVar.b;
        bdVar.getClass();
        beVar.b = bdVar;
        beVar.a |= 2;
        be beVar2 = (be) bbVar.h();
        br a2 = bs.a();
        a2.a.e = Integer.valueOf(5);
        a2.a.d = beVar2;
        return a(bitmap, a2.a());
    }

    @Keep
    public void onPause() {
        this.d.b();
    }

    @Keep
    public void onResume() {
        this.d.a();
    }

    @Keep
    public boolean requestLensActivityPendingIntent(PendingIntentConsumer pendingIntentConsumer) {
        return a(bs.a().a(), pendingIntentConsumer);
    }

    @Keep
    public boolean requestLensActivityPendingIntentWithBitmap(Bitmap bitmap, PendingIntentConsumer pendingIntentConsumer) {
        br a2 = bs.a();
        a2.O000000o(bitmap);
        return a(a2.a(), pendingIntentConsumer);
    }

    @Keep
    public boolean requestLensActivityPendingIntentWithBitmapUri(Context context, Uri uri, PendingIntentConsumer pendingIntentConsumer) {
        if (context != null) {
            context.grantUriPermission("com.google.android.googlequicksearchbox", uri, 1);
        }
        br a2 = bs.a();
        a2.a.a = uri;
        return a(a2.a(), pendingIntentConsumer);
    }
}
