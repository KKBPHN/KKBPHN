package okhttp3;

import javax.annotation.Nullable;

public interface Authenticator {
    public static final Authenticator NONE = new Authenticator() {
        public Request authenticate(Route route, Response response) {
            return null;
        }
    };

    @Nullable
    Request authenticate(Route route, Response response);
}
