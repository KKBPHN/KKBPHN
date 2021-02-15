package defpackage;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.lens.sdk.PendingIntentConsumer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* renamed from: ba reason: default package */
public final class ba extends h implements ServiceConnection, as {
    public final Executor a = new az(new Handler(Looper.getMainLooper()));
    public final Context b;
    public int c;
    public z d;
    public x e;
    public e f;
    public g g;
    public int h = bh.a;
    private final Executor i;
    private final ar j;
    private final al k;
    private int l = 1;

    public ba(Context context, ar arVar, al alVar) {
        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor(at.a);
        this.b = context;
        this.j = arVar;
        this.k = alVar;
        this.i = newSingleThreadExecutor;
    }

    private static boolean b(int i2) {
        return i2 == 5;
    }

    private static boolean c(int i2) {
        return i2 == 6 || i2 == 7 || i2 == 8;
    }

    public final void O000000o(g gVar) {
        eb.c();
        String str = "LensServiceConnImpl";
        if (this.f == null) {
            Log.w(str, "The service is no longer bound.");
        } else {
            try {
                this.g = gVar;
                if (this.g == null) {
                    Log.e(str, "Failed to create a Lens service session.");
                    this.h = bh.j;
                    a(7);
                    return;
                }
                a(4);
                n nVar = (n) o.c.e();
                int i2 = m.ak;
                if (nVar.c) {
                    nVar.b();
                    nVar.c = false;
                }
                o oVar = (o) nVar.b;
                int i3 = i2 - 1;
                if (i2 != 0) {
                    oVar.b = i3;
                    oVar.a |= 1;
                    o oVar2 = (o) nVar.h();
                    n nVar2 = (n) o.c.e();
                    int i4 = m.cJ;
                    if (nVar2.c) {
                        nVar2.b();
                        nVar2.c = false;
                    }
                    o oVar3 = (o) nVar2.b;
                    int i5 = i4 - 1;
                    if (i4 != 0) {
                        oVar3.b = i5;
                        oVar3.a |= 1;
                        cq cqVar = p.a;
                        q qVar = (q) r.a.e();
                        if (qVar.c) {
                            qVar.b();
                            qVar.c = false;
                        }
                        r.O000000o((r) qVar.b);
                        nVar2.O000000o(cqVar, (r) qVar.h());
                        o oVar4 = (o) nVar2.h();
                        g gVar2 = this.g;
                        eb.O00000oO(gVar2);
                        gVar2.a(oVar2.m());
                        g gVar3 = this.g;
                        eb.O00000oO(gVar3);
                        gVar3.a(oVar4.m());
                        return;
                    }
                    throw null;
                }
                throw null;
            } catch (RemoteException e2) {
                Log.w(str, "Failed to call client event callbacks.", e2);
            }
        }
        f();
    }

    public final void O000000o(byte[] bArr, k kVar) {
        this.a.execute(new aw(this, bArr, kVar));
    }

    public final void O00000Oo(byte[] bArr, k kVar) {
        eb.c();
        eb.a(c(), "Attempted to use lensServiceSession before ready.");
        g gVar = this.g;
        eb.O00000oO(gVar);
        gVar.O000000o(bArr, kVar);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ void O00000o0(byte[] bArr, k kVar) {
        cs csVar;
        int i2 = this.l;
        if (i2 == 4 || i2 == 5) {
            cs csVar2 = cs.a;
            if (csVar2 != null) {
                csVar = csVar2;
            } else {
                synchronized (cs.class) {
                    csVar = cs.a;
                    if (csVar == null) {
                        csVar = cy.O00000Oo(cs.class);
                        cs.a = csVar;
                    }
                }
            }
            try {
                ah ahVar = (ah) de.O000000o((de) ah.b, bArr, csVar);
                int a2 = af.a(ahVar.a);
                if (a2 == 0) {
                    a2 = af.a;
                }
                if (a2 != af.bn) {
                    ar arVar = this.j;
                    int a3 = af.a(ahVar.a);
                    if (a3 == 0) {
                        a3 = af.a;
                    }
                    if (a3 == af.bD) {
                        Parcelable parcelable = kVar.a;
                        if (parcelable instanceof PendingIntent) {
                            PendingIntent pendingIntent = (PendingIntent) parcelable;
                            ((aq) arVar).a.a();
                            PendingIntentConsumer pendingIntentConsumer = ((aq) arVar).b;
                            if (pendingIntentConsumer == null) {
                                Log.e("LensServiceBridge", "PendingIntentConsumer cannot be null");
                                return;
                            }
                            pendingIntentConsumer.onReceivedPendingIntent(pendingIntent);
                        }
                    }
                    return;
                }
                cq cqVar = s.a;
                de.O000000o(cqVar);
                ahVar.O00000Oo(cqVar);
                Object O000000o2 = ahVar.d.O000000o(cqVar.d);
                aa aaVar = (aa) (O000000o2 == null ? cqVar.b : cqVar.O00000oO(O000000o2));
                this.c = aaVar.a;
                z zVar = aaVar.b;
                if (zVar == null) {
                    zVar = z.f;
                }
                this.d = zVar;
                x xVar = aaVar.c;
                if (xVar == null) {
                    xVar = x.b;
                }
                this.e = xVar;
                int i3 = bh.a;
                this.h = bh.b;
                a(5);
            } catch (dl e2) {
                Log.e("LensServiceConnImpl", "Unable to parse the protobuf.", e2);
                this.h = bh.j;
                a(8);
            }
        } else {
            Log.w("LensServiceConnImpl", "ServiceEvent received after connection disposed.");
        }
    }

    public final void a() {
        eb.c();
        eb.a(c(), "Attempted to handover when not ready.");
        n nVar = (n) o.c.e();
        int i2 = m.al;
        if (nVar.c) {
            nVar.b();
            nVar.c = false;
        }
        o oVar = (o) nVar.b;
        int i3 = i2 - 1;
        if (i2 != 0) {
            oVar.b = i3;
            oVar.a |= 1;
            cq cqVar = ab.a;
            ac acVar = (ac) ad.a.e();
            if (acVar.c) {
                acVar.b();
                acVar.c = false;
            }
            ad.O000000o((ad) acVar.b);
            nVar.O000000o(cqVar, (ad) acVar.h());
            o oVar2 = (o) nVar.h();
            try {
                g gVar = this.g;
                eb.O00000oO(gVar);
                gVar.a(oVar2.m());
            } catch (RemoteException | SecurityException e2) {
                Log.e("LensServiceConnImpl", "Unable to stop Lens service session.", e2);
            }
            this.h = bh.k;
            a(8);
            return;
        }
        throw null;
    }

    public final void a(int i2) {
        eb.c();
        String.format("Transitioning from state %s to %s.", new Object[]{Integer.valueOf(this.l), Integer.valueOf(i2)});
        int i3 = this.l;
        this.l = i2;
        if (b(i2) && !b(i3)) {
            ar arVar = this.j;
            eb.c();
            ((aq) arVar).e();
        }
        if (c(i2) && !c(i3)) {
            ar arVar2 = this.j;
            eb.c();
            ((aq) arVar2).e();
        }
    }

    public final int b() {
        eb.c();
        eb.a(g(), "Attempted to use lensServiceSession before ready.");
        return this.c;
    }

    public final boolean c() {
        eb.c();
        return b(this.l);
    }

    public final boolean d() {
        eb.c();
        return c(this.l);
    }

    public final int e() {
        eb.c();
        boolean z = true;
        if (!c() && !d()) {
            z = false;
        }
        eb.a(z, "Attempted to use ServerFlags before ready or dead.");
        return this.h;
    }

    public final void f() {
        int i2;
        eb.c();
        if (this.g == null) {
            this.h = bh.j;
            i2 = 7;
        } else {
            this.h = bh.j;
            i2 = 8;
        }
        a(i2);
    }

    public final boolean g() {
        int i2 = this.l;
        return i2 == 5 || i2 == 8;
    }

    public final boolean h() {
        int i2 = this.l;
        return i2 == 3 || i2 == 4 || i2 == 5 || i2 == 7 || i2 == 8;
    }

    public final boolean i() {
        return this.l == 2;
    }

    public final void j() {
        eb.c();
        if (!i() && !h()) {
            a(2);
            this.k.O000000o(new au(this));
        }
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        e eVar;
        eb.c();
        if (iBinder != null) {
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.apps.gsa.publicsearch.IPublicSearchService");
            eVar = queryLocalInterface instanceof e ? (e) queryLocalInterface : new d(iBinder);
        } else {
            eVar = null;
        }
        this.f = eVar;
        this.i.execute(new av(this, eVar));
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        eb.c();
        this.h = bh.j;
        a(7);
    }
}
