package okhttp3.internal.http;

import com.android.camera.network.net.base.HTTP;
import java.net.ProtocolException;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Response.Builder;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.connection.StreamAllocation;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public final class CallServerInterceptor implements Interceptor {
    private final boolean forWebSocket;

    final class CountingSink extends ForwardingSink {
        long successfulCount;

        CountingSink(Sink sink) {
            super(sink);
        }

        public void write(Buffer buffer, long j) {
            super.write(buffer, j);
            this.successfulCount += j;
        }
    }

    public CallServerInterceptor(boolean z) {
        this.forWebSocket = z;
    }

    public Response intercept(Chain chain) {
        Builder builder;
        ResponseBody responseBody;
        RealInterceptorChain realInterceptorChain = (RealInterceptorChain) chain;
        HttpCodec httpStream = realInterceptorChain.httpStream();
        StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        RealConnection realConnection = (RealConnection) realInterceptorChain.connection();
        Request request = realInterceptorChain.request();
        long currentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        Builder builder2 = null;
        if (HttpMethod.permitsRequestBody(request.method()) && request.body() != null) {
            if (HTTP.EXPECT_CONTINUE.equalsIgnoreCase(request.header(HTTP.EXPECT_DIRECTIVE))) {
                httpStream.flushRequest();
                realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                builder2 = httpStream.readResponseHeaders(true);
            }
            if (builder2 == null) {
                realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                CountingSink countingSink = new CountingSink(httpStream.createRequestBody(request, request.body().contentLength()));
                BufferedSink buffer = Okio.buffer((Sink) countingSink);
                request.body().writeTo(buffer);
                buffer.close();
                realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
            } else if (!realConnection.isMultiplexed()) {
                streamAllocation.noNewStreams();
            }
        }
        httpStream.finishRequest();
        if (builder2 == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            builder2 = httpStream.readResponseHeaders(false);
        }
        Response build = builder2.request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), build);
        int code = build.code();
        if (!this.forWebSocket || code != 101) {
            builder = build.newBuilder();
            responseBody = httpStream.openResponseBody(build);
        } else {
            builder = build.newBuilder();
            responseBody = Util.EMPTY_RESPONSE;
        }
        Response build2 = builder.body(responseBody).build();
        Request request2 = build2.request();
        String str = HTTP.CONN_DIRECTIVE;
        String str2 = "close";
        if (str2.equalsIgnoreCase(request2.header(str)) || str2.equalsIgnoreCase(build2.header(str))) {
            streamAllocation.noNewStreams();
        }
        if ((code != 204 && code != 205) || build2.body().contentLength() <= 0) {
            return build2;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP ");
        sb.append(code);
        sb.append(" had non-zero Content-Length: ");
        sb.append(build2.body().contentLength());
        throw new ProtocolException(sb.toString());
    }
}
