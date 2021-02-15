package org.jcodec.containers.mp4;

import java.util.HashMap;
import java.util.Map;

public abstract class Boxes {
    protected final Map mappings = new HashMap();

    public void clear() {
        this.mappings.clear();
    }

    public void override(String str, Class cls) {
        this.mappings.put(str, cls);
    }

    public Class toClass(String str) {
        return (Class) this.mappings.get(str);
    }
}
