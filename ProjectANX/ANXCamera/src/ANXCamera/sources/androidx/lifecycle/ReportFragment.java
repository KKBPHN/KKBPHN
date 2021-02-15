package androidx.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.lifecycle.Lifecycle.Event;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class ReportFragment extends Fragment {
    private static final String REPORT_FRAGMENT_TAG = "androidx.lifecycle.LifecycleDispatcher.report_fragment_tag";
    private ActivityInitializationListener mProcessListener;

    interface ActivityInitializationListener {
        void onCreate();

        void onResume();

        void onStart();
    }

    private void dispatch(Event event) {
        Activity activity = getActivity();
        if (activity instanceof LifecycleRegistryOwner) {
            ((LifecycleRegistryOwner) activity).getLifecycle().handleLifecycleEvent(event);
            return;
        }
        if (activity instanceof LifecycleOwner) {
            Lifecycle lifecycle = ((LifecycleOwner) activity).getLifecycle();
            if (lifecycle instanceof LifecycleRegistry) {
                ((LifecycleRegistry) lifecycle).handleLifecycleEvent(event);
            }
        }
    }

    private void dispatchCreate(ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onCreate();
        }
    }

    private void dispatchResume(ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onResume();
        }
    }

    private void dispatchStart(ActivityInitializationListener activityInitializationListener) {
        if (activityInitializationListener != null) {
            activityInitializationListener.onStart();
        }
    }

    static ReportFragment get(Activity activity) {
        return (ReportFragment) activity.getFragmentManager().findFragmentByTag(REPORT_FRAGMENT_TAG);
    }

    public static void injectIfNeededIn(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        String str = REPORT_FRAGMENT_TAG;
        if (fragmentManager.findFragmentByTag(str) == null) {
            fragmentManager.beginTransaction().add(new ReportFragment(), str).commit();
            fragmentManager.executePendingTransactions();
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        dispatchCreate(this.mProcessListener);
        dispatch(Event.ON_CREATE);
    }

    public void onDestroy() {
        super.onDestroy();
        dispatch(Event.ON_DESTROY);
        this.mProcessListener = null;
    }

    public void onPause() {
        super.onPause();
        dispatch(Event.ON_PAUSE);
    }

    public void onResume() {
        super.onResume();
        dispatchResume(this.mProcessListener);
        dispatch(Event.ON_RESUME);
    }

    public void onStart() {
        super.onStart();
        dispatchStart(this.mProcessListener);
        dispatch(Event.ON_START);
    }

    public void onStop() {
        super.onStop();
        dispatch(Event.ON_STOP);
    }

    /* access modifiers changed from: 0000 */
    public void setProcessListener(ActivityInitializationListener activityInitializationListener) {
        this.mProcessListener = activityInitializationListener;
    }
}
