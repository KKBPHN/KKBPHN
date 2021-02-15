package androidx.core.app;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import java.lang.reflect.InvocationTargetException;

@RequiresApi(28)
public class AppComponentFactory extends android.app.AppComponentFactory {
    public final Activity instantiateActivity(ClassLoader classLoader, String str, Intent intent) {
        return (Activity) CoreComponentFactory.checkCompatWrapper(instantiateActivityCompat(classLoader, str, intent));
    }

    @NonNull
    public Activity instantiateActivityCompat(@NonNull ClassLoader classLoader, @NonNull String str, @Nullable Intent intent) {
        try {
            return (Activity) Class.forName(str, false, classLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    public final Application instantiateApplication(ClassLoader classLoader, String str) {
        return (Application) CoreComponentFactory.checkCompatWrapper(instantiateApplicationCompat(classLoader, str));
    }

    @NonNull
    public Application instantiateApplicationCompat(@NonNull ClassLoader classLoader, @NonNull String str) {
        try {
            return (Application) Class.forName(str, false, classLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    public final ContentProvider instantiateProvider(ClassLoader classLoader, String str) {
        return (ContentProvider) CoreComponentFactory.checkCompatWrapper(instantiateProviderCompat(classLoader, str));
    }

    @NonNull
    public ContentProvider instantiateProviderCompat(@NonNull ClassLoader classLoader, @NonNull String str) {
        try {
            return (ContentProvider) Class.forName(str, false, classLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    public final BroadcastReceiver instantiateReceiver(ClassLoader classLoader, String str, Intent intent) {
        return (BroadcastReceiver) CoreComponentFactory.checkCompatWrapper(instantiateReceiverCompat(classLoader, str, intent));
    }

    @NonNull
    public BroadcastReceiver instantiateReceiverCompat(@NonNull ClassLoader classLoader, @NonNull String str, @Nullable Intent intent) {
        try {
            return (BroadcastReceiver) Class.forName(str, false, classLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }

    public final Service instantiateService(ClassLoader classLoader, String str, Intent intent) {
        return (Service) CoreComponentFactory.checkCompatWrapper(instantiateServiceCompat(classLoader, str, intent));
    }

    @NonNull
    public Service instantiateServiceCompat(@NonNull ClassLoader classLoader, @NonNull String str, @Nullable Intent intent) {
        try {
            return (Service) Class.forName(str, false, classLoader).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Couldn't call constructor", e);
        }
    }
}
