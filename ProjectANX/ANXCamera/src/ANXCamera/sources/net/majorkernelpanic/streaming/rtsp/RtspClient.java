package net.majorkernelpanic.streaming.rtsp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.Stream;

public class RtspClient {
    public static final int ERROR_CONNECTION_FAILED = 1;
    public static final int ERROR_CONNECTION_LOST = 4;
    public static final int ERROR_WRONG_CREDENTIALS = 3;
    public static final int MESSAGE_CONNECTION_RECOVERED = 5;
    private static final int STATE_STARTED = 0;
    private static final int STATE_STARTING = 1;
    private static final int STATE_STOPPED = 3;
    private static final int STATE_STOPPING = 2;
    public static final String TAG = "RtspClient";
    public static final int TRANSPORT_TCP = 1;
    public static final int TRANSPORT_UDP = 0;
    protected static final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private String mAuthorization;
    private BufferedReader mBufferedReader;
    private int mCSeq = 0;
    /* access modifiers changed from: private */
    public Callback mCallback;
    /* access modifiers changed from: private */
    public Runnable mConnectionMonitor = new Runnable() {
        public void run() {
            if (RtspClient.this.mState == 0) {
                try {
                    RtspClient.this.sendRequestOption();
                    RtspClient.this.mHandler.postDelayed(RtspClient.this.mConnectionMonitor, 6000);
                } catch (IOException unused) {
                    RtspClient.this.postMessage(4);
                    Log.e(RtspClient.TAG, "Connection lost with the server...");
                    RtspClient.this.mParameters.session.stop();
                    RtspClient.this.mHandler.post(RtspClient.this.mRetryConnection);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Handler mMainHandler;
    private OutputStream mOutputStream;
    /* access modifiers changed from: private */
    public Parameters mParameters;
    /* access modifiers changed from: private */
    public Runnable mRetryConnection = new Runnable() {
        /* JADX WARNING: Can't wrap try/catch for region: R(7:3|4|5|6|7|8|14) */
        /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
            return;
         */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            if (RtspClient.this.mState == 0) {
                try {
                    Log.e(RtspClient.TAG, "Trying to reconnect...");
                    RtspClient.this.tryConnection();
                    RtspClient.this.mParameters.session.start();
                    RtspClient.this.mHandler.post(RtspClient.this.mConnectionMonitor);
                    RtspClient.this.postMessage(5);
                    RtspClient.this.abort();
                } catch (IOException unused) {
                    RtspClient.this.mHandler.postDelayed(RtspClient.this.mRetryConnection, 1000);
                }
            }
        }
    };
    private String mSessionID;
    private Socket mSocket;
    /* access modifiers changed from: private */
    public int mState = 0;
    /* access modifiers changed from: private */
    public Parameters mTmpParameters = new Parameters();

    public interface Callback {
        void onRtspUpdate(int i, Exception exc);
    }

    class Parameters {
        public String host;
        public String password;
        public String path;
        public int port;
        public Session session;
        public int transport;
        public String username;

        private Parameters() {
        }

        public static Parameters copyOf(Parameters parameters) {
            Parameters parameters2 = new Parameters();
            parameters2.host = parameters.host;
            parameters2.username = parameters.username;
            parameters2.password = parameters.password;
            parameters2.path = parameters.path;
            parameters2.session = parameters.session;
            parameters2.port = parameters.port;
            parameters2.transport = parameters.transport;
            return parameters2;
        }
    }

    class Response {
        public static final Pattern regexStatus = Pattern.compile("RTSP/\\d.\\d (\\d+) (\\w+)", 2);
        public static final Pattern rexegAuthenticate = Pattern.compile("realm=\"(.+)\",\\s+nonce=\"(\\w+)\"", 2);
        public static final Pattern rexegHeader = Pattern.compile("(\\S+):(.+)", 2);
        public static final Pattern rexegSession = Pattern.compile("(\\d+)", 2);
        public static final Pattern rexegTransport = Pattern.compile("client_port=(\\d+)-(\\d+).+server_port=(\\d+)-(\\d+)", 2);
        public HashMap headers = new HashMap();
        public int status;

        Response() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x0067  */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x004e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static Response parseResponse(BufferedReader bufferedReader) {
            String readLine;
            Response response = new Response();
            String readLine2 = bufferedReader.readLine();
            String str = "Connection lost";
            if (readLine2 != null) {
                Matcher matcher = regexStatus.matcher(readLine2);
                matcher.find();
                response.status = Integer.parseInt(matcher.group(1));
                while (true) {
                    readLine = bufferedReader.readLine();
                    if (readLine != null && readLine.length() > 3) {
                        Matcher matcher2 = rexegHeader.matcher(readLine);
                        matcher2.find();
                        response.headers.put(matcher2.group(1).toLowerCase(Locale.US), matcher2.group(2));
                    } else if (readLine == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Response from server: ");
                        sb.append(response.status);
                        Log.d(RtspClient.TAG, sb.toString());
                        return response;
                    } else {
                        throw new SocketException(str);
                    }
                }
                if (readLine == null) {
                }
            } else {
                throw new SocketException(str);
            }
        }
    }

    public RtspClient() {
        Parameters parameters = this.mTmpParameters;
        parameters.port = 1935;
        parameters.path = "/";
        parameters.transport = 0;
        this.mAuthorization = null;
        this.mCallback = null;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mState = 3;
        final Semaphore semaphore = new Semaphore(0);
        new HandlerThread("net.majorkernelpanic.streaming.RtspClient") {
            /* access modifiers changed from: protected */
            public void onLooperPrepared() {
                RtspClient.this.mHandler = new Handler();
                semaphore.release();
            }
        }.start();
        semaphore.acquireUninterruptibly();
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0003 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void abort() {
        sendRequestTeardown();
        this.mSocket.close();
        this.mHandler.removeCallbacks(this.mConnectionMonitor);
        this.mHandler.removeCallbacks(this.mRetryConnection);
        this.mState = 3;
    }

    private String addHeaders() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("CSeq: ");
        int i = this.mCSeq + 1;
        this.mCSeq = i;
        sb.append(i);
        sb.append("\r\nContent-Length: 0\r\nSession: ");
        sb.append(this.mSessionID);
        String str2 = "\r\n";
        sb.append(str2);
        if (this.mAuthorization != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Authorization: ");
            sb2.append(this.mAuthorization);
            sb2.append(str2);
            str = sb2.toString();
        } else {
            str = "";
        }
        sb.append(str);
        sb.append(str2);
        return sb.toString();
    }

    private static String bytesToHex(byte[] bArr) {
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i] & -1;
            int i2 = i * 2;
            char[] cArr2 = hexArray;
            cArr[i2] = cArr2[b >>> 4];
            cArr[i2 + 1] = cArr2[b & 15];
        }
        return new String(cArr);
    }

    private String computeMd5Hash(String str) {
        try {
            return bytesToHex(MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException unused) {
            return "";
        }
    }

    /* access modifiers changed from: private */
    public void postError(final int i, final Exception exc) {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (RtspClient.this.mCallback != null) {
                    RtspClient.this.mCallback.onRtspUpdate(i, exc);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void postMessage(final int i) {
        this.mMainHandler.post(new Runnable() {
            public void run() {
                if (RtspClient.this.mCallback != null) {
                    RtspClient.this.mCallback.onRtspUpdate(i, null);
                }
            }
        });
    }

    private void sendRequestAnnounce() {
        String str;
        String sessionDescription = this.mParameters.session.getSessionDescription();
        StringBuilder sb = new StringBuilder();
        String str2 = "ANNOUNCE rtsp://";
        sb.append(str2);
        sb.append(this.mParameters.host);
        String str3 = ":";
        sb.append(str3);
        sb.append(this.mParameters.port);
        sb.append(this.mParameters.path);
        String str4 = " RTSP/1.0\r\nCSeq: ";
        sb.append(str4);
        int i = this.mCSeq + 1;
        this.mCSeq = i;
        sb.append(i);
        String str5 = "\r\nContent-Length: ";
        sb.append(str5);
        sb.append(sessionDescription.length());
        String str6 = "\r\nContent-Type: application/sdp\r\n\r\n";
        sb.append(str6);
        sb.append(sessionDescription);
        String sb2 = sb.toString();
        String str7 = "\r\n";
        String substring = sb2.substring(0, sb2.indexOf(str7));
        String str8 = TAG;
        Log.i(str8, substring);
        String str9 = "UTF-8";
        this.mOutputStream.write(sb2.getBytes(str9));
        this.mOutputStream.flush();
        Response parseResponse = Response.parseResponse(this.mBufferedReader);
        String str10 = "server";
        if (parseResponse.headers.containsKey(str10)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("RTSP server name:");
            sb3.append((String) parseResponse.headers.get(str10));
            str = sb3.toString();
        } else {
            str = "RTSP server name unknown";
        }
        Log.v(str8, str);
        String str11 = "session";
        if (parseResponse.headers.containsKey(str11)) {
            try {
                Matcher matcher = Response.rexegSession.matcher((CharSequence) parseResponse.headers.get(str11));
                matcher.find();
                this.mSessionID = matcher.group(1);
            } catch (Exception unused) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Invalid response from server. Session id: ");
                sb4.append(this.mSessionID);
                throw new IOException(sb4.toString());
            }
        }
        int i2 = parseResponse.status;
        if (i2 == 401) {
            Parameters parameters = this.mParameters;
            if (parameters.username == null || parameters.password == null) {
                throw new IllegalStateException("Authentication is enabled and setCredentials(String,String) was not called !");
            }
            try {
                Matcher matcher2 = Response.rexegAuthenticate.matcher((CharSequence) parseResponse.headers.get("www-authenticate"));
                matcher2.find();
                String group = matcher2.group(2);
                String group2 = matcher2.group(1);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("rtsp://");
                sb5.append(this.mParameters.host);
                sb5.append(str3);
                sb5.append(this.mParameters.port);
                sb5.append(this.mParameters.path);
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append(this.mParameters.username);
                sb7.append(str3);
                String str12 = str9;
                sb7.append(matcher2.group(1));
                sb7.append(str3);
                sb7.append(this.mParameters.password);
                String computeMd5Hash = computeMd5Hash(sb7.toString());
                StringBuilder sb8 = new StringBuilder();
                sb8.append("ANNOUNCE:");
                sb8.append(sb6);
                String computeMd5Hash2 = computeMd5Hash(sb8.toString());
                StringBuilder sb9 = new StringBuilder();
                sb9.append(computeMd5Hash);
                sb9.append(str3);
                sb9.append(matcher2.group(2));
                sb9.append(str3);
                sb9.append(computeMd5Hash2);
                String computeMd5Hash3 = computeMd5Hash(sb9.toString());
                StringBuilder sb10 = new StringBuilder();
                sb10.append("Digest username=\"");
                sb10.append(this.mParameters.username);
                sb10.append("\",realm=\"");
                sb10.append(group2);
                sb10.append("\",nonce=\"");
                sb10.append(group);
                sb10.append("\",uri=\"");
                sb10.append(sb6);
                sb10.append("\",response=\"");
                sb10.append(computeMd5Hash3);
                sb10.append("\"");
                this.mAuthorization = sb10.toString();
                StringBuilder sb11 = new StringBuilder();
                sb11.append(str2);
                sb11.append(this.mParameters.host);
                sb11.append(str3);
                sb11.append(this.mParameters.port);
                sb11.append(this.mParameters.path);
                sb11.append(str4);
                int i3 = this.mCSeq + 1;
                this.mCSeq = i3;
                sb11.append(i3);
                sb11.append(str5);
                sb11.append(sessionDescription.length());
                sb11.append("\r\nAuthorization: ");
                sb11.append(this.mAuthorization);
                sb11.append("\r\nSession: ");
                sb11.append(this.mSessionID);
                sb11.append(str6);
                sb11.append(sessionDescription);
                String sb12 = sb11.toString();
                Log.i(str8, sb12.substring(0, sb12.indexOf(str7)));
                this.mOutputStream.write(sb12.getBytes(str12));
                this.mOutputStream.flush();
                if (Response.parseResponse(this.mBufferedReader).status == 401) {
                    throw new RuntimeException("Bad credentials !");
                }
            } catch (Exception unused2) {
                throw new IOException("Invalid response from server");
            }
        } else if (i2 == 403) {
            throw new RuntimeException("Access forbidden !");
        }
    }

    /* access modifiers changed from: private */
    public void sendRequestOption() {
        StringBuilder sb = new StringBuilder();
        sb.append("OPTIONS rtsp://");
        sb.append(this.mParameters.host);
        sb.append(":");
        sb.append(this.mParameters.port);
        sb.append(this.mParameters.path);
        sb.append(" RTSP/1.0\r\n");
        sb.append(addHeaders());
        String sb2 = sb.toString();
        Log.i(TAG, sb2.substring(0, sb2.indexOf("\r\n")));
        this.mOutputStream.write(sb2.getBytes("UTF-8"));
        this.mOutputStream.flush();
        Response.parseResponse(this.mBufferedReader);
    }

    private void sendRequestRecord() {
        StringBuilder sb = new StringBuilder();
        sb.append("RECORD rtsp://");
        sb.append(this.mParameters.host);
        sb.append(":");
        sb.append(this.mParameters.port);
        sb.append(this.mParameters.path);
        sb.append(" RTSP/1.0\r\nRange: npt=0.000-\r\n");
        sb.append(addHeaders());
        String sb2 = sb.toString();
        Log.i(TAG, sb2.substring(0, sb2.indexOf("\r\n")));
        this.mOutputStream.write(sb2.getBytes("UTF-8"));
        this.mOutputStream.flush();
        Response.parseResponse(this.mBufferedReader);
    }

    private void sendRequestSetup() {
        StringBuilder sb;
        for (int i = 0; i < 2; i++) {
            Stream track = this.mParameters.session.getTrack(i);
            if (track != null) {
                String str = "-";
                if (this.mParameters.transport == 1) {
                    sb = new StringBuilder();
                    sb.append("TCP;interleaved=");
                    int i2 = i * 2;
                    sb.append(i2);
                    sb.append(str);
                    sb.append(i2 + 1);
                } else {
                    sb = new StringBuilder();
                    sb.append("UDP;unicast;client_port=");
                    int i3 = (i * 2) + 5000;
                    sb.append(i3);
                    sb.append(str);
                    sb.append(i3 + 1);
                    sb.append(";mode=receive");
                }
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("SETUP rtsp://");
                sb3.append(this.mParameters.host);
                sb3.append(":");
                sb3.append(this.mParameters.port);
                sb3.append(this.mParameters.path);
                sb3.append("/trackID=");
                sb3.append(i);
                sb3.append(" RTSP/1.0\r\nTransport: RTP/AVP/");
                sb3.append(sb2);
                String str2 = "\r\n";
                sb3.append(str2);
                sb3.append(addHeaders());
                String sb4 = sb3.toString();
                String substring = sb4.substring(0, sb4.indexOf(str2));
                String str3 = TAG;
                Log.i(str3, substring);
                this.mOutputStream.write(sb4.getBytes("UTF-8"));
                this.mOutputStream.flush();
                Response parseResponse = Response.parseResponse(this.mBufferedReader);
                String str4 = "session";
                if (parseResponse.headers.containsKey(str4)) {
                    try {
                        Matcher matcher = Response.rexegSession.matcher((CharSequence) parseResponse.headers.get(str4));
                        matcher.find();
                        this.mSessionID = matcher.group(1);
                    } catch (Exception unused) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("Invalid response from server. Session id: ");
                        sb5.append(this.mSessionID);
                        throw new IOException(sb5.toString());
                    }
                }
                if (this.mParameters.transport == 0) {
                    try {
                        Matcher matcher2 = Response.rexegTransport.matcher((CharSequence) parseResponse.headers.get(NotificationCompat.CATEGORY_TRANSPORT));
                        matcher2.find();
                        track.setDestinationPorts(Integer.parseInt(matcher2.group(3)), Integer.parseInt(matcher2.group(4)));
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("Setting destination ports: ");
                        sb6.append(Integer.parseInt(matcher2.group(3)));
                        sb6.append(", ");
                        sb6.append(Integer.parseInt(matcher2.group(4)));
                        Log.d(str3, sb6.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        int[] destinationPorts = track.getDestinationPorts();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("Server did not specify ports, using default ports: ");
                        sb7.append(destinationPorts[0]);
                        sb7.append(str);
                        sb7.append(destinationPorts[1]);
                        Log.d(str3, sb7.toString());
                    }
                } else {
                    track.setOutputStream(this.mOutputStream, (byte) (i * 2));
                }
            }
        }
    }

    private void sendRequestTeardown() {
        StringBuilder sb = new StringBuilder();
        sb.append("TEARDOWN rtsp://");
        sb.append(this.mParameters.host);
        sb.append(":");
        sb.append(this.mParameters.port);
        sb.append(this.mParameters.path);
        sb.append(" RTSP/1.0\r\n");
        sb.append(addHeaders());
        String sb2 = sb.toString();
        Log.i(TAG, sb2.substring(0, sb2.indexOf("\r\n")));
        this.mOutputStream.write(sb2.getBytes("UTF-8"));
        this.mOutputStream.flush();
    }

    /* access modifiers changed from: private */
    public void tryConnection() {
        this.mCSeq = 0;
        Parameters parameters = this.mParameters;
        this.mSocket = new Socket(parameters.host, parameters.port);
        this.mBufferedReader = new BufferedReader(new InputStreamReader(this.mSocket.getInputStream()));
        this.mOutputStream = new BufferedOutputStream(this.mSocket.getOutputStream());
        sendRequestAnnounce();
        sendRequestSetup();
        sendRequestRecord();
    }

    public Session getSession() {
        return this.mTmpParameters.session;
    }

    public boolean isStreaming() {
        int i = this.mState;
        return i == 0 || i == 1;
    }

    public void release() {
        stopStream();
        this.mHandler.getLooper().quit();
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setCredentials(String str, String str2) {
        Parameters parameters = this.mTmpParameters;
        parameters.username = str;
        parameters.password = str2;
    }

    public void setServerAddress(String str, int i) {
        Parameters parameters = this.mTmpParameters;
        parameters.port = i;
        parameters.host = str;
    }

    public void setSession(Session session) {
        this.mTmpParameters.session = session;
    }

    public void setStreamPath(String str) {
        this.mTmpParameters.path = str;
    }

    public void setTransportMode(int i) {
        this.mTmpParameters.transport = i;
    }

    public void startStream() {
        Parameters parameters = this.mTmpParameters;
        if (parameters.host == null) {
            throw new IllegalStateException("setServerAddress(String,int) has not been called !");
        } else if (parameters.session != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (RtspClient.this.mState == 3) {
                        RtspClient.this.mState = 1;
                        Log.d(RtspClient.TAG, "Connecting to RTSP server...");
                        RtspClient rtspClient = RtspClient.this;
                        rtspClient.mParameters = Parameters.copyOf(rtspClient.mTmpParameters);
                        RtspClient.this.mParameters.session.setDestination(RtspClient.this.mTmpParameters.host);
                        try {
                            RtspClient.this.mParameters.session.syncConfigure();
                            try {
                                RtspClient.this.tryConnection();
                                try {
                                    RtspClient.this.mParameters.session.syncStart();
                                    RtspClient.this.mState = 0;
                                    if (RtspClient.this.mParameters.transport == 0) {
                                        RtspClient.this.mHandler.post(RtspClient.this.mConnectionMonitor);
                                    }
                                } catch (Exception unused) {
                                    RtspClient.this.abort();
                                }
                            } catch (Exception e) {
                                RtspClient.this.postError(1, e);
                                RtspClient.this.abort();
                            }
                        } catch (Exception unused2) {
                            RtspClient.this.mParameters.session = null;
                            RtspClient.this.mState = 3;
                        }
                    }
                }
            });
        } else {
            throw new IllegalStateException("setSession() has not been called !");
        }
    }

    public void stopStream() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (!(RtspClient.this.mParameters == null || RtspClient.this.mParameters.session == null)) {
                    RtspClient.this.mParameters.session.stop();
                }
                if (RtspClient.this.mState != 3) {
                    RtspClient.this.mState = 2;
                    RtspClient.this.abort();
                }
            }
        });
    }
}
