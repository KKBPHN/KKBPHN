package com.xiaomi.a.a.a;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import java.util.Map;

public interface a extends IInterface {

    /* renamed from: com.xiaomi.a.a.a.a$a reason: collision with other inner class name */
    public abstract class C0000a extends Binder implements a {
        static final int a = 1;
        static final int b = 2;
        private static final String c = "com.xiaomi.xmsf.push.service.IHttpService";

        /* renamed from: com.xiaomi.a.a.a.a$a$a reason: collision with other inner class name */
        class C0001a implements a {
            private IBinder a;

            C0001a(IBinder iBinder) {
                this.a = iBinder;
            }

            public String a() {
                return C0000a.c;
            }

            public String a(String str, Map map) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0000a.c);
                    obtain.writeString(str);
                    obtain.writeMap(map);
                    this.a.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public IBinder asBinder() {
                return this.a;
            }

            public String b(String str, Map map) {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(C0000a.c);
                    obtain.writeString(str);
                    obtain.writeMap(map);
                    this.a.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }

        public C0000a() {
            attachInterface(this, c);
        }

        public static a a(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(c);
            return (queryLocalInterface == null || !(queryLocalInterface instanceof a)) ? new C0001a(iBinder) : (a) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) {
            String a2;
            String str = c;
            if (i == 1) {
                parcel.enforceInterface(str);
                a2 = a(parcel.readString(), parcel.readHashMap(C0000a.class.getClassLoader()));
            } else if (i == 2) {
                parcel.enforceInterface(str);
                a2 = b(parcel.readString(), parcel.readHashMap(C0000a.class.getClassLoader()));
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString(str);
                return true;
            }
            parcel2.writeNoException();
            parcel2.writeString(a2);
            return true;
        }
    }

    String a(String str, Map map);

    String b(String str, Map map);
}
