package androidx.lifecycle;

import android.app.Application;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public class ViewModelProvider {
    private static final String DEFAULT_KEY = "androidx.lifecycle.ViewModelProvider.DefaultKey";
    private final Factory mFactory;
    private final ViewModelStore mViewModelStore;

    public class AndroidViewModelFactory extends NewInstanceFactory {
        private static AndroidViewModelFactory sInstance;
        private Application mApplication;

        public AndroidViewModelFactory(@NonNull Application application) {
            this.mApplication = application;
        }

        @NonNull
        public static AndroidViewModelFactory getInstance(@NonNull Application application) {
            if (sInstance == null) {
                sInstance = new AndroidViewModelFactory(application);
            }
            return sInstance;
        }

        @NonNull
        public ViewModel create(@NonNull Class cls) {
            String str = "Cannot create an instance of ";
            if (!AndroidViewModel.class.isAssignableFrom(cls)) {
                return super.create(cls);
            }
            try {
                return (ViewModel) cls.getConstructor(new Class[]{Application.class}).newInstance(new Object[]{this.mApplication});
            } catch (NoSuchMethodException e) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(cls);
                throw new RuntimeException(sb.toString(), e);
            } catch (IllegalAccessException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(cls);
                throw new RuntimeException(sb2.toString(), e2);
            } catch (InstantiationException e3) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str);
                sb3.append(cls);
                throw new RuntimeException(sb3.toString(), e3);
            } catch (InvocationTargetException e4) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(cls);
                throw new RuntimeException(sb4.toString(), e4);
            }
        }
    }

    public interface Factory {
        @NonNull
        ViewModel create(@NonNull Class cls);
    }

    abstract class KeyedFactory implements Factory {
        KeyedFactory() {
        }

        @NonNull
        public ViewModel create(@NonNull Class cls) {
            throw new UnsupportedOperationException("create(String, Class<?>) must be called on implementaions of KeyedFactory");
        }

        @NonNull
        public abstract ViewModel create(@NonNull String str, @NonNull Class cls);
    }

    public class NewInstanceFactory implements Factory {
        @NonNull
        public ViewModel create(@NonNull Class cls) {
            String str = "Cannot create an instance of ";
            try {
                return (ViewModel) cls.newInstance();
            } catch (InstantiationException e) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(cls);
                throw new RuntimeException(sb.toString(), e);
            } catch (IllegalAccessException e2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str);
                sb2.append(cls);
                throw new RuntimeException(sb2.toString(), e2);
            }
        }
    }

    public ViewModelProvider(@NonNull ViewModelStore viewModelStore, @NonNull Factory factory) {
        this.mFactory = factory;
        this.mViewModelStore = viewModelStore;
    }

    public ViewModelProvider(@NonNull ViewModelStoreOwner viewModelStoreOwner, @NonNull Factory factory) {
        this(viewModelStoreOwner.getViewModelStore(), factory);
    }

    @MainThread
    @NonNull
    public ViewModel get(@NonNull Class cls) {
        String canonicalName = cls.getCanonicalName();
        if (canonicalName != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("androidx.lifecycle.ViewModelProvider.DefaultKey:");
            sb.append(canonicalName);
            return get(sb.toString(), cls);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @MainThread
    @NonNull
    public ViewModel get(@NonNull String str, @NonNull Class cls) {
        ViewModel viewModel = this.mViewModelStore.get(str);
        if (cls.isInstance(viewModel)) {
            return viewModel;
        }
        Factory factory = this.mFactory;
        ViewModel create = factory instanceof KeyedFactory ? ((KeyedFactory) factory).create(str, cls) : factory.create(cls);
        this.mViewModelStore.put(str, create);
        return create;
    }
}
