package androidx.preference;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.XmlRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference.TargetFragment;
import androidx.preference.PreferenceGroup.PreferencePositionCallback;
import androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener;
import androidx.preference.PreferenceManager.OnNavigateToScreenListener;
import androidx.preference.PreferenceManager.OnPreferenceTreeClickListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.State;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public abstract class PreferenceFragmentCompat extends Fragment implements OnPreferenceTreeClickListener, OnDisplayPreferenceDialogListener, OnNavigateToScreenListener, TargetFragment {
    public static final String ARG_PREFERENCE_ROOT = "androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT";
    private static final String DIALOG_FRAGMENT_TAG = "androidx.preference.PreferenceFragment.DIALOG";
    private static final int MSG_BIND_PREFERENCES = 1;
    private static final String PREFERENCES_TAG = "android:preferences";
    private static final String TAG = "PreferenceFragment";
    private final DividerDecoration mDividerDecoration = new DividerDecoration();
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                PreferenceFragmentCompat.this.bindPreferences();
            }
        }
    };
    private boolean mHavePrefs;
    private boolean mInitDone;
    private int mLayoutResId = R.layout.preference_list_fragment;
    RecyclerView mList;
    private PreferenceManager mPreferenceManager;
    private final Runnable mRequestFocus = new Runnable() {
        public void run() {
            RecyclerView recyclerView = PreferenceFragmentCompat.this.mList;
            recyclerView.focusableViewAvailable(recyclerView);
        }
    };
    private Runnable mSelectPreferenceRunnable;

    class DividerDecoration extends ItemDecoration {
        private boolean mAllowDividerAfterLastItem = true;
        private Drawable mDivider;
        private int mDividerHeight;

        DividerDecoration() {
        }

        private boolean shouldDrawDividerBelow(View view, RecyclerView recyclerView) {
            ViewHolder childViewHolder = recyclerView.getChildViewHolder(view);
            boolean z = (childViewHolder instanceof PreferenceViewHolder) && ((PreferenceViewHolder) childViewHolder).isDividerAllowedBelow();
            if (!z) {
                return false;
            }
            boolean z2 = this.mAllowDividerAfterLastItem;
            int indexOfChild = recyclerView.indexOfChild(view);
            if (indexOfChild < recyclerView.getChildCount() - 1) {
                ViewHolder childViewHolder2 = recyclerView.getChildViewHolder(recyclerView.getChildAt(indexOfChild + 1));
                z2 = (childViewHolder2 instanceof PreferenceViewHolder) && ((PreferenceViewHolder) childViewHolder2).isDividerAllowedAbove();
            }
            return z2;
        }

        public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
            if (shouldDrawDividerBelow(view, recyclerView)) {
                rect.bottom = this.mDividerHeight;
            }
        }

        public void onDrawOver(Canvas canvas, RecyclerView recyclerView, State state) {
            if (this.mDivider != null) {
                int childCount = recyclerView.getChildCount();
                int width = recyclerView.getWidth();
                for (int i = 0; i < childCount; i++) {
                    View childAt = recyclerView.getChildAt(i);
                    if (shouldDrawDividerBelow(childAt, recyclerView)) {
                        int y = ((int) childAt.getY()) + childAt.getHeight();
                        this.mDivider.setBounds(0, y, width, this.mDividerHeight + y);
                        this.mDivider.draw(canvas);
                    }
                }
            }
        }

        public void setAllowDividerAfterLastItem(boolean z) {
            this.mAllowDividerAfterLastItem = z;
        }

        public void setDivider(Drawable drawable) {
            this.mDividerHeight = drawable != null ? drawable.getIntrinsicHeight() : 0;
            this.mDivider = drawable;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }

        public void setDividerHeight(int i) {
            this.mDividerHeight = i;
            PreferenceFragmentCompat.this.mList.invalidateItemDecorations();
        }
    }

    public interface OnPreferenceDisplayDialogCallback {
        boolean onPreferenceDisplayDialog(@NonNull PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(PreferenceFragmentCompat preferenceFragmentCompat, Preference preference);
    }

    public interface OnPreferenceStartScreenCallback {
        boolean onPreferenceStartScreen(PreferenceFragmentCompat preferenceFragmentCompat, PreferenceScreen preferenceScreen);
    }

    class ScrollToPreferenceObserver extends AdapterDataObserver {
        private final Adapter mAdapter;
        private final String mKey;
        private final RecyclerView mList;
        private final Preference mPreference;

        public ScrollToPreferenceObserver(Adapter adapter, RecyclerView recyclerView, Preference preference, String str) {
            this.mAdapter = adapter;
            this.mList = recyclerView;
            this.mPreference = preference;
            this.mKey = str;
        }

        private void scrollToPreference() {
            this.mAdapter.unregisterAdapterDataObserver(this);
            Preference preference = this.mPreference;
            int preferenceAdapterPosition = preference != null ? ((PreferencePositionCallback) this.mAdapter).getPreferenceAdapterPosition(preference) : ((PreferencePositionCallback) this.mAdapter).getPreferenceAdapterPosition(this.mKey);
            if (preferenceAdapterPosition != -1) {
                this.mList.scrollToPosition(preferenceAdapterPosition);
            }
        }

        public void onChanged() {
            scrollToPreference();
        }

        public void onItemRangeChanged(int i, int i2) {
            scrollToPreference();
        }

        public void onItemRangeChanged(int i, int i2, Object obj) {
            scrollToPreference();
        }

        public void onItemRangeInserted(int i, int i2) {
            scrollToPreference();
        }

        public void onItemRangeMoved(int i, int i2, int i3) {
            scrollToPreference();
        }

        public void onItemRangeRemoved(int i, int i2) {
            scrollToPreference();
        }
    }

    private void postBindPreferences() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.obtainMessage(1).sendToTarget();
        }
    }

    private void requirePreferenceManager() {
        if (this.mPreferenceManager == null) {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    private void scrollToPreferenceInternal(final Preference preference, final String str) {
        AnonymousClass3 r0 = new Runnable() {
            public void run() {
                Adapter adapter = PreferenceFragmentCompat.this.mList.getAdapter();
                if (adapter instanceof PreferencePositionCallback) {
                    Preference preference = preference;
                    int preferenceAdapterPosition = preference != null ? ((PreferencePositionCallback) adapter).getPreferenceAdapterPosition(preference) : ((PreferencePositionCallback) adapter).getPreferenceAdapterPosition(str);
                    if (preferenceAdapterPosition != -1) {
                        PreferenceFragmentCompat.this.mList.scrollToPosition(preferenceAdapterPosition);
                    } else {
                        adapter.registerAdapterDataObserver(new ScrollToPreferenceObserver(adapter, PreferenceFragmentCompat.this.mList, preference, str));
                    }
                } else if (adapter != null) {
                    throw new IllegalStateException("Adapter must implement PreferencePositionCallback");
                }
            }
        };
        if (this.mList == null) {
            this.mSelectPreferenceRunnable = r0;
        } else {
            r0.run();
        }
    }

    private void unbindPreferences() {
        getListView().setAdapter(null);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            preferenceScreen.onDetached();
        }
        onUnbindPreferences();
    }

    public void addPreferencesFromResource(@XmlRes int i) {
        requirePreferenceManager();
        setPreferenceScreen(this.mPreferenceManager.inflateFromResource(getContext(), i, getPreferenceScreen()));
    }

    /* access modifiers changed from: 0000 */
    public void bindPreferences() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            getListView().setAdapter(onCreateAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        onBindPreferences();
    }

    @Nullable
    public Preference findPreference(@NonNull CharSequence charSequence) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return preferenceManager.findPreference(charSequence);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public Fragment getCallbackFragment() {
        return null;
    }

    public final RecyclerView getListView() {
        return this.mList;
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceManager.getPreferenceScreen();
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void onBindPreferences() {
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.preferenceTheme, typedValue, true);
        int i = typedValue.resourceId;
        if (i == 0) {
            i = R.style.PreferenceThemeOverlay;
        }
        getActivity().getTheme().applyStyle(i, false);
        this.mPreferenceManager = new PreferenceManager(getContext());
        this.mPreferenceManager.setOnNavigateToScreenListener(this);
        onCreatePreferences(bundle, getArguments() != null ? getArguments().getString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT") : null);
    }

    /* access modifiers changed from: protected */
    public Adapter onCreateAdapter(PreferenceScreen preferenceScreen) {
        return new PreferenceGroupAdapter(preferenceScreen);
    }

    public LayoutManager onCreateLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public abstract void onCreatePreferences(Bundle bundle, String str);

    public RecyclerView onCreateRecyclerView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (getContext().getPackageManager().hasSystemFeature("android.hardware.type.automotive")) {
            RecyclerView recyclerView = (RecyclerView) viewGroup.findViewById(R.id.recycler_view);
            if (recyclerView != null) {
                return recyclerView;
            }
        }
        RecyclerView recyclerView2 = (RecyclerView) layoutInflater.inflate(R.layout.preference_recyclerview, viewGroup, false);
        recyclerView2.setLayoutManager(onCreateLayoutManager());
        recyclerView2.setAccessibilityDelegateCompat(new PreferenceRecyclerViewAccessibilityDelegate(recyclerView2));
        return recyclerView2;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R.styleable.PreferenceFragmentCompat, R.attr.preferenceFragmentCompatStyle, 0);
        this.mLayoutResId = obtainStyledAttributes.getResourceId(R.styleable.PreferenceFragmentCompat_android_layout, this.mLayoutResId);
        Drawable drawable = obtainStyledAttributes.getDrawable(R.styleable.PreferenceFragmentCompat_android_divider);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.PreferenceFragmentCompat_android_dividerHeight, -1);
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.PreferenceFragmentCompat_allowDividerAfterLastItem, true);
        obtainStyledAttributes.recycle();
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(getContext());
        View inflate = cloneInContext.inflate(this.mLayoutResId, viewGroup, false);
        View findViewById = inflate.findViewById(16908351);
        if (findViewById instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) findViewById;
            RecyclerView onCreateRecyclerView = onCreateRecyclerView(cloneInContext, viewGroup2, bundle);
            if (onCreateRecyclerView != null) {
                this.mList = onCreateRecyclerView;
                onCreateRecyclerView.addItemDecoration(this.mDividerDecoration);
                setDivider(drawable);
                if (dimensionPixelSize != -1) {
                    setDividerHeight(dimensionPixelSize);
                }
                this.mDividerDecoration.setAllowDividerAfterLastItem(z);
                if (this.mList.getParent() == null) {
                    viewGroup2.addView(this.mList);
                }
                this.mHandler.post(this.mRequestFocus);
                return inflate;
            }
            throw new RuntimeException("Could not create RecyclerView");
        }
        throw new IllegalStateException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    public void onDestroyView() {
        this.mHandler.removeCallbacks(this.mRequestFocus);
        this.mHandler.removeMessages(1);
        if (this.mHavePrefs) {
            unbindPreferences();
        }
        this.mList = null;
        super.onDestroyView();
    }

    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment;
        boolean onPreferenceDisplayDialog = getCallbackFragment() instanceof OnPreferenceDisplayDialogCallback ? ((OnPreferenceDisplayDialogCallback) getCallbackFragment()).onPreferenceDisplayDialog(this, preference) : false;
        if (!onPreferenceDisplayDialog && (getActivity() instanceof OnPreferenceDisplayDialogCallback)) {
            onPreferenceDisplayDialog = ((OnPreferenceDisplayDialogCallback) getActivity()).onPreferenceDisplayDialog(this, preference);
        }
        if (!onPreferenceDisplayDialog) {
            FragmentManager fragmentManager = getFragmentManager();
            String str = DIALOG_FRAGMENT_TAG;
            if (fragmentManager.findFragmentByTag(str) == null) {
                if (preference instanceof EditTextPreference) {
                    dialogFragment = EditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else if (preference instanceof ListPreference) {
                    dialogFragment = ListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else if (preference instanceof MultiSelectListPreference) {
                    dialogFragment = MultiSelectListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Cannot display dialog for an unknown Preference type: ");
                    sb.append(preference.getClass().getSimpleName());
                    sb.append(". Make sure to implement onPreferenceDisplayDialog() to handle displaying a custom dialog for this Preference.");
                    throw new IllegalArgumentException(sb.toString());
                }
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(getFragmentManager(), str);
            }
        }
    }

    public void onNavigateToScreen(PreferenceScreen preferenceScreen) {
        if (!(getCallbackFragment() instanceof OnPreferenceStartScreenCallback ? ((OnPreferenceStartScreenCallback) getCallbackFragment()).onPreferenceStartScreen(this, preferenceScreen) : false) && (getActivity() instanceof OnPreferenceStartScreenCallback)) {
            ((OnPreferenceStartScreenCallback) getActivity()).onPreferenceStartScreen(this, preferenceScreen);
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getFragment() == null) {
            return false;
        }
        boolean onPreferenceStartFragment = getCallbackFragment() instanceof OnPreferenceStartFragmentCallback ? ((OnPreferenceStartFragmentCallback) getCallbackFragment()).onPreferenceStartFragment(this, preference) : false;
        if (!onPreferenceStartFragment && (getActivity() instanceof OnPreferenceStartFragmentCallback)) {
            onPreferenceStartFragment = ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(this, preference);
        }
        if (!onPreferenceStartFragment) {
            Log.w(TAG, "onPreferenceStartFragment is not implemented in the parent activity - attempting to use a fallback implementation. You should implement this method so that you can configure the new fragment that will be displayed, and set a transition between the fragments.");
            FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
            Bundle extras = preference.getExtras();
            Fragment instantiate = supportFragmentManager.getFragmentFactory().instantiate(requireActivity().getClassLoader(), preference.getFragment());
            instantiate.setArguments(extras);
            instantiate.setTargetFragment(this, 0);
            supportFragmentManager.beginTransaction().replace(((View) getView().getParent()).getId(), instantiate).addToBackStack(null).commit();
        }
        return true;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle bundle2 = new Bundle();
            preferenceScreen.saveHierarchyState(bundle2);
            bundle.putBundle(PREFERENCES_TAG, bundle2);
        }
    }

    public void onStart() {
        super.onStart();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(this);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(this);
    }

    public void onStop() {
        super.onStop();
        this.mPreferenceManager.setOnPreferenceTreeClickListener(null);
        this.mPreferenceManager.setOnDisplayPreferenceDialogListener(null);
    }

    /* access modifiers changed from: protected */
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void onUnbindPreferences() {
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle != null) {
            Bundle bundle2 = bundle.getBundle(PREFERENCES_TAG);
            if (bundle2 != null) {
                PreferenceScreen preferenceScreen = getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceScreen.restoreHierarchyState(bundle2);
                }
            }
        }
        if (this.mHavePrefs) {
            bindPreferences();
            Runnable runnable = this.mSelectPreferenceRunnable;
            if (runnable != null) {
                runnable.run();
                this.mSelectPreferenceRunnable = null;
            }
        }
        this.mInitDone = true;
    }

    public void scrollToPreference(Preference preference) {
        scrollToPreferenceInternal(preference, null);
    }

    public void scrollToPreference(String str) {
        scrollToPreferenceInternal(null, str);
    }

    public void setDivider(Drawable drawable) {
        this.mDividerDecoration.setDivider(drawable);
    }

    public void setDividerHeight(int i) {
        this.mDividerDecoration.setDividerHeight(i);
    }

    public void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        if (this.mPreferenceManager.setPreferences(preferenceScreen) && preferenceScreen != null) {
            onUnbindPreferences();
            this.mHavePrefs = true;
            if (this.mInitDone) {
                postBindPreferences();
            }
        }
    }

    /* JADX WARNING: type inference failed for: r4v1, types: [androidx.preference.PreferenceGroup, androidx.preference.PreferenceScreen] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v4, types: [androidx.preference.Preference] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v1, types: [androidx.preference.PreferenceGroup, androidx.preference.PreferenceScreen]
  assigns: [androidx.preference.PreferenceScreen, androidx.preference.Preference]
  uses: [androidx.preference.PreferenceGroup, ?[OBJECT, ARRAY]]
  mth insns count: 20
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setPreferencesFromResource(@XmlRes int i, @Nullable String str) {
        requirePreferenceManager();
        ? inflateFromResource = this.mPreferenceManager.inflateFromResource(getContext(), i, null);
        if (str != null) {
            inflateFromResource = inflateFromResource.findPreference(str);
            if (!(inflateFromResource instanceof PreferenceScreen)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Preference object with key ");
                sb.append(str);
                sb.append(" is not a PreferenceScreen");
                throw new IllegalArgumentException(sb.toString());
            }
        }
        setPreferenceScreen((PreferenceScreen) inflateFromResource);
    }
}
