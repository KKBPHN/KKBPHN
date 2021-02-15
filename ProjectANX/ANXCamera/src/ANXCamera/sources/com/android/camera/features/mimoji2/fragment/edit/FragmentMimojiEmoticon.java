package com.android.camera.features.mimoji2.fragment.edit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.recyclerview.widget.RecyclerView.State;
import com.android.camera.ActivityBase;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.bean.MimojiEmoticonInfo;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiEditor2.MimojiEmoticon;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiVideoEditor;
import com.android.camera.features.mimoji2.widget.baseview.BaseNoScrollGridLayoutManager;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import com.android.camera.features.mimoji2.widget.helper.MimojiHelper2;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.storage.Storage;
import com.arcsoft.avatar2.emoticon.EmoInfo;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miui.app.AlertDialog;
import miui.app.AlertDialog.Builder;
import miui.app.ProgressDialog;

public class FragmentMimojiEmoticon extends BaseFragment implements MimojiEmoticon, OnClickListener, HandleBackTrace {
    private static final int FRAGMENT_INFO = 65522;
    public static final String TAG = "FragmentMimojiEmoticon";
    private static final int THUMBNAIL_TOTAL_COUNT = 6;
    private AlertDialog mAlertDialog;
    private LinearLayout mBottomActionLinearLayout;
    private RecyclerView mEmoticonRecyclerView;
    private boolean mIsBackToPreview;
    private boolean mIsNeedShare;
    /* access modifiers changed from: private */
    public boolean mIsRTL;
    private MimojiEmoticonAdapter mMimojiEmoticonAdapter;
    private ProgressDialog mProgressDialog;
    private TextView mSaveEmoticonBtn;
    private ImageView mSelectBtn;
    private ArrayList mSelectedEmoInfoList = new ArrayList();
    private ArrayList mShareEmoInfoList = new ArrayList();
    private TextView mShareEmoticonBtn;
    private Paint mThumbnailPaint;

    private boolean checkInitThumbnaiFinish() {
        MimojiEmoticonAdapter mimojiEmoticonAdapter = this.mMimojiEmoticonAdapter;
        return mimojiEmoticonAdapter != null && mimojiEmoticonAdapter.getItemCount() == 6;
    }

    private void deleteEmoticonCache() {
        FileUtils.deleteFile(MimojiHelper2.EMOTICON_MP4_CACHE_DIR);
        FileUtils.deleteFile(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
        FileUtils.deleteFile(MimojiHelper2.EMOTICON_JPEG_CACHE_DIR);
    }

    private void dissmissDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mProgressDialog = null;
        }
        AlertDialog alertDialog = this.mAlertDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mAlertDialog = null;
        }
    }

    private void getEmoticonThumbnail() {
        MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
        if (mimojiEditor2 != null) {
            mimojiEditor2.createEmoticonThumbnail();
        } else {
            Log.e(TAG, "mimoji void initEmoticon[] null");
        }
    }

    private void saveEmoticonGif(boolean z) {
        Context context;
        int i;
        this.mSelectedEmoInfoList.clear();
        this.mIsNeedShare = false;
        MimojiEmoticonAdapter mimojiEmoticonAdapter = this.mMimojiEmoticonAdapter;
        if (mimojiEmoticonAdapter == null || mimojiEmoticonAdapter.getItemCount() != 0) {
            for (MimojiEmoticonInfo mimojiEmoticonInfo : this.mMimojiEmoticonAdapter.getDataList()) {
                if (mimojiEmoticonInfo.isSelected()) {
                    this.mSelectedEmoInfoList.add(mimojiEmoticonInfo.getEmoInfo());
                }
            }
            if (this.mSelectedEmoInfoList.size() == 0) {
                context = getContext();
                i = R.string.mimoji_select_number_error;
            } else {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.mSelectedEmoInfoList);
                File file = new File(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
                if (file.exists() && file.isDirectory()) {
                    for (File absolutePath : file.listFiles()) {
                        String fileName = FileUtils.getFileName(absolutePath.getAbsolutePath());
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            if (((EmoInfo) it.next()).getEmoName().equals(fileName)) {
                                it.remove();
                            }
                        }
                    }
                }
                MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
                if (mimojiEditor2 != null) {
                    if (arrayList.size() != 0) {
                        showProgressDialog(getString(R.string.mimoji_create_emoticon_progress), 0);
                        if (!z) {
                            mimojiEditor2.createEmoticonVideo(arrayList);
                        }
                    } else if (!z) {
                        coverEmoticonSuccess();
                    }
                    mimojiEditor2.createEmoticonPicture(arrayList);
                } else {
                    Log.e(TAG, "mimoji void saveEmoticonGif[] null");
                }
                return;
            }
        } else {
            backToPreview(false);
            context = getContext();
            i = R.string.unknow_error;
        }
        ToastUtils.showToast(context, i, 80);
    }

    @SuppressLint({"StringFormatInvalid"})
    private void shareEmoticonGif() {
        this.mSelectedEmoInfoList.clear();
        this.mShareEmoInfoList.clear();
        this.mIsNeedShare = true;
        this.mIsBackToPreview = true;
        MimojiEmoticonAdapter mimojiEmoticonAdapter = this.mMimojiEmoticonAdapter;
        if (mimojiEmoticonAdapter == null || mimojiEmoticonAdapter.getItemCount() != 0) {
            for (MimojiEmoticonInfo mimojiEmoticonInfo : this.mMimojiEmoticonAdapter.getDataList()) {
                if (mimojiEmoticonInfo.isSelected()) {
                    this.mSelectedEmoInfoList.add(mimojiEmoticonInfo.getEmoInfo());
                }
            }
            if (this.mSelectedEmoInfoList.size() == 0) {
                ToastUtils.showToast(getContext(), String.format(getResources().getString(R.string.mimoji_select_number_error), new Object[]{Integer.valueOf(1)}), 80);
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.mSelectedEmoInfoList);
            File file = new File(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
            if (file.exists() && file.isDirectory()) {
                for (File absolutePath : file.listFiles()) {
                    String fileName = FileUtils.getFileName(absolutePath.getAbsolutePath());
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        if (((EmoInfo) it.next()).getEmoName().equals(fileName)) {
                            it.remove();
                        }
                    }
                }
            }
            if (arrayList.size() == 0) {
                coverEmoticonSuccess();
                return;
            }
            MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
            if (mimojiEditor2 != null) {
                showProgressDialog(getString(R.string.mimoji_create_emoticon_progress), 0);
                mimojiEditor2.createEmoticonVideo(arrayList);
            }
            return;
        }
        backToPreview(false);
        ToastUtils.showToast(getContext(), (int) R.string.unknow_error, 80);
    }

    private void showBackDialog() {
        if (getActivity() != null) {
            dissmissDialog();
            Builder builder = new Builder(getActivity());
            builder.setTitle((int) R.string.mimoji_edit_abandon_alert);
            builder.setCancelable(true);
            builder.setPositiveButton((int) R.string.mimoji_confirm, (DialogInterface.OnClickListener) new O0000o00(this));
            builder.setNegativeButton((int) R.string.mimoji_cancle, (DialogInterface.OnClickListener) new C0268O0000oOO(this));
            this.mAlertDialog = builder.show();
        }
    }

    private void showProgressDialog(String str, int i) {
        if (getActivity() != null) {
            if (TextUtils.isEmpty(str) || i < 0) {
                dissmissDialog();
                return;
            }
            if (this.mProgressDialog == null) {
                this.mProgressDialog = new ProgressDialog(getActivity());
                this.mProgressDialog.setProgressStyle(1);
                this.mProgressDialog.setCancelable(false);
                this.mProgressDialog.setMax(100);
                this.mProgressDialog.setOnKeyListener(new O0000o(this));
            }
            this.mProgressDialog.setMessage(str);
            this.mProgressDialog.setProgress(i);
            ProgressDialog progressDialog = this.mProgressDialog;
            if (progressDialog != null && !progressDialog.isShowing()) {
                this.mProgressDialog.show();
            }
        }
    }

    private void showSaveDialog() {
        if (getActivity() != null) {
            dissmissDialog();
            Builder builder = new Builder(getActivity());
            builder.setTitle((int) R.string.mimoji_emoticon_save);
            builder.setCancelable(true);
            builder.setMessage((int) R.string.mimoji_save_gif_emoticon);
            builder.setCheckBox(true, getString(R.string.mimoji_emoticon_with_jpeg_save));
            builder.setPositiveButton((int) R.string.mimoji_save, (DialogInterface.OnClickListener) new C0267O0000oO0(this));
            builder.setNegativeButton((int) R.string.mimoji_cancle, (DialogInterface.OnClickListener) new O0000Oo(this));
            this.mAlertDialog = builder.show();
        }
    }

    public /* synthetic */ void O000000o(MimojiEmoticonInfo mimojiEmoticonInfo, int i, View view) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("OnRecyclerItemClick position=");
        sb.append(i);
        Log.u(str, sb.toString());
        mimojiEmoticonInfo.setSelected(!mimojiEmoticonInfo.isSelected());
        this.mMimojiEmoticonAdapter.setSelectState(mimojiEmoticonInfo, i);
    }

    public /* synthetic */ void O000000o(EmoInfo emoInfo, int i, int i2) {
        if (this.mThumbnailPaint == null) {
            this.mThumbnailPaint = new Paint();
            this.mThumbnailPaint.setAntiAlias(true);
        }
        String str = "mimoji void updateEmoticonThumbnailProgress[num, emoInfo]";
        Log.d(TAG, str);
        Bitmap bitmap = null;
        if (emoInfo.getThumbnailData() != null) {
            Bitmap createBitmap = Bitmap.createBitmap(AvatarEngineManager2.CONFIG_EMO_THUM_SIZE.getWidth(), AvatarEngineManager2.CONFIG_EMO_THUM_SIZE.getHeight(), Config.ARGB_8888);
            createBitmap.copyPixelsFromBuffer(ByteBuffer.wrap(emoInfo.getThumbnailData()));
            if (createBitmap != null) {
                bitmap = Util.getRoundedCornerBitmap(createBitmap, 20.0f);
            } else {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(i);
                sb.append(" , ");
                sb.append(emoInfo.getEmoName());
                Log.e(str2, sb.toString());
            }
        }
        if (bitmap == null || bitmap.isRecycled()) {
            Log.e(TAG, "mimoji thumbnail null");
        } else if (this.mMimojiEmoticonAdapter != null) {
            MimojiEmoticonInfo mimojiEmoticonInfo = new MimojiEmoticonInfo(emoInfo, bitmap, i2);
            mimojiEmoticonInfo.setSelected(true);
            this.mMimojiEmoticonAdapter.addData(mimojiEmoticonInfo);
        }
    }

    public /* synthetic */ boolean O00000Oo(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4) {
            MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
            if (mimojiEditor2 != null) {
                mimojiEditor2.quitCoverEmoticon();
                dissmissDialog();
            } else {
                Log.e(TAG, "mimoji void saveEmoticonGif[] mimojiEditor2 null");
            }
            MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
            if (mimojiVideoEditor != null) {
                mimojiVideoEditor.cancelVideo2gif();
            } else {
                Log.e(TAG, "mimoji void saveEmoticonGif[] mimojiVideoEditor null");
            }
        }
        return true;
    }

    public /* synthetic */ void O0000O0o(DialogInterface dialogInterface, int i) {
        backToPreview(false);
    }

    public /* synthetic */ void O0000OOo(DialogInterface dialogInterface, int i) {
        dissmissDialog();
    }

    public /* synthetic */ void O0000Oo(DialogInterface dialogInterface, int i) {
        Log.u(TAG, "showSaveDialog onClick negative");
        dissmissDialog();
    }

    public /* synthetic */ void O0000Oo0(int i) {
        if (i != 0) {
            showProgressDialog(getString(R.string.mimoji_create_emoticon_progress), ((6 - i) * 100) / 6);
            return;
        }
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor != null) {
            mimojiVideoEditor.video2gif(this.mSelectedEmoInfoList);
        }
    }

    public /* synthetic */ void O0000Oo0(DialogInterface dialogInterface, int i) {
        AlertDialog alertDialog = this.mAlertDialog;
        boolean z = alertDialog != null && alertDialog.isChecked();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("showSaveDialog onClick positive, isChecked=");
        sb.append(z);
        Log.u(str, sb.toString());
        saveEmoticonGif(z);
    }

    public /* synthetic */ void O0000Ooo(boolean z) {
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(19);
        }
        if (this.mIsBackToPreview) {
            MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
            if (mimojiEditor2 != null) {
                mimojiEditor2.quitAndSaveEdit(false);
                return;
            }
            MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
            if (mimojiAvatarEngine2 != null) {
                mimojiAvatarEngine2.backToPreview(z, false);
                return;
            }
            return;
        }
        DataRepository.dataItemLive().getMimojiStatusManager2().setMode(6);
        MimojiEditor2 mimojiEditor22 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
        if (mimojiEditor22 != null) {
            mimojiEditor22.reInitMimojiEdit();
        }
    }

    public /* synthetic */ void O0000o00(boolean z) {
        this.mSelectBtn.setImageResource(z ? R.drawable.bg_mimoji_btn_emoticon_all_selected : R.drawable.bg_mimoji_btn_emoticon_all_unselected);
    }

    public /* synthetic */ void O000o0() {
        this.mSelectBtn.setContentDescription(getString(R.string.accessibility_deselect_all));
        if (Util.isAccessible() && isAdded()) {
            this.mSelectBtn.announceForAccessibility(getString(R.string.accessibility_select_all_end));
        }
    }

    public /* synthetic */ void O000o00O() {
        dissmissDialog();
        ToastUtils.showToast((Context) getActivity(), (int) R.string.mimoji_create_emoticon_error, 80);
    }

    public /* synthetic */ void O000o00o() {
        this.mSelectBtn.setContentDescription(getString(R.string.accessibility_select_all));
        if (Util.isAccessible() && isAdded()) {
            this.mSelectBtn.announceForAccessibility(getString(R.string.accessibility_deselect_all_end));
        }
    }

    public void backToPreview(boolean z) {
        deleteEmoticonCache();
        MimojiVideoEditor mimojiVideoEditor = (MimojiVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(252);
        if (mimojiVideoEditor != null) {
            mimojiVideoEditor.cancelVideo2gif();
        }
        getActivity().runOnUiThread(new C0269O0000oOo(this, z));
    }

    public void coverEmoticonError() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C0271O0000oo0(this));
        }
    }

    public void coverEmoticonSuccess() {
        File[] listFiles;
        Intent intent;
        Object obj;
        StringBuilder sb;
        String str;
        String str2 = "Current video URI: ";
        File file = new File(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
        if (!file.exists() || !file.isDirectory()) {
            ToastUtils.showToast(getContext(), (int) R.string.unknow_error, 80);
            backToPreview(false);
            return;
        }
        String str3 = ".gif";
        String str4 = "_";
        String str5 = "MIMOJI_GIF_";
        int i = 300;
        if (this.mIsNeedShare) {
            File[] listFiles2 = file.listFiles();
            int length = listFiles2.length;
            int i2 = 0;
            while (i2 < length) {
                File file2 = listFiles2[i2];
                String fileName = FileUtils.getFileName(file2.getAbsolutePath());
                Iterator it = this.mSelectedEmoInfoList.iterator();
                while (it.hasNext()) {
                    if (((EmoInfo) it.next()).getEmoName().equals(fileName)) {
                        try {
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(Storage.DIRECTORY);
                            sb2.append(File.separator);
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str5);
                            sb3.append(fileName);
                            sb3.append(str4);
                            sb2.append(FileUtils.createtFileName(sb3.toString(), str3));
                            String sb4 = sb2.toString();
                            FileUtils.copyFile(file2, new File(sb4));
                            obj = ((ActivityBase) getActivity()).getImageSaver().addGifSync(sb4, i, i);
                            try {
                                String str6 = TAG;
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append("mimoji void shareEmoticonGif[] f.getAbsolutePath() : ");
                                sb5.append(file2.getAbsolutePath());
                                sb5.append(" \n  ");
                                sb5.append(file2.getPath());
                                sb5.append("   ");
                                sb5.append(file2.getCanonicalPath());
                                Log.d(str6, sb5.toString());
                                if (obj != null) {
                                    this.mShareEmoInfoList.add(obj);
                                }
                                str = TAG;
                                sb = new StringBuilder();
                            } catch (Exception e) {
                                e = e;
                                try {
                                    Log.e(TAG, "failed to add video to media store", (Throwable) e);
                                    str = TAG;
                                    sb = new StringBuilder();
                                    sb.append(str2);
                                    sb.append(obj);
                                    Log.d(str, sb.toString());
                                    i = 300;
                                } catch (Throwable th) {
                                    th = th;
                                    String str7 = TAG;
                                    StringBuilder sb6 = new StringBuilder();
                                    sb6.append(str2);
                                    sb6.append(obj);
                                    Log.d(str7, sb6.toString());
                                    throw th;
                                }
                            }
                        } catch (Exception e2) {
                            e = e2;
                            Object obj2 = null;
                            Log.e(TAG, "failed to add video to media store", (Throwable) e);
                            str = TAG;
                            sb = new StringBuilder();
                            sb.append(str2);
                            sb.append(obj);
                            Log.d(str, sb.toString());
                            i = 300;
                        } catch (Throwable th2) {
                            th = th2;
                            obj = null;
                            String str72 = TAG;
                            StringBuilder sb62 = new StringBuilder();
                            sb62.append(str2);
                            sb62.append(obj);
                            Log.d(str72, sb62.toString());
                            throw th;
                        }
                        sb.append(str2);
                        sb.append(obj);
                        Log.d(str, sb.toString());
                    }
                    i = 300;
                }
                i2++;
                i = 300;
            }
            if (this.mShareEmoInfoList.size() == 0) {
                ToastUtils.showToast(getContext(), (int) R.string.unknow_error, 80);
                return;
            }
            int size = this.mShareEmoInfoList.size();
            String str8 = Storage.MIME_GIF;
            String str9 = "android.intent.extra.STREAM";
            if (size == 1) {
                intent = new Intent("android.intent.action.SEND");
                intent.putExtra(str9, (Parcelable) this.mShareEmoInfoList.get(0));
            } else {
                intent = new Intent("android.intent.action.SEND_MULTIPLE");
                intent.putParcelableArrayListExtra(str9, this.mShareEmoInfoList);
            }
            intent.setType(str8);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            deleteEmoticonCache();
            dissmissDialog();
        } else {
            for (File file3 : file.listFiles()) {
                String fileName2 = FileUtils.getFileName(file3.getAbsolutePath());
                Iterator it2 = this.mSelectedEmoInfoList.iterator();
                while (it2.hasNext()) {
                    if (((EmoInfo) it2.next()).getEmoName().equals(fileName2)) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append(Storage.DIRECTORY);
                        sb7.append(File.separator);
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str5);
                        sb8.append(fileName2);
                        sb8.append(str4);
                        sb7.append(FileUtils.createtFileName(sb8.toString(), str3));
                        String sb9 = sb7.toString();
                        try {
                            FileUtils.copyFile(file3, new File(sb9));
                            try {
                                ((ActivityBase) getActivity()).getImageSaver().addGif(sb9, 300, 300);
                            } catch (IOException e3) {
                                e = e3;
                                e.printStackTrace();
                            }
                        } catch (IOException e4) {
                            e = e4;
                            e.printStackTrace();
                        }
                    }
                }
            }
            ToastUtils.showToast((Context) getActivity(), (int) R.string.mimoji_save_success, 80);
            this.mIsBackToPreview = true;
            backToPreview(true);
        }
    }

    public int getFragmentInto() {
        return 65522;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_mimoji_emoticon;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mSaveEmoticonBtn = (TextView) view.findViewById(R.id.btn_save_emoticon);
        this.mSaveEmoticonBtn.setOnClickListener(this);
        this.mShareEmoticonBtn = (TextView) view.findViewById(R.id.btn_share_emoticon);
        this.mShareEmoticonBtn.setOnClickListener(this);
        view.findViewById(R.id.btn_back).setOnClickListener(this);
        FolmeUtils.touchButtonTint(R.color.mimoji_btn_pressed_bg, this.mSaveEmoticonBtn, this.mShareEmoticonBtn);
        this.mBottomActionLinearLayout = (LinearLayout) view.findViewById(R.id.ll_bottom_action);
        LayoutParams layoutParams = (LayoutParams) this.mBottomActionLinearLayout.getLayoutParams();
        layoutParams.bottomMargin = Display.getNavigationBarHeight() + getResources().getDimensionPixelOffset(R.dimen.mimoji_emoticon_margin_bottom_action);
        this.mBottomActionLinearLayout.setLayoutParams(layoutParams);
        this.mSelectBtn = (ImageView) view.findViewById(R.id.btn_select_all);
        this.mSelectBtn.setOnClickListener(this);
        this.mEmoticonRecyclerView = (RecyclerView) view.findViewById(R.id.rv_emoticon);
        this.mIsRTL = Util.isLayoutRTL(getContext());
        deleteEmoticonCache();
        if (this.mMimojiEmoticonAdapter == null) {
            this.mMimojiEmoticonAdapter = new MimojiEmoticonAdapter(null);
            this.mEmoticonRecyclerView.setLayoutManager(new BaseNoScrollGridLayoutManager(getContext(), 2));
            this.mEmoticonRecyclerView.getItemAnimator().setChangeDuration(0);
            this.mEmoticonRecyclerView.addItemDecoration(new ItemDecoration() {
                final int margin = FragmentMimojiEmoticon.this.getResources().getDimensionPixelSize(R.dimen.mimoji_emoticon_offset);

                public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, State state) {
                    int childAdapterPosition = recyclerView.getChildAdapterPosition(view) % 2;
                    if (FragmentMimojiEmoticon.this.mIsRTL) {
                        if (childAdapterPosition == 0) {
                            rect.set(this.margin, 0, 0, 0);
                        }
                    } else if (childAdapterPosition == 0) {
                        rect.set(0, 0, this.margin, 0);
                    }
                }
            });
            this.mEmoticonRecyclerView.setAdapter(this.mMimojiEmoticonAdapter);
        }
        this.mMimojiEmoticonAdapter.setOnRecyclerItemClickListener(new C0265O0000o0o(this));
        this.mMimojiEmoticonAdapter.setOnAllSelectStateChangeListener(new C0263O0000Ooo(this));
        getEmoticonThumbnail();
    }

    public boolean onBackEvent(int i) {
        if (i != 1) {
            return false;
        }
        showBackDialog();
        return true;
    }

    public void onClick(View view) {
        ImageView imageView;
        Runnable runnable;
        switch (view.getId()) {
            case R.id.btn_back /*2131296393*/:
                Log.u(TAG, "onClick: btn_back");
                backToPreview(false);
                break;
            case R.id.btn_save_emoticon /*2131296396*/:
                Log.u(TAG, "onClick: btn_save_emoticon");
                if (checkInitThumbnaiFinish()) {
                    showSaveDialog();
                    break;
                } else {
                    return;
                }
            case R.id.btn_select_all /*2131296397*/:
                if (checkInitThumbnaiFinish()) {
                    boolean isAllSelected = this.mMimojiEmoticonAdapter.getIsAllSelected();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("onClick: btn_select_all, isAllSelected=");
                    sb.append(isAllSelected);
                    Log.u(str, sb.toString());
                    if (isAllSelected) {
                        this.mMimojiEmoticonAdapter.clearState();
                        imageView = this.mSelectBtn;
                        runnable = new C0264O0000o0O(this);
                    } else {
                        this.mMimojiEmoticonAdapter.selectAll();
                        imageView = this.mSelectBtn;
                        runnable = new C0266O0000oO(this);
                    }
                    imageView.postDelayed(runnable, 100);
                    break;
                } else {
                    return;
                }
            case R.id.btn_share_emoticon /*2131296398*/:
                Log.u(TAG, "onClick: btn_share_emoticon");
                if (checkInitThumbnaiFinish()) {
                    shareEmoticonGif();
                    break;
                } else {
                    return;
                }
        }
    }

    public void onStop() {
        super.onStop();
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("provideAnimateElement, animateInElements");
        sb.append(list);
        sb.append("resetType = ");
        sb.append(i2);
        Log.d(str, sb.toString());
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(250, this);
    }

    public void release() {
        MimojiEmoticonAdapter mimojiEmoticonAdapter = this.mMimojiEmoticonAdapter;
        if (mimojiEmoticonAdapter != null && mimojiEmoticonAdapter.getItemCount() > 0) {
            Iterator it = ((ArrayList) this.mMimojiEmoticonAdapter.getDataList()).iterator();
            while (it.hasNext()) {
                MimojiEmoticonInfo mimojiEmoticonInfo = (MimojiEmoticonInfo) it.next();
                if (mimojiEmoticonInfo.getBitmap() != null && !mimojiEmoticonInfo.getBitmap().isRecycled()) {
                    mimojiEmoticonInfo.getBitmap().recycle();
                }
                mimojiEmoticonInfo.setBitmap(null);
            }
        }
        dissmissDialog();
        this.mMimojiEmoticonAdapter = null;
        AvatarEngineManager2.getInstance().setEmoManager(null);
    }

    public void setIsBackToPreview(boolean z) {
        this.mIsBackToPreview = z;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(250, this);
        release();
    }

    public void updateEmoticonGifProgress(int i) {
        if (getContext() != null) {
            ((Activity) getContext()).runOnUiThread(new O0000o0(this, i));
        }
    }

    public void updateEmoticonPictureProgress(String str, EmoInfo emoInfo, boolean z) {
        if (getActivity() != null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji void updateEmoticonPictureProgress[path, emoInfo, isFinal]: ");
            sb.append(str);
            Log.d(str2, sb.toString());
            if (z) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.mSelectedEmoInfoList);
                File file = new File(MimojiHelper2.EMOTICON_GIF_CACHE_DIR);
                if (file.exists() && file.isDirectory()) {
                    for (File absolutePath : file.listFiles()) {
                        String fileName = FileUtils.getFileName(absolutePath.getAbsolutePath());
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            if (((EmoInfo) it.next()).getEmoName().equals(fileName)) {
                                it.remove();
                            }
                        }
                    }
                }
                MimojiEditor2 mimojiEditor2 = (MimojiEditor2) ModeCoordinatorImpl.getInstance().getAttachProtocol(247);
                if (mimojiEditor2 != null) {
                    mimojiEditor2.createEmoticonVideo(arrayList);
                } else {
                    Log.e(TAG, "mimoji void saveEmoticonGif[] null");
                }
            }
        }
    }

    public void updateEmoticonThumbnailProgress(int i, EmoInfo emoInfo, int i2) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C0262O0000OoO(this, emoInfo, i, i2));
        }
    }
}
