package okio;

public abstract class ForwardingSource implements Source {
    private final Source delegate;

    public ForwardingSource(Source source) {
        if (source != null) {
            this.delegate = source;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    public void close() {
        this.delegate.close();
    }

    public final Source delegate() {
        return this.delegate;
    }

    public long read(Buffer buffer, long j) {
        return this.delegate.read(buffer, j);
    }

    public Timeout timeout() {
        return this.delegate.timeout();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("(");
        sb.append(this.delegate.toString());
        sb.append(")");
        return sb.toString();
    }
}
