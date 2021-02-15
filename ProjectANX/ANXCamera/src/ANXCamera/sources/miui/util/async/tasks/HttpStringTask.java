package miui.util.async.tasks;

import java.util.Map;
import miui.net.http.HttpSession;
import miui.util.async.tasks.HttpTask.Method;

public class HttpStringTask extends HttpTextTask {
    public HttpStringTask(String str) {
        this(null, Method.Get, str, null);
    }

    public HttpStringTask(HttpSession httpSession, String str) {
        this(httpSession, Method.Get, str, null);
    }

    public HttpStringTask(HttpSession httpSession, Method method, String str, Map map) {
        super(httpSession, method, str, map);
    }

    public String doLoad() {
        return requestText();
    }
}
