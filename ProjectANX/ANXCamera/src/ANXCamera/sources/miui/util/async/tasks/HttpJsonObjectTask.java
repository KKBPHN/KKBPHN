package miui.util.async.tasks;

import java.util.Map;
import miui.net.http.HttpSession;
import miui.util.async.tasks.HttpTask.Method;
import org.json.JSONObject;

public class HttpJsonObjectTask extends HttpTextTask {
    public HttpJsonObjectTask(String str) {
        super(str);
    }

    public HttpJsonObjectTask(HttpSession httpSession, String str) {
        super(httpSession, str);
    }

    public HttpJsonObjectTask(HttpSession httpSession, Method method, String str, Map map) {
        super(httpSession, method, str, map);
    }

    public JSONObject doLoad() {
        return new JSONObject(requestText());
    }
}
