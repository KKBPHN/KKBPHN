package com.xiaomi.stat;

import com.xiaomi.stat.d.j;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.n;
import java.io.Reader;
import java.io.StringReader;
import org.json.JSONException;
import org.json.JSONObject;

public class MiStatParams {
    private static final String a = "MiStatParams";
    private JSONObject b;

    public MiStatParams() {
        this.b = new JSONObject();
    }

    MiStatParams(MiStatParams miStatParams) {
        JSONObject jSONObject;
        if (miStatParams != null) {
            JSONObject jSONObject2 = miStatParams.b;
            if (jSONObject2 != null) {
                jSONObject = a(jSONObject2);
                this.b = jSONObject;
            }
        }
        jSONObject = new JSONObject();
        this.b = jSONObject;
    }

    private JSONObject a(JSONObject jSONObject) {
        StringReader stringReader;
        Object e;
        try {
            stringReader = new StringReader(jSONObject.toString());
            try {
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int read = stringReader.read();
                    if (read != -1) {
                        sb.append((char) read);
                    } else {
                        JSONObject jSONObject2 = new JSONObject(sb.toString());
                        j.a((Reader) stringReader);
                        return jSONObject2;
                    }
                }
            } catch (Exception e2) {
                e = e2;
                try {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(" deepCopy ");
                    sb2.append(e);
                    k.e(sb2.toString());
                    j.a((Reader) stringReader);
                    return jSONObject;
                } catch (Throwable th) {
                    th = th;
                    j.a((Reader) stringReader);
                    throw th;
                }
            }
        } catch (Exception e3) {
            Object obj = e3;
            stringReader = null;
            e = obj;
            StringBuilder sb22 = new StringBuilder();
            sb22.append(" deepCopy ");
            sb22.append(e);
            k.e(sb22.toString());
            j.a((Reader) stringReader);
            return jSONObject;
        } catch (Throwable th2) {
            stringReader = null;
            th = th2;
            j.a((Reader) stringReader);
            throw th;
        }
    }

    private boolean c(String str) {
        return a() && !this.b.has(str) && this.b.length() == 30;
    }

    /* access modifiers changed from: 0000 */
    public boolean a() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean a(String str) {
        return n.a(str);
    }

    /* access modifiers changed from: 0000 */
    public boolean b(String str) {
        return n.b(str);
    }

    public int getParamsNumber() {
        return this.b.length();
    }

    public boolean isEmpty() {
        return this.b.length() == 0;
    }

    public void putBoolean(String str, boolean z) {
        if (!a(str)) {
            n.e(str);
        } else if (c(str)) {
            n.a();
        } else {
            try {
                this.b.put(str, z);
            } catch (JSONException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("put value error ");
                sb.append(e);
                k.c(a, sb.toString());
            }
        }
    }

    public void putDouble(String str, double d) {
        if (!a(str)) {
            n.e(str);
        } else if (c(str)) {
            n.a();
        } else {
            try {
                this.b.put(str, d);
            } catch (JSONException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("put value error ");
                sb.append(e);
                k.c(a, sb.toString());
            }
        }
    }

    public void putInt(String str, int i) {
        if (!a(str)) {
            n.e(str);
        } else if (c(str)) {
            n.a();
        } else {
            try {
                this.b.put(str, i);
            } catch (JSONException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("put value error ");
                sb.append(e);
                k.c(a, sb.toString());
            }
        }
    }

    public void putLong(String str, long j) {
        if (!a(str)) {
            n.e(str);
        } else if (c(str)) {
            n.a();
        } else {
            try {
                this.b.put(str, j);
            } catch (JSONException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("put value error ");
                sb.append(e);
                k.c(a, sb.toString());
            }
        }
    }

    public void putString(String str, String str2) {
        if (!a(str)) {
            n.e(str);
        } else if (!b(str2)) {
            n.f(str2);
        } else if (c(str)) {
            n.a();
        } else {
            try {
                this.b.put(str, n.c(str2));
            } catch (JSONException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("put value error ");
                sb.append(e);
                k.c(a, sb.toString());
            }
        }
    }

    public String toJsonString() {
        return this.b.toString();
    }
}
