package defpackage;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: cy reason: default package */
public abstract class cy {
    private static final Logger a = Logger.getLogger(cn.class.getName());
    private static final String b = "bz";

    public static cs O00000Oo(Class cls) {
        String str;
        ClassLoader classLoader = cy.class.getClassLoader();
        if (cls.equals(cs.class)) {
            str = b;
        } else if (cls.getPackage().equals(cy.class.getPackage())) {
            str = String.format("%s.BlazeGenerated%sLoader", new Object[]{cls.getPackage().getName(), cls.getSimpleName()});
        } else {
            throw new IllegalArgumentException(cls.getName());
        }
        try {
            return (cs) cls.cast(((cy) Class.forName(str, true, classLoader).getConstructor(new Class[0]).newInstance(new Object[0])).a());
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        } catch (InstantiationException e2) {
            throw new IllegalStateException(e2);
        } catch (IllegalAccessException e3) {
            throw new IllegalStateException(e3);
        } catch (InvocationTargetException e4) {
            throw new IllegalStateException(e4);
        } catch (ClassNotFoundException unused) {
            Iterator it = ServiceLoader.load(cy.class, classLoader).iterator();
            ArrayList arrayList = new ArrayList();
            while (it.hasNext()) {
                try {
                    arrayList.add(cls.cast(((cy) it.next()).a()));
                } catch (ServiceConfigurationError e5) {
                    ServiceConfigurationError serviceConfigurationError = e5;
                    Logger logger = a;
                    Level level = Level.SEVERE;
                    String valueOf = String.valueOf(cls.getSimpleName());
                    String str2 = "Unable to load ";
                    logger.logp(level, "com.google.protobuf.GeneratedExtensionRegistryLoader", "load", valueOf.length() == 0 ? new String(str2) : str2.concat(valueOf), serviceConfigurationError);
                }
            }
            if (arrayList.size() == 1) {
                return (cs) arrayList.get(0);
            }
            if (arrayList.size() == 0) {
                return null;
            }
            try {
                return (cs) cls.getMethod("combine", new Class[]{Collection.class}).invoke(null, new Object[]{arrayList});
            } catch (NoSuchMethodException e6) {
                throw new IllegalStateException(e6);
            } catch (IllegalAccessException e7) {
                throw new IllegalStateException(e7);
            } catch (InvocationTargetException e8) {
                throw new IllegalStateException(e8);
            }
        }
    }

    public abstract cs a();
}
