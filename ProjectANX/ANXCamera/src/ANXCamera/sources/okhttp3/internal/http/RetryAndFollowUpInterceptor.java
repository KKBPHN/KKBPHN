package okhttp3.internal.http;

import com.android.camera.network.net.base.HTTP;
import com.ss.android.vesdk.runtime.cloudconfig.HttpRequest;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpRetryException;
import java.net.ProtocolException;
import java.net.Proxy.Type;
import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.CertificatePinner;
import okhttp3.EventListener;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.RouteException;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.http2.ConnectionShutdownException;

public final class RetryAndFollowUpInterceptor implements Interceptor {
    private static final int MAX_FOLLOW_UPS = 20;
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private StreamAllocation streamAllocation;

    public RetryAndFollowUpInterceptor(OkHttpClient okHttpClient, boolean z) {
        this.client = okHttpClient;
        this.forWebSocket = z;
    }

    private Address createAddress(HttpUrl httpUrl) {
        CertificatePinner certificatePinner;
        HostnameVerifier hostnameVerifier;
        SSLSocketFactory sSLSocketFactory;
        if (httpUrl.isHttps()) {
            SSLSocketFactory sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            sSLSocketFactory = sslSocketFactory;
            certificatePinner = this.client.certificatePinner();
        } else {
            sSLSocketFactory = null;
            hostnameVerifier = null;
            certificatePinner = null;
        }
        Address address = new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sSLSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
        return address;
    }

    private Request followUpRequest(Response response) {
        if (response != null) {
            RealConnection connection = this.streamAllocation.connection();
            RequestBody requestBody = null;
            Route route = connection != null ? connection.route() : null;
            int code = response.code();
            String method = response.request().method();
            String str = "GET";
            if (code == 307 || code == 308) {
                if (!method.equals(str) && !method.equals(HttpRequest.METHOD_HEAD)) {
                    return null;
                }
            } else if (code == 401) {
                return this.client.authenticator().authenticate(route, response);
            } else {
                if (code == 407) {
                    if ((route != null ? route.proxy() : this.client.proxy()).type() == Type.HTTP) {
                        return this.client.proxyAuthenticator().authenticate(route, response);
                    }
                    throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                } else if (code != 408) {
                    switch (code) {
                        case 300:
                        case 301:
                        case 302:
                        case 303:
                            break;
                        default:
                            return null;
                    }
                } else if (!this.client.retryOnConnectionFailure() || (response.request().body() instanceof UnrepeatableRequestBody)) {
                    return null;
                } else {
                    if (response.priorResponse() == null || response.priorResponse().code() != 408) {
                        return response.request();
                    }
                    return null;
                }
            }
            if (!this.client.followRedirects()) {
                return null;
            }
            String header = response.header(HttpRequest.HEADER_LOCATION);
            if (header == null) {
                return null;
            }
            HttpUrl resolve = response.request().url().resolve(header);
            if (resolve == null) {
                return null;
            }
            if (!resolve.scheme().equals(response.request().url().scheme()) && !this.client.followSslRedirects()) {
                return null;
            }
            Builder newBuilder = response.request().newBuilder();
            if (HttpMethod.permitsRequestBody(method)) {
                boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
                if (HttpMethod.redirectsToGet(method)) {
                    newBuilder.method(str, null);
                } else {
                    if (redirectsWithBody) {
                        requestBody = response.request().body();
                    }
                    newBuilder.method(method, requestBody);
                }
                if (!redirectsWithBody) {
                    newBuilder.removeHeader(HTTP.TRANSFER_ENCODING);
                    newBuilder.removeHeader("Content-Length");
                    newBuilder.removeHeader("Content-Type");
                }
            }
            if (!sameConnection(response, resolve)) {
                newBuilder.removeHeader(HttpRequest.HEADER_AUTHORIZATION);
            }
            return newBuilder.url(resolve).build();
        }
        throw new IllegalStateException();
    }

    private boolean isRecoverable(IOException iOException, boolean z) {
        boolean z2 = false;
        if (iOException instanceof ProtocolException) {
            return false;
        }
        if (!(iOException instanceof InterruptedIOException)) {
            return (!(iOException instanceof SSLHandshakeException) || !(iOException.getCause() instanceof CertificateException)) && !(iOException instanceof SSLPeerUnverifiedException);
        }
        if ((iOException instanceof SocketTimeoutException) && !z) {
            z2 = true;
        }
        return z2;
    }

    private boolean recover(IOException iOException, boolean z, Request request) {
        this.streamAllocation.streamFailed(iOException);
        if (!this.client.retryOnConnectionFailure()) {
            return false;
        }
        return (!z || !(request.body() instanceof UnrepeatableRequestBody)) && isRecoverable(iOException, z) && this.streamAllocation.hasMoreRoutes();
    }

    private boolean sameConnection(Response response, HttpUrl httpUrl) {
        HttpUrl url = response.request().url();
        return url.host().equals(httpUrl.host()) && url.port() == httpUrl.port() && url.scheme().equals(httpUrl.scheme());
    }

    public void cancel() {
        this.canceled = true;
        StreamAllocation streamAllocation2 = this.streamAllocation;
        if (streamAllocation2 != null) {
            streamAllocation2.cancel();
        }
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [okhttp3.Request] */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: type inference failed for: r1v4, types: [okhttp3.Response] */
    /* JADX WARNING: type inference failed for: r0v1, types: [okhttp3.Request] */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v3, types: [okhttp3.Request] */
    /* JADX WARNING: type inference failed for: r0v4, types: [okhttp3.Request] */
    /* JADX WARNING: type inference failed for: r0v6, types: [okhttp3.Response] */
    /* JADX WARNING: type inference failed for: r0v7, types: [okhttp3.Response, java.lang.Object] */
    /* JADX WARNING: type inference failed for: r11v0, types: [okhttp3.Request] */
    /* JADX WARNING: type inference failed for: r1v12 */
    /* JADX WARNING: type inference failed for: r0v10 */
    /* JADX WARNING: type inference failed for: r0v14, types: [okhttp3.Response] */
    /* JADX WARNING: type inference failed for: r0v15 */
    /* JADX WARNING: type inference failed for: r1v23 */
    /* JADX WARNING: type inference failed for: r0v16 */
    /* JADX WARNING: type inference failed for: r1v24 */
    /* JADX WARNING: type inference failed for: r0v17 */
    /* JADX WARNING: type inference failed for: r0v18 */
    /* JADX WARNING: type inference failed for: r0v19 */
    /* JADX WARNING: type inference failed for: r0v20 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r1v5
  assigns: []
  uses: []
  mth insns count: 125
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
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Response intercept(Chain chain) {
        ? r0;
        ? r02;
        ? request = chain.request();
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        Call call = realInterceptorChain.call();
        EventListener eventListener = realInterceptorChain.eventListener();
        StreamAllocation streamAllocation2 = new StreamAllocation(this.client.connectionPool(), createAddress(request.url()), call, eventListener, this.callStackTrace);
        this.streamAllocation = streamAllocation2;
        int i = 0;
        ? r1 = 0;
        ? r03 = request;
        while (!this.canceled) {
            try {
                ? r04 = r03;
                ? proceed = realInterceptorChain.proceed(r03, this.streamAllocation, null, null);
                if (r1 != 0) {
                    proceed = proceed.newBuilder().priorResponse(r1.newBuilder().body(null).build()).build();
                }
                ? followUpRequest = followUpRequest(proceed);
                if (followUpRequest == 0) {
                    if (!this.forWebSocket) {
                        this.streamAllocation.release();
                    }
                    return proceed;
                }
                Util.closeQuietly((Closeable) proceed.body());
                int i2 = i + 1;
                if (i2 > 20) {
                    this.streamAllocation.release();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Too many follow-up requests: ");
                    sb.append(i2);
                    throw new ProtocolException(sb.toString());
                } else if (!(followUpRequest.body() instanceof UnrepeatableRequestBody)) {
                    if (!sameConnection(proceed, followUpRequest.url())) {
                        this.streamAllocation.release();
                        StreamAllocation streamAllocation3 = new StreamAllocation(this.client.connectionPool(), createAddress(followUpRequest.url()), call, eventListener, this.callStackTrace);
                        this.streamAllocation = streamAllocation3;
                    } else if (this.streamAllocation.codec() != null) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Closing the body of ");
                        sb2.append(proceed);
                        sb2.append(" didn't close its backing stream. Bad interceptor?");
                        throw new IllegalStateException(sb2.toString());
                    }
                    r1 = proceed;
                    r0 = followUpRequest;
                    i = i2;
                    r1 = r1;
                    r03 = r0;
                } else {
                    this.streamAllocation.release();
                    throw new HttpRetryException("Cannot retry streamed HTTP body", proceed.code());
                }
            } catch (RouteException e) {
                if (recover(e.getLastConnectException(), false, r03)) {
                    r1 = r1;
                    r0 = r03;
                } else {
                    throw e.getLastConnectException();
                }
            } catch (IOException e2) {
                if (recover(e2, !(e2 instanceof ConnectionShutdownException), r02)) {
                    r0 = r02;
                } else {
                    throw e2;
                }
            } catch (Throwable th) {
                this.streamAllocation.streamFailed(null);
                this.streamAllocation.release();
                throw th;
            }
        }
        this.streamAllocation.release();
        throw new IOException("Canceled");
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCallStackTrace(Object obj) {
        this.callStackTrace = obj;
    }

    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
}
