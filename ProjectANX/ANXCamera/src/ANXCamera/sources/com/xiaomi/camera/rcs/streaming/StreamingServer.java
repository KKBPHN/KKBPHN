package com.xiaomi.camera.rcs.streaming;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.Surface;
import androidx.core.app.NotificationCompat;
import com.android.camera.R;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import com.xiaomi.camera.rcs.RemoteControlContract;
import com.xiaomi.camera.rcs.util.RCSDebug;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.majorkernelpanic.streaming.Session;
import net.majorkernelpanic.streaming.SessionBuilder;
import net.majorkernelpanic.streaming.rtsp.UriParser;
import net.majorkernelpanic.streaming.video.VideoQuality;

public class StreamingServer {
    public static final String SERVER_NAME = "XIAOMI-CAMERA-LIVE-STREAMING-SERVER";
    public static final int SERVER_STATE_BIND_FAILED = 1;
    public static final int SERVER_STATE_BIND_SUCCEED = 2;
    public static final int SERVER_STATE_START_FAILED = 1;
    public static final int SESSION_STATE_STREAMING_STARTED = 2;
    public static final int SESSION_STATE_STREAMING_STOPPED = 3;
    /* access modifiers changed from: private */
    public static final String TAG = RCSDebug.createTag(StreamingServer.class);
    private static final int sAudioEncoder = 0;
    private static final int sVideoEncoder = 1;
    private static final VideoQuality sVideoQuality = new VideoQuality(480, 640, 30, 500000);
    private boolean mEnabled;
    private RequestListener mListenerThread;
    private final LinkedList mListeners = new LinkedList();
    /* access modifiers changed from: private */
    public String mPassword;
    /* access modifiers changed from: private */
    public int mPort;
    private boolean mRestart;
    /* access modifiers changed from: private */
    public WeakHashMap mSessions = new WeakHashMap(2);
    /* access modifiers changed from: private */
    public String mUsername;

    class Request {
        public static final Pattern regexMethod = Pattern.compile("(\\w+) (\\S+) RTSP", 2);
        public static final Pattern rexegHeader = Pattern.compile("(\\S+):(.+)", 2);
        public HashMap headers = new HashMap();
        public String method;
        public String uri;

        Request() {
        }

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
                    if (readLine == null || readLine.length() <= 3) {
                        String access$100 = StreamingServer.TAG;
                    } else {
                        Matcher matcher2 = rexegHeader.matcher(readLine);
                        matcher2.find();
                        request.headers.put(matcher2.group(1).toLowerCase(Locale.US), matcher2.group(2));
                    }
                }
                String access$1002 = StreamingServer.TAG;
                if (readLine != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(request.method);
                    sb.append(" ");
                    sb.append(request.uri);
                    RCSDebug.e(access$1002, sb.toString());
                    return request;
                }
                RCSDebug.d(access$1002, "parseRequest: read null from input: header");
                throw new SocketException(str);
            }
            RCSDebug.d(StreamingServer.TAG, "parseRequest: read null from input: method");
            throw new SocketException(str);
        }
    }

    class RequestListener extends Thread implements Runnable {
        private final Object mLock = new Object();
        private volatile boolean mReady = false;
        private final ServerSocket mServer;

        public RequestListener() {
            try {
                this.mServer = new ServerSocket(StreamingServer.this.mPort);
                start();
                RCSDebug.d(StreamingServer.TAG, "ServerSocket created");
            } catch (BindException e) {
                e.printStackTrace();
                RCSDebug.e(StreamingServer.TAG, "Port already in use !", e);
                StreamingServer.this.postStreamingServerState(1);
                throw e;
            }
        }

        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x000c */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void kill() {
            synchronized (this.mLock) {
                this.mReady = false;
            }
            this.mServer.close();
            try {
                join();
            } catch (InterruptedException unused) {
            }
        }

        public void run() {
            synchronized (this.mLock) {
                StringBuilder sb = new StringBuilder();
                sb.append("rtsp://");
                sb.append(StreamingServer.this.getIPAddress(true));
                sb.append(":");
                sb.append(this.mServer.getLocalPort());
                String sb2 = sb.toString();
                String access$100 = StreamingServer.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Rtsp server started: ");
                sb3.append(sb2);
                RCSDebug.i(access$100, sb3.toString());
                this.mReady = true;
                this.mLock.notifyAll();
            }
            while (!Thread.interrupted()) {
                try {
                    new WorkerThread(this.mServer.accept()).start();
                } catch (SocketException unused) {
                } catch (IOException e) {
                    String access$1002 = StreamingServer.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Rtsp server listening failed: ");
                    sb4.append(e);
                    RCSDebug.e(access$1002, sb4.toString());
                }
            }
            synchronized (this.mLock) {
                this.mReady = false;
            }
            RCSDebug.i(StreamingServer.TAG, "RTSP server stopped");
        }

        public void waitUtilReady() {
            synchronized (this.mLock) {
                while (!this.mReady) {
                    try {
                        this.mLock.wait();
                    } catch (InterruptedException e) {
                        String access$100 = StreamingServer.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("waitUntilReady() interrupted: ");
                        sb.append(e);
                        RCSDebug.e(access$100, sb.toString());
                    }
                }
            }
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
            String str2 = "";
            try {
                i = Integer.parseInt(((String) this.mRequest.headers.get("cseq")).replace(" ", str2));
            } catch (Exception e) {
                String access$100 = StreamingServer.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Error parsing CSeq: ");
                sb.append(e.getMessage() != null ? e.getMessage() : str2);
                RCSDebug.e(access$100, sb.toString());
                i = -1;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("RTSP/1.0 ");
            sb2.append(this.status);
            sb2.append("\r\nServer: ");
            sb2.append(StreamingServer.SERVER_NAME);
            String str3 = "\r\n";
            sb2.append(str3);
            if (i >= 0) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Cseq: ");
                sb3.append(i);
                sb3.append(str3);
                str = sb3.toString();
            } else {
                str = str2;
            }
            sb2.append(str);
            sb2.append("Content-Length: ");
            sb2.append(this.content.length());
            sb2.append(str3);
            sb2.append(this.attributes);
            sb2.append(str3);
            sb2.append(this.content);
            String sb4 = sb2.toString();
            RCSDebug.d(StreamingServer.TAG, sb4.replace("\r", str2));
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
            if (StreamingServer.this.mUsername == null || StreamingServer.this.mPassword == null || StreamingServer.this.mUsername.isEmpty()) {
                return true;
            }
            if (str != null && !str.isEmpty()) {
                String substring = str.substring(str.lastIndexOf(" ") + 1);
                StringBuilder sb = new StringBuilder();
                sb.append(StreamingServer.this.mUsername);
                sb.append(":");
                sb.append(StreamingServer.this.mPassword);
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
                    this.mSession = StreamingServer.this.handleRequest(request.uri, this.mClient);
                    String access$100 = StreamingServer.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("create new session: ");
                    sb2.append(this.mSession);
                    RCSDebug.d(access$100, sb2.toString());
                    StreamingServer.this.mSessions.put(this.mSession, null);
                    this.mSession.syncConfigure();
                    String sessionDescription = this.mSession.getSessionDescription();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Content-Base: ");
                    sb3.append(this.mClient.getLocalAddress().getHostAddress());
                    sb3.append(str2);
                    sb3.append(this.mClient.getLocalPort());
                    sb3.append("/\r\nContent-Type: application/sdp\r\n");
                    response.attributes = sb3.toString();
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
                                i2 = destinationPorts[0];
                                i = destinationPorts[1];
                            } else {
                                i2 = Integer.parseInt(matcher2.group(1));
                                i = matcher2.group(2) == null ? i2 + 1 : Integer.parseInt(matcher2.group(2));
                            }
                            int ssrc = this.mSession.getTrack(parseInt).getSSRC();
                            int[] localPorts = this.mSession.getTrack(parseInt).getLocalPorts();
                            String destination = this.mSession.getDestination();
                            this.mSession.getTrack(parseInt).setDestinationPorts(i2, i);
                            boolean isStreaming = StreamingServer.this.isStreaming();
                            String access$1002 = StreamingServer.TAG;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("SETUP -> before: ");
                            sb4.append(isStreaming);
                            RCSDebug.d(access$1002, sb4.toString());
                            this.mSession.syncStart(parseInt);
                            String access$1003 = StreamingServer.TAG;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append("SETUP ->  after: ");
                            sb5.append(StreamingServer.this.isStreaming());
                            RCSDebug.d(access$1003, sb5.toString());
                            if (!isStreaming && StreamingServer.this.isStreaming()) {
                                RCSDebug.d(StreamingServer.TAG, "SETUP -> send STATE_STREAMING_STARTED");
                                StreamingServer.this.postStreamingSessionState(this.mSession, 2);
                            }
                            StringBuilder sb6 = new StringBuilder();
                            sb6.append("Transport: RTP/AVP/UDP;");
                            sb6.append(InetAddress.getByName(destination).isMulticastAddress() ? "multicast" : "unicast");
                            sb6.append(";destination=");
                            sb6.append(this.mSession.getDestination());
                            sb6.append(";client_port=");
                            sb6.append(i2);
                            String str5 = "-";
                            sb6.append(str5);
                            sb6.append(i);
                            sb6.append(";server_port=");
                            sb6.append(localPorts[0]);
                            sb6.append(str5);
                            sb6.append(localPorts[1]);
                            sb6.append(";ssrc=");
                            sb6.append(Integer.toHexString(ssrc));
                            sb6.append(";mode=play\r\nSession: 1185d20035702ca\r\nCache-Control: no-cache\r\n");
                            response.attributes = sb6.toString();
                            response.status = str3;
                        } else if (request.method.equalsIgnoreCase("PLAY")) {
                            String str6 = "RTP-Info: ";
                            String str7 = ";seq=0,";
                            String str8 = "/trackID=";
                            String str9 = "url=rtsp://";
                            if (this.mSession.trackExists(0)) {
                                StringBuilder sb7 = new StringBuilder();
                                sb7.append(str6);
                                sb7.append(str9);
                                sb7.append(this.mClient.getLocalAddress().getHostAddress());
                                sb7.append(str2);
                                sb7.append(this.mClient.getLocalPort());
                                sb7.append(str8);
                                sb7.append(0);
                                sb7.append(str7);
                                str6 = sb7.toString();
                            }
                            if (this.mSession.trackExists(1)) {
                                StringBuilder sb8 = new StringBuilder();
                                sb8.append(str6);
                                sb8.append(str9);
                                sb8.append(this.mClient.getLocalAddress().getHostAddress());
                                sb8.append(str2);
                                sb8.append(this.mClient.getLocalPort());
                                sb8.append(str8);
                                sb8.append(1);
                                sb8.append(str7);
                                str6 = sb8.toString();
                            }
                            StringBuilder sb9 = new StringBuilder();
                            sb9.append(str6.substring(0, str6.length() - 1));
                            sb9.append("\r\nSession: 1185d20035702ca\r\n");
                            sb = sb9.toString();
                        } else if (!request.method.equalsIgnoreCase("PAUSE")) {
                            if (request.method.equalsIgnoreCase("TEARDOWN")) {
                                String access$1004 = StreamingServer.TAG;
                                StringBuilder sb10 = new StringBuilder();
                                sb10.append("TEARDOWN -> is streaming: ");
                                sb10.append(StreamingServer.this.isStreaming());
                                RCSDebug.d(access$1004, sb10.toString());
                            } else {
                                String access$1005 = StreamingServer.TAG;
                                StringBuilder sb11 = new StringBuilder();
                                sb11.append("Command unknown: ");
                                sb11.append(request);
                                RCSDebug.e(access$1005, sb11.toString());
                                response.status = str4;
                            }
                        }
                    }
                    response.attributes = sb;
                }
                response.status = str3;
            } else {
                response.attributes = "WWW-Authenticate: Basic realm=\"XIAOMI-CAMERA-LIVE-STREAMING-SERVER\"\r\n";
                response.status = "401 Unauthorized";
            }
            return response;
        }

        public void run() {
            Response response;
            String access$100 = StreamingServer.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Connection from ");
            sb.append(this.mClient.getInetAddress().getHostAddress());
            RCSDebug.i(access$100, sb.toString());
            while (!Thread.interrupted()) {
                Request request = null;
                try {
                    response = null;
                    request = Request.parseRequest(this.mInput);
                } catch (SocketException unused) {
                    RCSDebug.d(StreamingServer.TAG, "Client has left");
                } catch (Exception unused2) {
                    RCSDebug.d(StreamingServer.TAG, "Unknown request from client");
                    response = new Response();
                    response.status = "400 Bad Request";
                }
                if (request != null) {
                    try {
                        response = processRequest(request);
                    } catch (Exception e) {
                        RCSDebug.e(StreamingServer.TAG, e.getMessage() != null ? e.getMessage() : "An error occurred");
                        e.printStackTrace();
                        StreamingServer.this.postStreamingServerState(1);
                        response = new Response(request);
                    }
                }
                try {
                    response.send(this.mOutput);
                } catch (IOException unused3) {
                    RCSDebug.e(StreamingServer.TAG, "Response was not sent properly");
                }
            }
            boolean isStreaming = StreamingServer.this.isStreaming();
            this.mSession.syncStop();
            if (isStreaming && !StreamingServer.this.isStreaming()) {
                StreamingServer.this.postStreamingSessionState(this.mSession, 3);
            }
            StreamingServer.this.mSessions.remove(this.mSession);
            this.mSession.release();
            try {
                this.mClient.close();
            } catch (IOException unused4) {
            }
            RCSDebug.i(StreamingServer.TAG, "Client disconnected");
        }
    }

    public StreamingServer(Context context) {
        this.mPort = context.getResources().getInteger(R.integer.camera_streaming_server_port);
        this.mEnabled = C0122O00000o.instance().OO0o0Oo();
        this.mRestart = false;
        SessionBuilder.getInstance().setContext(context).setAudioEncoder(0).setVideoEncoder(1).setVideoQuality(sVideoQuality);
    }

    /* access modifiers changed from: private */
    public final String getIPAddress(boolean z) {
        try {
            for (NetworkInterface inetAddresses : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                Iterator it = Collections.list(inetAddresses.getInetAddresses()).iterator();
                while (true) {
                    if (it.hasNext()) {
                        InetAddress inetAddress = (InetAddress) it.next();
                        if (!inetAddress.isLoopbackAddress()) {
                            String hostAddress = inetAddress.getHostAddress();
                            boolean z2 = hostAddress.indexOf(58) < 0;
                            if (z) {
                                if (z2) {
                                    return hostAddress;
                                }
                            } else if (!z2) {
                                int indexOf = hostAddress.indexOf(37);
                                return indexOf < 0 ? hostAddress.toUpperCase(Locale.ENGLISH) : hostAddress.substring(0, indexOf).toUpperCase(Locale.ENGLISH);
                            }
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        return "0.0.0.0";
    }

    public void addCallbackListener(StreamingStateCallback streamingStateCallback) {
        synchronized (this.mListeners) {
            if (!this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    if (((StreamingStateCallback) it.next()) == streamingStateCallback) {
                        return;
                    }
                }
            }
            this.mListeners.add(streamingStateCallback);
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

    public Session getStreamingSession() {
        for (Session session : this.mSessions.keySet()) {
            if (session != null && session.isStreaming()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("session ");
                sb.append(session);
                sb.append(" is streaming");
                RCSDebug.d(str, sb.toString());
                return session;
            }
        }
        return null;
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
        return getStreamingSession() != null;
    }

    public void postStreamingServerState(int i) {
        synchronized (this.mListeners) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("postStreamingServerState: ");
            sb.append(i);
            RCSDebug.d(str, sb.toString());
            if (!this.mListeners.isEmpty()) {
                Bundle bundle = new Bundle();
                RemoteControlContract.setStreamingServerPort(bundle, this.mPort);
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    StreamingStateCallback streamingStateCallback = (StreamingStateCallback) it.next();
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handle for ");
                    sb2.append(i);
                    sb2.append(" -> ");
                    sb2.append(streamingStateCallback);
                    RCSDebug.d(str2, sb2.toString());
                    streamingStateCallback.onStreamingServerStateChanged(i, bundle);
                }
            } else {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("no handle for ");
                sb3.append(i);
                RCSDebug.d(str3, sb3.toString());
            }
        }
    }

    public void postStreamingSessionState(Session session, int i) {
        synchronized (this.mListeners) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("postStreamingSessionState: ");
            sb.append(i);
            RCSDebug.d(str, sb.toString());
            if (!this.mListeners.isEmpty()) {
                Bundle bundle = new Bundle();
                if (i == 2) {
                    int i2 = session.getVideoTrack().getVideoQuality().resX;
                    int i3 = session.getVideoTrack().getVideoQuality().resY;
                    Surface codecInputSurface = session.getVideoTrack().getCodecInputSurface();
                    RemoteControlContract.setVideoStreamSize(bundle, i2, i3);
                    RemoteControlContract.setCodecInputSurface(bundle, codecInputSurface);
                }
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    StreamingStateCallback streamingStateCallback = (StreamingStateCallback) it.next();
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handle for ");
                    sb2.append(i);
                    sb2.append(" -> ");
                    sb2.append(streamingStateCallback);
                    RCSDebug.d(str2, sb2.toString());
                    streamingStateCallback.onStreamingSessionStateChanged(i, bundle);
                }
            } else {
                String str3 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("no handle for ");
                sb3.append(i);
                RCSDebug.d(str3, sb3.toString());
            }
        }
    }

    public void removeCallbackListener(StreamingStateCallback streamingStateCallback) {
        synchronized (this.mListeners) {
            this.mListeners.remove(streamingStateCallback);
        }
    }

    public void setAuthorization(String str, String str2) {
        this.mUsername = str;
        this.mPassword = str2;
    }

    public void setEnabled(boolean z) {
        this.mEnabled = z;
        start();
    }

    public void setPort(int i) {
        if (i != this.mPort) {
            this.mPort = i;
            this.mRestart = true;
            start();
        }
    }

    public void start() {
        if (!this.mEnabled || this.mRestart) {
            stop();
        }
        if (this.mEnabled && this.mListenerThread == null) {
            try {
                this.mListenerThread = new RequestListener();
                this.mListenerThread.waitUtilReady();
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

    public boolean waitUtilConnectable() {
        RequestListener requestListener = this.mListenerThread;
        if (requestListener == null) {
            return false;
        }
        requestListener.waitUtilReady();
        return true;
    }
}
