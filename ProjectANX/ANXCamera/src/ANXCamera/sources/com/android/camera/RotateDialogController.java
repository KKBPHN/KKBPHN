package com.android.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.camera.ui.Rotatable;
import com.android.camera.ui.RotateLayout;
import com.android.camera.ui.ScreenHint;
import com.android.camera2.DetachableClickListener;
import java.util.Locale;

public class RotateDialogController implements Rotatable {
    private static final long ANIM_DURATION = 150;
    private static final String TAG = "RotateDialogController";
    private Activity mActivity;
    private View mDialogRootLayout;
    private Animation mFadeInAnim;
    private Animation mFadeOutAnim;
    private int mLayoutResourceID;
    private RotateLayout mRotateDialog;
    private TextView mRotateDialogButton1;
    private TextView mRotateDialogButton2;
    private View mRotateDialogButtonLayout;
    private ProgressBar mRotateDialogSpinner;
    private TextView mRotateDialogText;
    private TextView mRotateDialogTitle;
    private View mRotateDialogTitleLayout;

    public RotateDialogController(Activity activity, int i) {
        this.mActivity = activity;
        if (i == 0) {
            i = R.layout.rotate_dialog;
        }
        this.mLayoutResourceID = i;
    }

    static /* synthetic */ boolean O00000o0(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return i == 25 || i == 24;
    }

    private void fadeInDialog() {
        this.mDialogRootLayout.startAnimation(this.mFadeInAnim);
        this.mDialogRootLayout.setVisibility(0);
    }

    private void fadeOutDialog() {
        this.mDialogRootLayout.startAnimation(this.mFadeOutAnim);
        this.mDialogRootLayout.setVisibility(8);
    }

    private void inflateDialogLayout() {
        if (this.mDialogRootLayout == null) {
            View inflate = this.mActivity.getLayoutInflater().inflate(this.mLayoutResourceID, (ViewGroup) this.mActivity.getWindow().getDecorView());
            this.mDialogRootLayout = inflate.findViewById(R.id.rotate_dialog_root_layout);
            this.mRotateDialog = (RotateLayout) inflate.findViewById(R.id.rotate_dialog_layout);
            this.mRotateDialogTitleLayout = inflate.findViewById(R.id.rotate_dialog_title_layout);
            this.mRotateDialogButtonLayout = inflate.findViewById(R.id.rotate_dialog_button_layout);
            this.mRotateDialogTitle = (TextView) inflate.findViewById(R.id.rotate_dialog_title);
            this.mRotateDialogSpinner = (ProgressBar) inflate.findViewById(R.id.rotate_dialog_spinner);
            this.mRotateDialogText = (TextView) inflate.findViewById(R.id.rotate_dialog_text);
            this.mRotateDialogButton1 = (Button) inflate.findViewById(R.id.rotate_dialog_button1);
            this.mRotateDialogButton2 = (Button) inflate.findViewById(R.id.rotate_dialog_button2);
            this.mFadeInAnim = AnimationUtils.loadAnimation(this.mActivity, 17432576);
            this.mFadeOutAnim = AnimationUtils.loadAnimation(this.mActivity, 17432577);
            this.mFadeInAnim.setDuration(ANIM_DURATION);
            this.mFadeOutAnim.setDuration(ANIM_DURATION);
        }
    }

    public static AlertDialog showCTADialog(Activity activity, CharSequence charSequence, final Runnable runnable, CharSequence charSequence2, final Runnable runnable2, View view) {
        DetachableClickListener wrap = DetachableClickListener.wrap(new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Runnable runnable;
                if (i == -2) {
                    runnable = runnable2;
                    if (runnable == null) {
                        return;
                    }
                } else if (i == -1) {
                    runnable = runnable;
                    if (runnable == null) {
                        return;
                    }
                } else {
                    return;
                }
                runnable.run();
            }
        }, new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Runnable runnable = runnable2;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        String format = String.format("%s_%s", new Object[]{Locale.getDefault().getLanguage(), Locale.getDefault().getCountry()});
        TextView textView = (TextView) view.findViewById(R.id.tv_cta_user_tips);
        textView.setText(Html.fromHtml(activity.getString(R.string.cta_user_tips, new Object[]{activity.getString(R.string.link_user_agreement, new Object[]{format}), activity.getString(R.string.link_privacy_policy, new Object[]{format})}), 63));
        ScreenHint.setLinkClickEvent(textView, activity);
        Builder builder = new Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        builder.setOnCancelListener(wrap);
        if (charSequence != null) {
            builder.setPositiveButton(charSequence, wrap);
        }
        if (charSequence2 != null) {
            builder.setNegativeButton(charSequence2, wrap);
        }
        AlertDialog create = builder.create();
        create.show();
        wrap.clearOnDetach(create);
        return create;
    }

    public static AlertDialog showLocationDialog(Activity activity, CharSequence charSequence, final Runnable runnable, CharSequence charSequence2, final Runnable runnable2, View view, String str) {
        DetachableClickListener wrap = DetachableClickListener.wrap(new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Runnable runnable;
                if (i != -2) {
                    runnable = runnable;
                    if (runnable == null) {
                        return;
                    }
                } else {
                    runnable = runnable2;
                    if (runnable == null) {
                        return;
                    }
                }
                runnable.run();
            }
        }, new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Runnable runnable = runnable2;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        AlertDialog create = new Builder(activity).setTitle(str).setView(view).setCancelable(true).setOnCancelListener(wrap).setPositiveButton(charSequence, wrap).setNegativeButton(charSequence2, wrap).create();
        create.show();
        return create;
    }

    public static AlertDialog showSystemAlertDialog(Context context, String str, CharSequence charSequence, CharSequence charSequence2, final Runnable runnable, CharSequence charSequence3, final Runnable runnable2, CharSequence charSequence4, final Runnable runnable3) {
        DetachableClickListener wrap = DetachableClickListener.wrap(new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Runnable runnable;
                if (i == -3) {
                    runnable = runnable2;
                    if (runnable == null) {
                        return;
                    }
                } else if (i == -2) {
                    runnable = runnable3;
                    if (runnable == null) {
                        return;
                    }
                } else if (i == -1) {
                    runnable = runnable;
                    if (runnable == null) {
                        return;
                    }
                } else {
                    return;
                }
                runnable.run();
            }
        }, new OnCancelListener() {
            public void onCancel(DialogInterface dialogInterface) {
                Runnable runnable = runnable3;
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        Builder builder = new Builder(context);
        builder.setTitle(str);
        builder.setMessage(charSequence);
        builder.setCancelable(true);
        builder.setOnCancelListener(wrap);
        builder.setOnKeyListener(O0000Oo0.INSTANCE);
        if (charSequence2 != null) {
            builder.setPositiveButton(charSequence2, wrap);
        }
        if (charSequence4 != null) {
            builder.setNegativeButton(charSequence4, wrap);
        }
        if (charSequence3 != null) {
            builder.setNeutralButton(charSequence3, wrap);
        }
        AlertDialog create = builder.create();
        create.show();
        wrap.clearOnDetach(create);
        return create;
    }

    public void dismissDialog() {
        View view = this.mDialogRootLayout;
        if (view != null && view.getVisibility() != 8) {
            fadeOutDialog();
        }
    }

    public void resetRotateDialog() {
        inflateDialogLayout();
        this.mRotateDialogTitleLayout.setVisibility(8);
        this.mRotateDialogSpinner.setVisibility(8);
        this.mRotateDialogButton1.setVisibility(8);
        this.mRotateDialogButton2.setVisibility(8);
        this.mRotateDialogButtonLayout.setVisibility(8);
    }

    public void setOrientation(int i, boolean z) {
        inflateDialogLayout();
        this.mRotateDialog.setOrientation(i, z);
    }

    public void showAlertDialog(String str, String str2, String str3, final Runnable runnable, String str4, final Runnable runnable2) {
        resetRotateDialog();
        if (str != null) {
            this.mRotateDialogTitle.setText(str);
            this.mRotateDialogTitleLayout.setVisibility(0);
        }
        this.mRotateDialogText.setText(str2);
        if (str3 != null) {
            this.mRotateDialogButton1.setText(str3);
            this.mRotateDialogButton1.setContentDescription(str3);
            this.mRotateDialogButton1.setVisibility(0);
            this.mRotateDialogButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Runnable runnable = runnable;
                    if (runnable != null) {
                        runnable.run();
                    }
                    RotateDialogController.this.dismissDialog();
                }
            });
            this.mRotateDialogButtonLayout.setVisibility(0);
        }
        if (str4 != null) {
            this.mRotateDialogButton2.setText(str4);
            this.mRotateDialogButton2.setContentDescription(str4);
            this.mRotateDialogButton2.setVisibility(0);
            this.mRotateDialogButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Runnable runnable = runnable2;
                    if (runnable != null) {
                        runnable.run();
                    }
                    RotateDialogController.this.dismissDialog();
                }
            });
            this.mRotateDialogButtonLayout.setVisibility(0);
        }
        fadeInDialog();
    }
}
