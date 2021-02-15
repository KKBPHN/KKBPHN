package net.majorkernelpanic.streaming.rtsp;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;

public class RtspServer extends Service {
    public static final int DEFAULT_RTSP_PORT = 8086;
    public static final int ERROR_BIND_FAILED = 0;
    public static final int ERROR_START_FAILED = 1;
    public static final String KEY_ENABLED = "rtsp_enabled";
    public static final String KEY_PORT = "rtsp_port";
    public static final int MESSAGE_STREAMING_STARTED = 0;
    public static final int MESSAGE_STREAMING_STOPPED = 1;
    public static String SERVER_NAME = "MajorKernelPanic RTSP Server";
    public static final String TAG = "RtspServer";
    private final IBinder mBinder = new LocalBinder();
    protected boolean mEnabled = true;
    private RequestListener mListenerThread;
    private final LinkedList mListeners = new LinkedList();
    private OnSharedPreferenceChangeListener mOnSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            String str2 = RtspServer.KEY_PORT;
            if (str.equals(str2)) {
                int parseInt = Integer.parseInt(sharedPreferences.getString(str2, String.valueOf(RtspServer.this.mPort)));
                RtspServer rtspServer = RtspServer.this;
                if (parseInt != rtspServer.mPort) {
                    rtspServer.mPort = parseInt;
                    rtspServer.mRestart = true;
                } else {
                    return;
                }
            } else {
                String str3 = RtspServer.KEY_ENABLED;
                if (str.equals(str3)) {
                    RtspServer rtspServer2 = RtspServer.this;
                    rtspServer2.mEnabled = sharedPreferences.getBoolean(str3, rtspServer2.mEnabled);
                } else {
                    return;
                }
            }
            RtspServer.this.start();
        }
    };
    /* access modifiers changed from: private */
    public String mPassword;
    protected int mPort = 8086;
    /* access modifiers changed from: private */
    public boolean mRestart = false;
    protected SessionBuilder mSessionBuilder;
    protected WeakHashMap mSessions = new WeakHashMap(2);
    protected SharedPreferences mSharedPreferences;
    /* access modifiers changed from: private */
    public String mUsername;

    public interface CallbackListener {
        void onError(RtspServer rtspServer, Exception exc, int i);

        void onMessage(RtspServer rtspServer, int i);
    }

    public class LocalBinder extends Binder {
        public LocalBinder() {
        }

        public RtspServer getService() {
            return RtspServer.this;
        }
    }

    class Request {
        public static final Pattern regexMethod = Pattern.compile("(\\w+) (\\S+) RTSP", 2);
        public static final Pattern rexegHeader = Pattern.compile("(\\S+):(.+)", 2);
        public HashMap headers = new HashMap();
        public String method;
        public String uri;

        Request() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:11:0x006e  */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x0050  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static Request parseRequest(BufferedReader bufferedReader) {
            String readLine;
            Request request = new Request();
            String readLine2 = bufferedReader.readLine();
            String str = "Client disconnected";
            if (readLine2 != null) {
                Matcher matcher = regexMethod.matcher(readLine2);
                matcher.find();
                request.method = matcher.group(1);
                request.uri = matcher.group(2);
                while (true) {
                    readLine = bufferedReader.readLine();
                    if (readLine != null && readLine.length() > 3) {
                        Matcher matcher2 = rexegHeader.matcher(readLine);
                        matcher2.find();
                        request.headers.put(matcher2.group(1).toLowerCase(Locale.US), matcher2.group(2));
                    } else if (readLine == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(request.method);
                        sb.append(" ");
                        sb.append(request.uri);
                        Log.e(RtspServer.TAG, sb.toString());
                        return request;
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

    class RequestListener extends Thread implements Runnable {
        private final ServerSocket mServer;

        public RequestListener() {
            try {
                this.mServer = new ServerSocket(RtspServer.this.mPort);
                start();
            } catch (BindException e) {
                Log.e(RtspServer.TAG, "Port already in use !");
                RtspServer.this.postError(e, 0);
                throw e;
            }
        }

        /* JADX WARNING: Can't wrap try/catch for region: R(5:0|1|2|3|5) */
        /* JADX WARNING: Code restructure failed: missing block: B:6:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0005 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void kill() {
            this.mServer.close();
            join();
        }

        public void run() {
            StringBuilder sb = new StringBuilder();
            sb.append("RTSP server listening on port ");
            sb.append(this.mServer.getLocalPort());
            String sb2 = sb.toString();
            String str = RtspServer.TAG;
            Log.i(str, sb2);
            while (!Thread.interrupted()) {
                try {
                    new WorkerThread(this.mServer.accept()).start();
                } catch (SocketException unused) {
                } catch (IOException e) {
                    Log.e(str, e.getMessage());
                }
            }
            Log.i(str, "RTSP server stopped !");
        }
    }

    class Response {
        public static final String STATUS_BAD_REQUEST = "400 Bad Request";
        public static final String STATUS_INTERNAL_SERVER_ERROR = "500 Internal Server Error";
        public static final String STATUS_NOT_FOUND = "404 Not Found";
        public static final String STATUS_OK = "200 OK";
        public static final String STATUS_UNAUTHORIZED = "401 Unauthorized";
        public String attributes;
        public String content;
        private final Request mRequest;
        public String status = "500 Internal Server Error";

        public Response() {
            String str = "";
            this.content = str;
            this.attributes = str;
            this.mRequest = null;
        }

        public Response(Request request) {
            String str = "";
            this.content = str;
            this.attributes = str;
            this.mRequest = request;
        }

        public void send(OutputStream outputStream) {
            int i;
            String str;
            String str2 = RtspServer.TAG;
            String str3 = "";
            try {
                i = Integer.parseInt(((String) this.mRequest.headers.get("cseq")).replace(" ", str3));
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error parsing CSeq: ");
                sb.append(e.getMessage() != null ? e.getMessage() : str3);
                Log.e(str2, sb.toString());
                i = -1;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("RTSP/1.0 ");
            sb2.append(this.status);
            sb2.append("\r\nServer: ");
            sb2.append(RtspServer.SERVER_NAME);
            String str4 = "\r\n";
            sb2.append(str4);
            if (i >= 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Cseq: ");
                sb3.append(i);
                sb3.append(str4);
                str = sb3.toString();
            } else {
                str = str3;
            }
            sb2.append(str);
            sb2.append("Content-Length: ");
            sb2.append(this.content.length());
            sb2.append(str4);
            sb2.append(this.attributes);
            sb2.append(str4);
            sb2.append(this.content);
            String sb4 = sb2.toString();
            Log.d(str2, sb4.replace("\r", str3));
            outputStream.write(sb4.getBytes());
        }
    }

    class WorkerThread extends Thread implements Runnable {
        private final Socket mClient;
        private final BufferedReader mInput;
        private final OutputStream mOutput;
        private Session mSession = new Session();

        public WorkerThread(Socket socket) {
            this.mInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.mOutput = socket.getOutputStream();
            this.mClient = socket;
        }

        private boolean isAuthorized(Request request) {
            String str = (String) request.headers.get("authorization");
            if (RtspServer.this.mUsername == null || RtspServer.this.mPassword == null || RtspServer.this.mUsername.isEmpty()) {
                return true;
            }
            if (str != null && !str.isEmpty()) {
                String substring = str.substring(str.lastIndexOf(" ") + 1);
                StringBuilder sb = new StringBuilder();
                sb.append(RtspServer.this.mUsername);
                sb.append(":");
                sb.append(RtspServer.this.mPassword);
                if (Base64.encodeToString(sb.toString().getBytes(), 2).equals(substring)) {
                    return true;
                }
            }
            return false;
        }

        public Response processRequest(Request request) {
            String sb;
            int i;
            int i2;
            Response response = new Response(request);
            boolean isAuthorized = isAuthorized(request);
            String str = HttpRequest.METHOD_OPTIONS;
            if (isAuthorized || request.method.equalsIgnoreCase(str)) {
                String str2 = ":";
                String str3 = "200 OK";
                if (request.method.equalsIgnoreCase("DESCRIBE")) {
                    this.mSession = RtspServer.this.handleRequest(request.uri, this.mClient);
                    RtspServer.this.mSessions.put(this.mSession, null);
                    this.mSession.syncConfigure();
                    String sessionDescription = this.mSession.getSessionDescription();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Content-Base: ");
                    sb2.append(this.mClient.getLocalAddress().getHostAddress());
                    sb2.append(str2);
                    sb2.append(this.mClient.getLocalPort());
                    sb2.append("/\r\nContent-Type: application/sdp\r\n");
                    response.attributes = sb2.toString();
                    response.content = sessionDescription;
                } else {
                    if (request.method.equalsIgnoreCase(str)) {
                        response.status = str3;
                        sb = "Public: DESCRIBE,SETUP,TEARDOWN,PLAY,PAUSE\r\n";
                    } else {
                        String str4 = "400 Bad Request";
                        if (request.method.equalsIgnoreCase("SETUP")) {
                            Matcher matcher = Pattern.compile("trackID=(\\w+)", 2).matcher(request.uri);
                            if (!matcher.find()) {
                                response.status = str4;
                                return response;
                            }
                            int parseInt = Integer.parseInt(matcher.group(1));
                            if (!this.mSession.trackExists(parseInt)) {
                                response.status = "404 Not Found";
                                return response;
                            }
                            Matcher matcher2 = Pattern.compile("client_port=(\\d+)(?:-(\\d+))?", 2).matcher((CharSequence) request.headers.get(NotificationCompat.CATEGORY_TRANSPORT));
                            if (!matcher2.find()) {
                                int[] destinationPorts = this.mSession.getTrack(parseInt).getDestinationPorts();
                                int i3 = destinationPorts[0];
                                i = destinationPorts[1];
                                i2 = i3;
                            } else {
                                i2 = Integer.parseInt(matcher2.group(1));
                                i = matcher2.group(2) == null ? i2 + 1 : Integer.parseInt(matcher2.group(2));
                            }
                            int ssrc = this.mSession.getTrack(parseInt).getSSRC();
                            int[] localPorts = this.mSession.getTrack(parseInt).getLocalPorts();
                            String destination = this.mSession.getDestination();
                            this.mSession.getTrack(parseInt).setDestinationPorts(i2, i);
                            boolean isStreaming = RtspServer.this.isStreaming();
                            this.mSession.syncStart(parseInt);
                            if (!isStreaming && RtspServer.this.isStreaming()) {
                                RtspServer.this.postMessage(0);
                            }
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Transport: RTP/AVP/UDP;");
                            sb3.append(InetAddress.getByName(destination).isMulticastAddress() ? "multicast" : "unicast");
                            sb3.append(";destination=");
                            sb3.append(this.mSession.getDestination());
                            sb3.append(";client_port=");
                            sb3.append(i2);
                            String str5 = "-";
                            sb3.append(str5);
                            sb3.append(i);
                            sb3.append(";server_port=");
                            sb3.append(localPorts[0]);
                            sb3.append(str5);
                            sb3.append(localPorts[1]);
                            sb3.append(";ssrc=");
                            sb3.append(Integer.toHexString(ssrc));
                            sb3.append(";mode=play\r\nSession: 1185d20035702ca\r\nCache-Control: no-cache\r\n");
                            response.attributes = sb3.toString();
                            response.status = str3;
                        } else if (request.method.equalsIgnoreCase("PLAY")) {
                            String str6 = "RTP-Info: ";
                            String str7 = ";seq=0,";
                            String str8 = "/trackID=";
                            String str9 = "url=rtsp://";
                            if (this.mSession.trackExists(0)) {
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append(str6);
                                sb4.append(str9);
                                sb4.append(this.mClient.getLocalAddress().getHostAddress());
                                sb4.append(str2);
                                sb4.append(this.mClient.getLocalPort());
                                sb4.append(str8);
                                sb4.append(0);
                                sb4.append(str7);
                                str6 = sb4.toString();
                            }
                            if (this.mSession.trackExists(1)) {
                                StringBuilder sb5 = new StringBuilder();
                                sb5.append(str6);
                                sb5.append(str9);
                                sb5.append(this.mClient.getLocalAddress().getHostAddress());
                                sb5.append(str2);
                                sb5.append(this.mClient.getLocalPort());
                                sb5.append(str8);
                                sb5.append(1);
                                sb5.append(str7);
                                str6 = sb5.toString();
                            }
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append(str6.substring(0, str6.length() - 1));
                            sb6.append("\r\nSession: 1185d20035702ca\r\n");
                            sb = sb6.toString();
                        } else if (!request.method.equalsIgnoreCase("PAUSE") && !request.method.equalsIgnoreCase("TEARDOWN")) {
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append("Command unknown: ");
                            sb7.append(request);
                            Log.e(RtspServer.TAG, sb7.toString());
                            response.status = str4;
                        }
                    }
                    response.attributes = sb;
                }
                response.status = str3;
            } else {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("WWW-Authenticate: Basic realm=\"");
                sb8.append(RtspServer.SERVER_NAME);
                sb8.append("\"\r\n");
                response.attributes = sb8.toString();
                response.status = "401 Unauthorized";
            }
            return response;
        }

        public void run() {
            Response response;
            StringBuilder sb = new StringBuilder();
            sb.append("Connection from ");
            sb.append(this.mClient.getInetAddress().getHostAddress());
            String sb2 = sb.toString();
            String str = RtspServer.TAG;
            Log.i(str, sb2);
            while (!Thread.interrupted()) {
                Request request = null;
                try {
                    response = null;
                    request = Request.parseRequest(this.mInput);
                } catch (SocketException unused) {
                } catch (Exception unused2) {
                    response = new Response();
                    response.status = "400 Bad Request";
                }
                if (request != null) {
                    try {
                        response = processRequest(request);
                    } catch (Exception e) {
                        RtspServer.this.postError(e, 1);
                        Log.e(str, e.getMessage() != null ? e.getMessage() : "An error occurred");
                        e.printStackTrace();
                        response = new Response(request);
                    }
                }
                try {
                    response.send(this.mOutput);
                } catch (IOException unused3) {
                    Log.e(str, "Response was not sent properly");
                }
            }
            boolean isStreaming = RtspServer.this.isStreaming();
            this.mSession.syncStop();
            if (isStreaming && !RtspServer.this.isStreaming()) {
                RtspServer.this.postMessage(1);
            }
            this.mSession.release();
            try {
                this.mClient.close();
            } catch (IOException unused4) {
            }
            Log.i(str, "Client disconnected");
        }
    }

    public void addCallbackListener(CallbackListener callbackListener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    if (((CallbackListener) it.next()) == callbackListener) {
                        return;
                    }
                }
            }
            this.mListeners.add(callbackListener);
        }
    }

    public long getBitrate() {
        long j = 0;
        for (Session session : this.mSessions.keySet()) {
            if (session != null && session.isStreaming()) {
                j += session.getBitrate();
            }
        }
        return j;
    }

    public int getPort() {
        return this.mPort;
    }

    /* access modifiers changed from: protected */
    public Session handleRequest(String str, Socket socket) {
        Session parse = UriParser.parse(str);
        parse.setOrigin(socket.getLocalAddress().getHostAddress());
        if (parse.getDestination() == null) {
            parse.setDestination(socket.getInetAddress().getHostAddress());
        }
        return parse;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public boolean isStreaming() {
        for (Session session : this.mSessions.keySet()) {
            if (session != null && session.isStreaming()) {
                return true;
            }
        }
        return false;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public void onCreate() {
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.mPort = Integer.parseInt(this.mSharedPreferences.getString(KEY_PORT, String.valueOf(this.mPort)));
        this.mEnabled = this.mSharedPreferences.getBoolean(KEY_ENABLED, this.mEnabled);
        this.mSharedPreferences.registerOnSharedPreferenceChangeListener(this.mOnSharedPreferenceChangeListener);
        start();
    }

    public void onDestroy() {
        stop();
        this.mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this.mOnSharedPreferenceChangeListener);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public void postError(Exception exc, int i) {
        synchronized (this.mListeners) {
            if (!this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    ((CallbackListener) it.next()).onError(this, exc, i);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void postMessage(int i) {
        synchronized (this.mListeners) {
            if (!this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    ((CallbackListener) it.next()).onMessage(this, i);
                }
            }
        }
    }

    public void removeCallbackListener(CallbackListener callbackListener) {
        synchronized (this.mListeners) {
            this.mListeners.remove(callbackListener);
        }
    }

    public void setAuthorization(String str, String str2) {
        this.mUsername = str;
        this.mPassword = str2;
    }

    public void setPort(int i) {
        Editor edit = this.mSharedPreferences.edit();
        edit.putString(KEY_PORT, String.valueOf(i));
        edit.commit();
    }

    public void start() {
        if (!this.mEnabled || this.mRestart) {
            stop();
        }
        if (this.mEnabled && this.mListenerThread == null) {
            try {
                this.mListenerThread = new RequestListener();
            } catch (Exception unused) {
                this.mListenerThread = null;
            }
        }
        this.mRestart = false;
    }

    public void stop() {
        RequestListener requestListener = this.mListenerThread;
        if (requestListener != null) {
            try {
                requestListener.kill();
                for (Session session : this.mSessions.keySet()) {
                    if (session != null && session.isStreaming()) {
                        session.stop();
                    }
                }
            } catch (Exception unused) {
            } catch (Throwable th) {
                this.mListenerThread = null;
                throw th;
            }
            this.mListenerThread = null;
        }
    }
}
