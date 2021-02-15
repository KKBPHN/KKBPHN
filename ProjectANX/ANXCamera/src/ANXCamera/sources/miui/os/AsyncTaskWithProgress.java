package miui.os;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import java.util.HashMap;
import miui.app.ProgressDialog;

public abstract class AsyncTaskWithProgress extends AsyncTask {
    /* access modifiers changed from: private */
    public static final HashMap sAllTasks = new HashMap();
    /* access modifiers changed from: private */
    public boolean mCancelable = false;
    /* access modifiers changed from: private */
    public int mCurrentProgress = 0;
    /* access modifiers changed from: private */
    public volatile ProgressDialogFragment mFragment = null;
    private final FragmentManager mFragmentManager;
    /* access modifiers changed from: private */
    public boolean mIndeterminate = false;
    /* access modifiers changed from: private */
    public final Listeners mListeners = new Listeners();
    /* access modifiers changed from: private */
    public int mMaxProgress = 0;
    /* access modifiers changed from: private */
    public CharSequence mMessage = null;
    /* access modifiers changed from: private */
    public int mMessageId = 0;
    /* access modifiers changed from: private */
    public int mProgressStyle = 0;
    /* access modifiers changed from: private */
    public int mTheme = 0;
    /* access modifiers changed from: private */
    public CharSequence mTitle = null;
    /* access modifiers changed from: private */
    public int mTitleId = 0;

    class Listeners implements OnClickListener, OnCancelListener {
        private Listeners() {
        }

        public void onCancel(DialogInterface dialogInterface) {
            onClick(dialogInterface, -2);
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            if (AsyncTaskWithProgress.this.mFragment != null) {
                Dialog dialog = AsyncTaskWithProgress.this.mFragment.getDialog();
                if (dialog != null && dialogInterface == dialog && i == -2) {
                    AsyncTaskWithProgress.this.cancel(true);
                }
            }
        }
    }

    public class ProgressDialogFragment extends DialogFragment {
        private AsyncTaskWithProgress mTask;

        static ProgressDialogFragment newInstance(String str) {
            ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString("task", str);
            progressDialogFragment.setArguments(bundle);
            return progressDialogFragment;
        }

        public void onCancel(DialogInterface dialogInterface) {
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null && asyncTaskWithProgress.mCancelable) {
                this.mTask.mListeners.onCancel(dialogInterface);
            }
            super.onCancel(dialogInterface);
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            this.mTask = (AsyncTaskWithProgress) AsyncTaskWithProgress.sAllTasks.get(getArguments().getString("task"));
            if (this.mTask == null) {
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                beginTransaction.remove(this);
                beginTransaction.commit();
            }
        }

        public Dialog onCreateDialog(Bundle bundle) {
            boolean z;
            if (this.mTask == null) {
                return super.onCreateDialog(bundle);
            }
            ProgressDialog progressDialog = new ProgressDialog(getActivity(), this.mTask.mTheme);
            if (this.mTask.mTitleId != 0) {
                progressDialog.setTitle(this.mTask.mTitleId);
            } else {
                progressDialog.setTitle(this.mTask.mTitle);
            }
            progressDialog.setMessage(this.mTask.mMessageId != 0 ? getActivity().getText(this.mTask.mMessageId) : this.mTask.mMessage);
            progressDialog.setProgressStyle(this.mTask.mProgressStyle);
            progressDialog.setIndeterminate(this.mTask.mIndeterminate);
            if (!this.mTask.mIndeterminate) {
                progressDialog.setMax(this.mTask.mMaxProgress);
                progressDialog.setProgress(this.mTask.mCurrentProgress);
            }
            if (this.mTask.mCancelable) {
                progressDialog.setButton(-2, progressDialog.getContext().getText(17039360), (OnClickListener) this.mTask.mListeners);
                z = true;
            } else {
                progressDialog.setButton(-2, (CharSequence) null, (OnClickListener) null);
                z = false;
            }
            progressDialog.setCancelable(z);
            return progressDialog;
        }

        public void onStart() {
            super.onStart();
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null) {
                asyncTaskWithProgress.mFragment = this;
            }
        }

        public void onStop() {
            AsyncTaskWithProgress asyncTaskWithProgress = this.mTask;
            if (asyncTaskWithProgress != null) {
                asyncTaskWithProgress.mFragment = null;
            }
            super.onStop();
        }

        /* access modifiers changed from: 0000 */
        public void setProgress(int i) {
            Dialog dialog = getDialog();
            if (dialog instanceof ProgressDialog) {
                ((ProgressDialog) dialog).setProgress(i);
            }
        }
    }

    public AsyncTaskWithProgress(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    private void dismissDialog() {
        FragmentManager fragmentManager = this.mFragmentManager;
        StringBuilder sb = new StringBuilder();
        sb.append("AsyncTaskWithProgress@");
        sb.append(hashCode());
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(sb.toString());
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissAllowingStateLoss();
        }
    }

    public Activity getActivity() {
        if (this.mFragment != null) {
            return this.mFragment.getActivity();
        }
        return null;
    }

    public void onCancelled() {
        HashMap hashMap = sAllTasks;
        StringBuilder sb = new StringBuilder();
        sb.append("AsyncTaskWithProgress@");
        sb.append(hashCode());
        hashMap.remove(sb.toString());
        dismissDialog();
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Object obj) {
        HashMap hashMap = sAllTasks;
        StringBuilder sb = new StringBuilder();
        sb.append("AsyncTaskWithProgress@");
        sb.append(hashCode());
        hashMap.remove(sb.toString());
        dismissDialog();
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        StringBuilder sb = new StringBuilder();
        sb.append("AsyncTaskWithProgress@");
        sb.append(hashCode());
        String sb2 = sb.toString();
        sAllTasks.put(sb2, this);
        if (this.mFragmentManager != null) {
            this.mFragment = ProgressDialogFragment.newInstance(sb2);
            this.mFragment.setCancelable(this.mCancelable);
            this.mFragment.show(this.mFragmentManager, sb2);
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(Integer... numArr) {
        super.onProgressUpdate(numArr);
        this.mCurrentProgress = numArr[0].intValue();
        if (this.mFragment != null) {
            this.mFragment.setProgress(this.mCurrentProgress);
        }
    }

    public AsyncTaskWithProgress setCancelable(boolean z) {
        this.mCancelable = z;
        return this;
    }

    public AsyncTaskWithProgress setIndeterminate(boolean z) {
        this.mIndeterminate = z;
        return this;
    }

    public AsyncTaskWithProgress setMaxProgress(int i) {
        this.mMaxProgress = i;
        return this;
    }

    public AsyncTaskWithProgress setMessage(int i) {
        this.mMessageId = i;
        this.mMessage = null;
        return this;
    }

    public AsyncTaskWithProgress setMessage(CharSequence charSequence) {
        this.mMessageId = 0;
        this.mMessage = charSequence;
        return this;
    }

    public AsyncTaskWithProgress setProgressStyle(int i) {
        this.mProgressStyle = i;
        return this;
    }

    public AsyncTaskWithProgress setTheme(int i) {
        this.mTheme = i;
        return this;
    }

    public AsyncTaskWithProgress setTitle(int i) {
        this.mTitleId = i;
        this.mTitle = null;
        return this;
    }

    public AsyncTaskWithProgress setTitle(CharSequence charSequence) {
        this.mTitleId = 0;
        this.mTitle = charSequence;
        return this;
    }
}
