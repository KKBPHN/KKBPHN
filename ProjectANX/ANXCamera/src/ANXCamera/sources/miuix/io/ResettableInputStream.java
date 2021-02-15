package miuix.io;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.AssetManager.AssetInputStream;
import android.net.Uri;
import android.util.Log;
import com.ss.android.ugc.effectmanager.effect.model.ComposerHelper;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResettableInputStream extends InputStream {
    private final AssetManager mAssetManager;
    private final String mAssetPath;
    private final byte[] mByteArray;
    private final Context mContext;
    private final Object mFinalizeGuardian;
    private volatile InputStream mInputStream;
    private IOException mLastException;
    /* access modifiers changed from: private */
    public Throwable mOpenStack;
    private final String mPath;
    private final Type mType;
    private final Uri mUri;

    /* renamed from: miuix.io.ResettableInputStream$2 reason: invalid class name */
    /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$miuix$io$ResettableInputStream$Type = new int[Type.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$miuix$io$ResettableInputStream$Type[Type.Uri.ordinal()] = 1;
            $SwitchMap$miuix$io$ResettableInputStream$Type[Type.File.ordinal()] = 2;
            $SwitchMap$miuix$io$ResettableInputStream$Type[Type.Asset.ordinal()] = 3;
            try {
                $SwitchMap$miuix$io$ResettableInputStream$Type[Type.ByteArray.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    enum Type {
        File,
        Uri,
        Asset,
        ByteArray
    }

    public ResettableInputStream(Context context, Uri uri) {
        this.mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        if (ComposerHelper.COMPOSER_PATH.equals(uri.getScheme())) {
            this.mType = Type.File;
            this.mPath = uri.getPath();
            this.mContext = null;
            this.mUri = null;
        } else {
            this.mType = Type.Uri;
            this.mContext = context;
            this.mUri = uri;
            this.mPath = null;
        }
        this.mAssetManager = null;
        this.mAssetPath = null;
        this.mByteArray = null;
    }

    public ResettableInputStream(AssetManager assetManager, String str) {
        this.mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mType = Type.Asset;
        this.mAssetManager = assetManager;
        this.mAssetPath = str;
        this.mPath = null;
        this.mContext = null;
        this.mUri = null;
        this.mByteArray = null;
    }

    public ResettableInputStream(String str) {
        this.mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mType = Type.File;
        this.mPath = str;
        this.mContext = null;
        this.mUri = null;
        this.mAssetManager = null;
        this.mAssetPath = null;
        this.mByteArray = null;
    }

    public ResettableInputStream(byte[] bArr) {
        this.mFinalizeGuardian = new Object() {
            /* access modifiers changed from: protected */
            public void finalize() {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mType = Type.ByteArray;
        this.mByteArray = bArr;
        this.mPath = null;
        this.mContext = null;
        this.mUri = null;
        this.mAssetManager = null;
        this.mAssetPath = null;
    }

    private void openStream() {
        InputStream openInputStream;
        IOException iOException = this.mLastException;
        if (iOException != null) {
            throw iOException;
        } else if (this.mInputStream == null) {
            synchronized (this.mFinalizeGuardian) {
                if (this.mInputStream == null) {
                    int i = AnonymousClass2.$SwitchMap$miuix$io$ResettableInputStream$Type[this.mType.ordinal()];
                    if (i == 1) {
                        openInputStream = this.mContext.getContentResolver().openInputStream(this.mUri);
                    } else if (i == 2) {
                        openInputStream = new FileInputStream(this.mPath);
                    } else if (i == 3) {
                        openInputStream = this.mAssetManager.open(this.mAssetPath);
                    } else if (i == 4) {
                        openInputStream = new ByteArrayInputStream(this.mByteArray);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Unkown type ");
                        sb.append(this.mType);
                        throw new IllegalStateException(sb.toString());
                    }
                    this.mInputStream = openInputStream;
                    this.mOpenStack = new Throwable();
                }
            }
        }
    }

    public int available() {
        openStream();
        return this.mInputStream.available();
    }

    public void close() {
        if (this.mInputStream != null) {
            synchronized (this.mFinalizeGuardian) {
                if (this.mInputStream != null) {
                    try {
                        this.mInputStream.close();
                    } finally {
                        this.mOpenStack = null;
                        this.mInputStream = null;
                        this.mLastException = null;
                    }
                }
            }
        }
    }

    public void mark(int i) {
        try {
            openStream();
            this.mInputStream.mark(i);
        } catch (IOException e) {
            this.mLastException = e;
        }
    }

    public boolean markSupported() {
        try {
            this = this;
            openStream();
            this = this.mInputStream.markSupported();
            r1 = this;
            return this;
        } catch (IOException e) {
            r1.mLastException = e;
            return super.markSupported();
        }
    }

    public int read() {
        openStream();
        return this.mInputStream.read();
    }

    public int read(byte[] bArr) {
        openStream();
        return this.mInputStream.read(bArr);
    }

    public int read(byte[] bArr, int i, int i2) {
        openStream();
        return this.mInputStream.read(bArr, i, i2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void reset() {
        if (this.mInputStream != null) {
            if (this.mInputStream instanceof FileInputStream) {
                ((FileInputStream) this.mInputStream).getChannel().position(0);
                return;
            }
            if (!(this.mInputStream instanceof AssetInputStream)) {
                if (!(this.mInputStream instanceof ByteArrayInputStream)) {
                    close();
                }
            }
            this.mInputStream.reset();
        }
    }

    public long skip(long j) {
        openStream();
        return this.mInputStream.skip(j);
    }
}
