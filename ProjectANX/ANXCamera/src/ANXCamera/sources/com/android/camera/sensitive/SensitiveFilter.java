package com.android.camera.sensitive;

import android.text.TextUtils;
import android.util.Base64;
import com.android.camera.log.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.zip.ZipException;
import miui.util.IOUtils;

public class SensitiveFilter implements Serializable {
    public static final String CLOUD_FILE_PATH = "/data/data/com.android.camera/sensi_words";
    static final int DEFAULT_INITIAL_CAPACITY = 8388608;
    public static final String LOCAL_FILE_PATH = "/data/data/com.android.camera/";
    private static SensitiveFilter sensitiveFilter = null;
    private static final long serialVersionUID = 1;
    protected SensitiveNode[] nodes = new SensitiveNode[8388608];

    /* JADX WARNING: type inference failed for: r5v0, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r5v1, types: [java.io.InputStreamReader] */
    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v4 */
    /* JADX WARNING: type inference failed for: r5v3 */
    /* JADX WARNING: type inference failed for: r5v7 */
    /* JADX WARNING: type inference failed for: r5v9 */
    /* JADX WARNING: type inference failed for: r5v10 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009c A[SYNTHETIC, Splitter:B:44:0x009c] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00a4 A[Catch:{ IOException -> 0x00a0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x00a9 A[Catch:{ IOException -> 0x00a0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00ae A[Catch:{ IOException -> 0x00a0 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00b9 A[SYNTHETIC, Splitter:B:57:0x00b9] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00c1 A[Catch:{ IOException -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:64:0x00c6 A[Catch:{ IOException -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x00cb A[Catch:{ IOException -> 0x00bd }] */
    /* JADX WARNING: Removed duplicated region for block: B:72:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private SensitiveFilter() {
        ? r5;
        ByteArrayInputStream byteArrayInputStream;
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        ByteArrayInputStream byteArrayInputStream2;
        ByteArrayInputStream byteArrayInputStream3;
        ByteArrayInputStream byteArrayInputStream4;
        BufferedReader bufferedReader;
        String str = "IOException in SensitiveFilter constructor finally";
        String str2 = "SensitiveFilter";
        BufferedReader bufferedReader2 = null;
        try {
            File file = new File(CLOUD_FILE_PATH);
            inputStream = file.exists() ? new FileInputStream(file) : getClass().getResourceAsStream("/assets/sensi/sensi_words.txt");
            try {
                byteArrayInputStream = new ByteArrayInputStream(new String(Base64.decode(IOUtils.toString(inputStream), 0)).getBytes());
            } catch (IOException e) {
                e = e;
                byteArrayInputStream3 = 0;
                inputStreamReader = byteArrayInputStream3;
                byteArrayInputStream2 = byteArrayInputStream3;
                try {
                    Log.e(str2, "IOException in SensitiveFilter constructor", (Throwable) e);
                    if (bufferedReader2 != null) {
                    }
                    if (inputStreamReader != 0) {
                    }
                    if (byteArrayInputStream2 != 0) {
                    }
                    if (inputStream != null) {
                    }
                } catch (Throwable th) {
                    th = th;
                    r5 = inputStreamReader;
                    byteArrayInputStream = byteArrayInputStream2;
                    if (bufferedReader2 != null) {
                        try {
                            bufferedReader2.close();
                        } catch (IOException e2) {
                            Log.e(str2, str, (Throwable) e2);
                            throw th;
                        }
                    }
                    if (r5 != 0) {
                        r5.close();
                    }
                    if (byteArrayInputStream != 0) {
                        byteArrayInputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                byteArrayInputStream4 = 0;
                r5 = byteArrayInputStream2;
                byteArrayInputStream = byteArrayInputStream2;
                if (bufferedReader2 != null) {
                }
                if (r5 != 0) {
                }
                if (byteArrayInputStream != 0) {
                }
                if (inputStream != null) {
                }
                throw th;
            }
            try {
                InputStreamReader inputStreamReader2 = new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8);
                try {
                    bufferedReader = new BufferedReader(inputStreamReader2);
                    try {
                        String readLine = bufferedReader.readLine();
                        while (readLine != null) {
                            readLine = bufferedReader.readLine();
                            put(readLine);
                        }
                        bufferedReader.close();
                        inputStreamReader2.close();
                        byteArrayInputStream.close();
                        inputStream.close();
                    } catch (IOException e3) {
                        e = e3;
                        bufferedReader2 = bufferedReader;
                        Log.e(str2, "IOException in SensitiveFilter constructor", (Throwable) e);
                        if (bufferedReader2 != null) {
                        }
                        if (inputStreamReader != 0) {
                        }
                        if (byteArrayInputStream2 != 0) {
                        }
                        if (inputStream != null) {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        bufferedReader2 = bufferedReader;
                        r5 = inputStreamReader2;
                        if (bufferedReader2 != null) {
                        }
                        if (r5 != 0) {
                        }
                        if (byteArrayInputStream != 0) {
                        }
                        if (inputStream != null) {
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e = e4;
                    Log.e(str2, "IOException in SensitiveFilter constructor", (Throwable) e);
                    if (bufferedReader2 != null) {
                    }
                    if (inputStreamReader != 0) {
                    }
                    if (byteArrayInputStream2 != 0) {
                    }
                    if (inputStream != null) {
                    }
                }
                try {
                    bufferedReader.close();
                    inputStreamReader2.close();
                    byteArrayInputStream.close();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e5) {
                    Log.e(str2, str, (Throwable) e5);
                }
            } catch (IOException e6) {
                e = e6;
                inputStreamReader = 0;
                Log.e(str2, "IOException in SensitiveFilter constructor", (Throwable) e);
                if (bufferedReader2 != null) {
                }
                if (inputStreamReader != 0) {
                }
                if (byteArrayInputStream2 != 0) {
                }
                if (inputStream != null) {
                }
            } catch (Throwable th4) {
                th = th4;
                r5 = 0;
                byteArrayInputStream = byteArrayInputStream;
                if (bufferedReader2 != null) {
                }
                if (r5 != 0) {
                }
                if (byteArrayInputStream != 0) {
                }
                if (inputStream != null) {
                }
                throw th;
            }
        } catch (IOException e7) {
            e = e7;
            InputStream inputStream2 = null;
            byteArrayInputStream3 = 0;
            inputStreamReader = byteArrayInputStream3;
            byteArrayInputStream2 = byteArrayInputStream3;
            Log.e(str2, "IOException in SensitiveFilter constructor", (Throwable) e);
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            if (inputStreamReader != 0) {
                inputStreamReader.close();
            }
            if (byteArrayInputStream2 != 0) {
                byteArrayInputStream2.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Throwable th5) {
            th = th5;
            InputStream inputStream3 = null;
            byteArrayInputStream4 = 0;
            r5 = byteArrayInputStream2;
            byteArrayInputStream = byteArrayInputStream2;
            if (bufferedReader2 != null) {
            }
            if (r5 != 0) {
            }
            if (byteArrayInputStream != 0) {
            }
            if (inputStream != null) {
            }
            throw th;
        }
    }

    public static synchronized SensitiveFilter getInstance() {
        SensitiveFilter sensitiveFilter2;
        synchronized (SensitiveFilter.class) {
            if (sensitiveFilter == null) {
                sensitiveFilter = new SensitiveFilter();
            }
            sensitiveFilter2 = sensitiveFilter;
        }
        return sensitiveFilter2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:41:0x0074, code lost:
        if (r7 == null) goto L_0x00c3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        r7.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00a2, code lost:
        if (r7 != null) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:82:0x00b1, code lost:
        if (r7 != null) goto L_0x0076;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x00c0, code lost:
        if (r7 != null) goto L_0x0076;
     */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x008d A[SYNTHETIC, Splitter:B:58:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0092 A[SYNTHETIC, Splitter:B:62:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x009f A[SYNTHETIC, Splitter:B:71:0x009f] */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x00ae A[SYNTHETIC, Splitter:B:80:0x00ae] */
    /* JADX WARNING: Removed duplicated region for block: B:87:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x00bd A[SYNTHETIC, Splitter:B:89:0x00bd] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean loadSensitiveWords(String str, String str2) {
        FileOutputStream fileOutputStream;
        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        FileOutputStream fileOutputStream2;
        HttpURLConnection httpURLConnection2;
        InputStream inputStream2;
        FileOutputStream fileOutputStream3;
        HttpURLConnection httpURLConnection3;
        InputStream inputStream3;
        FileOutputStream fileOutputStream4;
        boolean z = false;
        InputStream inputStream4 = null;
        try {
            httpURLConnection3 = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection3.setConnectTimeout(20000);
                httpURLConnection3.setReadTimeout(20000);
                httpURLConnection3.setRequestMethod("GET");
                httpURLConnection3.setDoOutput(false);
                httpURLConnection3.connect();
                if (httpURLConnection3.getResponseCode() == 200) {
                    InputStream inputStream5 = httpURLConnection3.getInputStream();
                    try {
                        File file = new File(str2, "sensi_words");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        while (true) {
                            try {
                                int read = inputStream5.read();
                                if (read == -1) {
                                    break;
                                }
                                fileOutputStream.write(read);
                            } catch (MalformedURLException unused) {
                                inputStream = inputStream5;
                                if (httpURLConnection != null) {
                                }
                                if (inputStream != null) {
                                }
                            } catch (ZipException unused2) {
                                inputStream2 = inputStream5;
                                if (httpURLConnection2 != null) {
                                }
                                if (inputStream2 != null) {
                                }
                            } catch (Exception unused3) {
                                inputStream3 = inputStream5;
                                if (httpURLConnection3 != null) {
                                }
                                if (inputStream3 != null) {
                                }
                            } catch (Throwable th) {
                                inputStream4 = inputStream5;
                                Throwable th2 = th;
                                fileOutputStream4 = fileOutputStream;
                                th = th2;
                                if (httpURLConnection3 != null) {
                                }
                                if (inputStream4 != null) {
                                }
                                if (fileOutputStream4 != null) {
                                }
                                throw th;
                            }
                        }
                        inputStream5.close();
                        fileOutputStream.close();
                        z = true;
                        inputStream4 = inputStream5;
                    } catch (MalformedURLException unused4) {
                        fileOutputStream2 = null;
                        inputStream = inputStream5;
                        if (httpURLConnection != null) {
                        }
                        if (inputStream != null) {
                        }
                    } catch (ZipException unused5) {
                        fileOutputStream3 = null;
                        inputStream2 = inputStream5;
                        if (httpURLConnection2 != null) {
                        }
                        if (inputStream2 != null) {
                        }
                    } catch (Exception unused6) {
                        fileOutputStream = null;
                        inputStream3 = inputStream5;
                        if (httpURLConnection3 != null) {
                        }
                        if (inputStream3 != null) {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileOutputStream4 = null;
                        inputStream4 = inputStream5;
                        if (httpURLConnection3 != null) {
                        }
                        if (inputStream4 != null) {
                        }
                        if (fileOutputStream4 != null) {
                        }
                        throw th;
                    }
                } else {
                    fileOutputStream = null;
                }
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                if (inputStream4 != null) {
                    try {
                        inputStream4.close();
                    } catch (IOException unused7) {
                    }
                }
            } catch (MalformedURLException unused8) {
                fileOutputStream2 = null;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused9) {
                    }
                }
            } catch (ZipException unused10) {
                fileOutputStream3 = null;
                if (httpURLConnection2 != null) {
                    httpURLConnection2.disconnect();
                }
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (IOException unused11) {
                    }
                }
            } catch (Exception unused12) {
                fileOutputStream = null;
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                if (inputStream3 != null) {
                    try {
                        inputStream3.close();
                    } catch (IOException unused13) {
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream4 = null;
                if (httpURLConnection3 != null) {
                    httpURLConnection3.disconnect();
                }
                if (inputStream4 != null) {
                    try {
                        inputStream4.close();
                    } catch (IOException unused14) {
                    }
                }
                if (fileOutputStream4 != null) {
                    try {
                        fileOutputStream4.close();
                    } catch (IOException unused15) {
                    }
                }
                throw th;
            }
        } catch (MalformedURLException unused16) {
            httpURLConnection = null;
            fileOutputStream2 = null;
            if (httpURLConnection != null) {
            }
            if (inputStream != null) {
            }
        } catch (ZipException unused17) {
            httpURLConnection2 = null;
            fileOutputStream3 = null;
            if (httpURLConnection2 != null) {
            }
            if (inputStream2 != null) {
            }
        } catch (Exception unused18) {
            httpURLConnection3 = null;
            fileOutputStream = null;
            if (httpURLConnection3 != null) {
            }
            if (inputStream3 != null) {
            }
        } catch (Throwable th5) {
            th = th5;
            httpURLConnection3 = null;
            fileOutputStream4 = null;
            if (httpURLConnection3 != null) {
            }
            if (inputStream4 != null) {
            }
            if (fileOutputStream4 != null) {
            }
            throw th;
        }
        return z;
    }

    public String getSensitiveWord(String str) {
        String str2 = "";
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        StringPointer stringPointer = new StringPointer(str);
        int i = 0;
        while (true) {
            int i2 = 1;
            if (i >= stringPointer.length - 1) {
                return str2;
            }
            int nextTwoCharHash = stringPointer.nextTwoCharHash(i);
            SensitiveNode[] sensitiveNodeArr = this.nodes;
            SensitiveNode sensitiveNode = sensitiveNodeArr[nextTwoCharHash & (sensitiveNodeArr.length - 1)];
            if (sensitiveNode != null) {
                int nextTwoCharMix = stringPointer.nextTwoCharMix(i);
                while (true) {
                    if (sensitiveNode == null) {
                        break;
                    }
                    if (sensitiveNode.headTwoCharMix == nextTwoCharMix) {
                        NavigableSet headSet = sensitiveNode.words.headSet(stringPointer.substring(i), true);
                        if (headSet != null) {
                            for (StringPointer stringPointer2 : headSet.descendingSet()) {
                                if (stringPointer.nextStartsWith(i, stringPointer2)) {
                                    str2 = new String(stringPointer2.value);
                                    i2 = stringPointer2.length;
                                    break;
                                }
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    sensitiveNode = sensitiveNode.next;
                }
            }
            i += i2;
        }
    }

    public boolean put(String str) {
        TreeSet treeSet;
        if (this.nodes == null || str == null || str.trim().length() < 2) {
            return false;
        }
        StringPointer stringPointer = new StringPointer(str.trim());
        int nextTwoCharHash = stringPointer.nextTwoCharHash(0);
        int nextTwoCharMix = stringPointer.nextTwoCharMix(0);
        SensitiveNode[] sensitiveNodeArr = this.nodes;
        int length = nextTwoCharHash & (sensitiveNodeArr.length - 1);
        SensitiveNode sensitiveNode = sensitiveNodeArr[length];
        if (sensitiveNode == null) {
            SensitiveNode sensitiveNode2 = new SensitiveNode(nextTwoCharMix);
            sensitiveNode2.words.add(stringPointer);
            this.nodes[length] = sensitiveNode2;
        } else {
            while (sensitiveNode != null) {
                if (sensitiveNode.headTwoCharMix == nextTwoCharMix) {
                    treeSet = sensitiveNode.words;
                } else {
                    SensitiveNode sensitiveNode3 = sensitiveNode.next;
                    if (sensitiveNode3 == null) {
                        treeSet = new SensitiveNode(nextTwoCharMix, sensitiveNode).words;
                    } else {
                        sensitiveNode = sensitiveNode3;
                    }
                }
                treeSet.add(stringPointer);
                return true;
            }
        }
        return true;
    }

    public String replaceSensitiveWord(String str, char c) {
        int i;
        StringPointer stringPointer = new StringPointer(str);
        int i2 = 0;
        boolean z = false;
        while (true) {
            boolean z2 = true;
            if (i2 >= stringPointer.length - 1) {
                break;
            }
            int nextTwoCharHash = stringPointer.nextTwoCharHash(i2);
            SensitiveNode[] sensitiveNodeArr = this.nodes;
            SensitiveNode sensitiveNode = sensitiveNodeArr[nextTwoCharHash & (sensitiveNodeArr.length - 1)];
            if (sensitiveNode != null) {
                int nextTwoCharMix = stringPointer.nextTwoCharMix(i2);
                while (true) {
                    if (sensitiveNode == null) {
                        break;
                    }
                    if (sensitiveNode.headTwoCharMix == nextTwoCharMix) {
                        NavigableSet headSet = sensitiveNode.words.headSet(stringPointer.substring(i2), true);
                        if (headSet != null) {
                            for (StringPointer stringPointer2 : headSet.descendingSet()) {
                                if (stringPointer.nextStartsWith(i2, stringPointer2)) {
                                    stringPointer.fill(i2, stringPointer2.length + i2, c);
                                    i = stringPointer2.length;
                                    break;
                                }
                            }
                            continue;
                        } else {
                            continue;
                        }
                    }
                    sensitiveNode = sensitiveNode.next;
                }
            }
            z2 = z;
            i = 1;
            i2 += i;
            z = z2;
        }
        return z ? stringPointer.toString() : str;
    }
}
