package androidx.core.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import androidx.annotation.StringRes;
import androidx.core.content.IntentCompat;
import java.util.ArrayList;

public final class ShareCompat {
    public static final String EXTRA_CALLING_ACTIVITY = "androidx.core.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_ACTIVITY_INTEROP = "android.support.v4.app.EXTRA_CALLING_ACTIVITY";
    public static final String EXTRA_CALLING_PACKAGE = "androidx.core.app.EXTRA_CALLING_PACKAGE";
    public static final String EXTRA_CALLING_PACKAGE_INTEROP = "android.support.v4.app.EXTRA_CALLING_PACKAGE";
    private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";

    public class IntentBuilder {
        private Activity mActivity;
        private ArrayList mBccAddresses;
        private ArrayList mCcAddresses;
        private CharSequence mChooserTitle;
        private Intent mIntent = new Intent().setAction("android.intent.action.SEND");
        private ArrayList mStreams;
        private ArrayList mToAddresses;

        private IntentBuilder(Activity activity) {
            this.mActivity = activity;
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE, activity.getPackageName());
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_PACKAGE_INTEROP, activity.getPackageName());
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY, activity.getComponentName());
            this.mIntent.putExtra(ShareCompat.EXTRA_CALLING_ACTIVITY_INTEROP, activity.getComponentName());
            this.mIntent.addFlags(524288);
        }

        private void combineArrayExtra(String str, ArrayList arrayList) {
            String[] stringArrayExtra = this.mIntent.getStringArrayExtra(str);
            int length = stringArrayExtra != null ? stringArrayExtra.length : 0;
            String[] strArr = new String[(arrayList.size() + length)];
            arrayList.toArray(strArr);
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, strArr, arrayList.size(), length);
            }
            this.mIntent.putExtra(str, strArr);
        }

        private void combineArrayExtra(String str, String[] strArr) {
            Intent intent = getIntent();
            String[] stringArrayExtra = intent.getStringArrayExtra(str);
            int length = stringArrayExtra != null ? stringArrayExtra.length : 0;
            String[] strArr2 = new String[(strArr.length + length)];
            if (stringArrayExtra != null) {
                System.arraycopy(stringArrayExtra, 0, strArr2, 0, length);
            }
            System.arraycopy(strArr, 0, strArr2, length, strArr.length);
            intent.putExtra(str, strArr2);
        }

        public static IntentBuilder from(Activity activity) {
            return new IntentBuilder(activity);
        }

        public IntentBuilder addEmailBcc(String str) {
            if (this.mBccAddresses == null) {
                this.mBccAddresses = new ArrayList();
            }
            this.mBccAddresses.add(str);
            return this;
        }

        public IntentBuilder addEmailBcc(String[] strArr) {
            combineArrayExtra("android.intent.extra.BCC", strArr);
            return this;
        }

        public IntentBuilder addEmailCc(String str) {
            if (this.mCcAddresses == null) {
                this.mCcAddresses = new ArrayList();
            }
            this.mCcAddresses.add(str);
            return this;
        }

        public IntentBuilder addEmailCc(String[] strArr) {
            combineArrayExtra("android.intent.extra.CC", strArr);
            return this;
        }

        public IntentBuilder addEmailTo(String str) {
            if (this.mToAddresses == null) {
                this.mToAddresses = new ArrayList();
            }
            this.mToAddresses.add(str);
            return this;
        }

        public IntentBuilder addEmailTo(String[] strArr) {
            combineArrayExtra("android.intent.extra.EMAIL", strArr);
            return this;
        }

        public IntentBuilder addStream(Uri uri) {
            String str = "android.intent.extra.STREAM";
            Uri uri2 = (Uri) this.mIntent.getParcelableExtra(str);
            if (this.mStreams == null && uri2 == null) {
                return setStream(uri);
            }
            if (this.mStreams == null) {
                this.mStreams = new ArrayList();
            }
            if (uri2 != null) {
                this.mIntent.removeExtra(str);
                this.mStreams.add(uri2);
            }
            this.mStreams.add(uri);
            return this;
        }

        public Intent createChooserIntent() {
            return Intent.createChooser(getIntent(), this.mChooserTitle);
        }

        /* access modifiers changed from: 0000 */
        public Activity getActivity() {
            return this.mActivity;
        }

        public Intent getIntent() {
            ArrayList arrayList = this.mToAddresses;
            if (arrayList != null) {
                combineArrayExtra("android.intent.extra.EMAIL", arrayList);
                this.mToAddresses = null;
            }
            ArrayList arrayList2 = this.mCcAddresses;
            if (arrayList2 != null) {
                combineArrayExtra("android.intent.extra.CC", arrayList2);
                this.mCcAddresses = null;
            }
            ArrayList arrayList3 = this.mBccAddresses;
            if (arrayList3 != null) {
                combineArrayExtra("android.intent.extra.BCC", arrayList3);
                this.mBccAddresses = null;
            }
            ArrayList arrayList4 = this.mStreams;
            boolean z = true;
            if (arrayList4 == null || arrayList4.size() <= 1) {
                z = false;
            }
            String str = "android.intent.action.SEND_MULTIPLE";
            boolean equals = this.mIntent.getAction().equals(str);
            String str2 = "android.intent.extra.STREAM";
            if (!z && equals) {
                this.mIntent.setAction("android.intent.action.SEND");
                ArrayList arrayList5 = this.mStreams;
                if (arrayList5 == null || arrayList5.isEmpty()) {
                    this.mIntent.removeExtra(str2);
                } else {
                    this.mIntent.putExtra(str2, (Parcelable) this.mStreams.get(0));
                }
                this.mStreams = null;
            }
            if (z && !equals) {
                this.mIntent.setAction(str);
                ArrayList arrayList6 = this.mStreams;
                if (arrayList6 == null || arrayList6.isEmpty()) {
                    this.mIntent.removeExtra(str2);
                } else {
                    this.mIntent.putParcelableArrayListExtra(str2, this.mStreams);
                }
            }
            return this.mIntent;
        }

        public IntentBuilder setChooserTitle(@StringRes int i) {
            return setChooserTitle(this.mActivity.getText(i));
        }

        public IntentBuilder setChooserTitle(CharSequence charSequence) {
            this.mChooserTitle = charSequence;
            return this;
        }

        public IntentBuilder setEmailBcc(String[] strArr) {
            this.mIntent.putExtra("android.intent.extra.BCC", strArr);
            return this;
        }

        public IntentBuilder setEmailCc(String[] strArr) {
            this.mIntent.putExtra("android.intent.extra.CC", strArr);
            return this;
        }

        public IntentBuilder setEmailTo(String[] strArr) {
            if (this.mToAddresses != null) {
                this.mToAddresses = null;
            }
            this.mIntent.putExtra("android.intent.extra.EMAIL", strArr);
            return this;
        }

        public IntentBuilder setHtmlText(String str) {
            this.mIntent.putExtra(IntentCompat.EXTRA_HTML_TEXT, str);
            if (!this.mIntent.hasExtra("android.intent.extra.TEXT")) {
                setText(Html.fromHtml(str));
            }
            return this;
        }

        public IntentBuilder setStream(Uri uri) {
            String str = "android.intent.action.SEND";
            if (!this.mIntent.getAction().equals(str)) {
                this.mIntent.setAction(str);
            }
            this.mStreams = null;
            this.mIntent.putExtra("android.intent.extra.STREAM", uri);
            return this;
        }

        public IntentBuilder setSubject(String str) {
            this.mIntent.putExtra("android.intent.extra.SUBJECT", str);
            return this;
        }

        public IntentBuilder setText(CharSequence charSequence) {
            this.mIntent.putExtra("android.intent.extra.TEXT", charSequence);
            return this;
        }

        public IntentBuilder setType(String str) {
            this.mIntent.setType(str);
            return this;
        }

        public void startChooser() {
            this.mActivity.startActivity(createChooserIntent());
        }
    }

    public class IntentReader {
        private static final String TAG = "IntentReader";
        private Activity mActivity;
        private ComponentName mCallingActivity;
        private String mCallingPackage;
        private Intent mIntent;
        private ArrayList mStreams;

        private IntentReader(Activity activity) {
            this.mActivity = activity;
            this.mIntent = activity.getIntent();
            this.mCallingPackage = ShareCompat.getCallingPackage(activity);
            this.mCallingActivity = ShareCompat.getCallingActivity(activity);
        }

        public static IntentReader from(Activity activity) {
            return new IntentReader(activity);
        }

        private static void withinStyle(StringBuilder sb, CharSequence charSequence, int i, int i2) {
            String sb2;
            while (r5 < i2) {
                char charAt = charSequence.charAt(r5);
                if (charAt == '<') {
                    sb2 = "&lt;";
                } else if (charAt == '>') {
                    sb2 = "&gt;";
                } else if (charAt == '&') {
                    sb2 = "&amp;";
                } else if (charAt > '~' || charAt < ' ') {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("&#");
                    sb3.append(charAt);
                    sb3.append(";");
                    sb2 = sb3.toString();
                } else {
                    if (charAt == ' ') {
                        while (true) {
                            int i3 = r5 + 1;
                            if (i3 >= i2 || charSequence.charAt(i3) != ' ') {
                                sb.append(' ');
                            } else {
                                sb.append("&nbsp;");
                                r5 = i3;
                            }
                        }
                        sb.append(' ');
                    } else {
                        sb.append(charAt);
                    }
                    i = r5 + 1;
                }
                sb.append(sb2);
                i = r5 + 1;
            }
        }

        public ComponentName getCallingActivity() {
            return this.mCallingActivity;
        }

        public Drawable getCallingActivityIcon() {
            if (this.mCallingActivity == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getActivityIcon(this.mCallingActivity);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve icon for calling activity", e);
                return null;
            }
        }

        public Drawable getCallingApplicationIcon() {
            if (this.mCallingPackage == null) {
                return null;
            }
            try {
                return this.mActivity.getPackageManager().getApplicationIcon(this.mCallingPackage);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve icon for calling application", e);
                return null;
            }
        }

        public CharSequence getCallingApplicationLabel() {
            if (this.mCallingPackage == null) {
                return null;
            }
            PackageManager packageManager = this.mActivity.getPackageManager();
            try {
                return packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mCallingPackage, 0));
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Could not retrieve label for calling application", e);
                return null;
            }
        }

        public String getCallingPackage() {
            return this.mCallingPackage;
        }

        public String[] getEmailBcc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.BCC");
        }

        public String[] getEmailCc() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.CC");
        }

        public String[] getEmailTo() {
            return this.mIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        }

        public String getHtmlText() {
            String stringExtra = this.mIntent.getStringExtra(IntentCompat.EXTRA_HTML_TEXT);
            if (stringExtra != null) {
                return stringExtra;
            }
            CharSequence text = getText();
            if (text instanceof Spanned) {
                return Html.toHtml((Spanned) text);
            }
            if (text == null) {
                return stringExtra;
            }
            if (VERSION.SDK_INT >= 16) {
                return Html.escapeHtml(text);
            }
            StringBuilder sb = new StringBuilder();
            withinStyle(sb, text, 0, text.length());
            return sb.toString();
        }

        public Uri getStream() {
            return (Uri) this.mIntent.getParcelableExtra("android.intent.extra.STREAM");
        }

        public Uri getStream(int i) {
            Object parcelableExtra;
            String str = "android.intent.extra.STREAM";
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra(str);
            }
            ArrayList arrayList = this.mStreams;
            if (arrayList != null) {
                parcelableExtra = arrayList.get(i);
            } else if (i == 0) {
                parcelableExtra = this.mIntent.getParcelableExtra(str);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Stream items available: ");
                sb.append(getStreamCount());
                sb.append(" index requested: ");
                sb.append(i);
                throw new IndexOutOfBoundsException(sb.toString());
            }
            return (Uri) parcelableExtra;
        }

        public int getStreamCount() {
            String str = "android.intent.extra.STREAM";
            if (this.mStreams == null && isMultipleShare()) {
                this.mStreams = this.mIntent.getParcelableArrayListExtra(str);
            }
            ArrayList arrayList = this.mStreams;
            return arrayList != null ? arrayList.size() : this.mIntent.hasExtra(str) ? 1 : 0;
        }

        public String getSubject() {
            return this.mIntent.getStringExtra("android.intent.extra.SUBJECT");
        }

        public CharSequence getText() {
            return this.mIntent.getCharSequenceExtra("android.intent.extra.TEXT");
        }

        public String getType() {
            return this.mIntent.getType();
        }

        public boolean isMultipleShare() {
            return "android.intent.action.SEND_MULTIPLE".equals(this.mIntent.getAction());
        }

        public boolean isShareIntent() {
            String action = this.mIntent.getAction();
            return "android.intent.action.SEND".equals(action) || "android.intent.action.SEND_MULTIPLE".equals(action);
        }

        public boolean isSingleShare() {
            return "android.intent.action.SEND".equals(this.mIntent.getAction());
        }
    }

    private ShareCompat() {
    }

    public static void configureMenuItem(Menu menu, int i, IntentBuilder intentBuilder) {
        MenuItem findItem = menu.findItem(i);
        if (findItem != null) {
            configureMenuItem(findItem, intentBuilder);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Could not find menu item with id ");
        sb.append(i);
        sb.append(" in the supplied menu");
        throw new IllegalArgumentException(sb.toString());
    }

    public static void configureMenuItem(MenuItem menuItem, IntentBuilder intentBuilder) {
        ActionProvider actionProvider = menuItem.getActionProvider();
        ShareActionProvider shareActionProvider = !(actionProvider instanceof ShareActionProvider) ? new ShareActionProvider(intentBuilder.getActivity()) : (ShareActionProvider) actionProvider;
        StringBuilder sb = new StringBuilder();
        sb.append(HISTORY_FILENAME_PREFIX);
        sb.append(intentBuilder.getActivity().getClass().getName());
        shareActionProvider.setShareHistoryFileName(sb.toString());
        shareActionProvider.setShareIntent(intentBuilder.getIntent());
        menuItem.setActionProvider(shareActionProvider);
        if (VERSION.SDK_INT < 16 && !menuItem.hasSubMenu()) {
            menuItem.setIntent(intentBuilder.createChooserIntent());
        }
    }

    public static ComponentName getCallingActivity(Activity activity) {
        ComponentName callingActivity = activity.getCallingActivity();
        if (callingActivity != null) {
            return callingActivity;
        }
        ComponentName componentName = (ComponentName) activity.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY);
        return componentName == null ? (ComponentName) activity.getIntent().getParcelableExtra(EXTRA_CALLING_ACTIVITY_INTEROP) : componentName;
    }

    public static String getCallingPackage(Activity activity) {
        String callingPackage = activity.getCallingPackage();
        if (callingPackage != null) {
            return callingPackage;
        }
        String stringExtra = activity.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE);
        return stringExtra == null ? activity.getIntent().getStringExtra(EXTRA_CALLING_PACKAGE_INTEROP) : stringExtra;
    }
}
