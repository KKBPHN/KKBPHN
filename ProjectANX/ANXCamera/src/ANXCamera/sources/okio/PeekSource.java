package okio;

final class PeekSource implements Source {
    private final Buffer buffer;
    private boolean closed;
    private int expectedPos;
    private Segment expectedSegment = this.buffer.head;
    private long pos;
    private final BufferedSource upstream;

    PeekSource(BufferedSource bufferedSource) {
        this.upstream = bufferedSource;
        this.buffer = bufferedSource.buffer();
        Segment segment = this.expectedSegment;
        this.expectedPos = segment != null ? segment.pos : -1;
    }

    public void close() {
        this.closed = true;
    }

    public long read(Buffer buffer2, long j) {
        int i = (j > 0 ? 1 : (j == 0 ? 0 : -1));
        if (i < 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("byteCount < 0: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        } else if (!this.closed) {
            Segment segment = this.expectedSegment;
            if (segment != null) {
                Segment segment2 = this.buffer.head;
                if (!(segment == segment2 && this.expectedPos == segment2.pos)) {
                    throw new IllegalStateException("Peek source is invalid because upstream source was used");
                }
            }
            if (i == 0) {
                return 0;
            }
            if (!this.upstream.request(this.pos + 1)) {
                return -1;
            }
            if (this.expectedSegment == null) {
                Segment segment3 = this.buffer.head;
                if (segment3 != null) {
                    this.expectedSegment = segment3;
                    this.expectedPos = segment3.pos;
                }
            }
            long min = Math.min(j, this.buffer.size - this.pos);
            this.buffer.copyTo(buffer2, this.pos, min);
            this.pos += min;
            return min;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public Timeout timeout() {
        return this.upstream.timeout();
    }
}
