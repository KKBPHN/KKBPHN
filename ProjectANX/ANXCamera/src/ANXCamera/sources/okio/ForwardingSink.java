package okio;

public abstract class ForwardingSink implements Sink {
    private final Sink delegate;

    public ForwardingSink(Sink sink) {
        if (sink != null) {
            this.delegate = sink;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    public void close() {
        this.delegate.close();
    }

    public final Sink delegate() {
        return this.delegate;
    }

    public void flush() {
        this.delegate.flush();
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

    public void write(Buffer buffer, long j) {
        this.delegate.write(buffer, j);
    }
}
