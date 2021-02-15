package okhttp3;

import java.util.Collections;
import java.util.List;

public interface CookieJar {
    public static final CookieJar NO_COOKIES = new CookieJar() {
        public List loadForRequest(HttpUrl httpUrl) {
            return Collections.emptyList();
        }

        public void saveFromResponse(HttpUrl httpUrl, List list) {
        }
    };

    List loadForRequest(HttpUrl httpUrl);

    void saveFromResponse(HttpUrl httpUrl, List list);
}
