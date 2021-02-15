package com.miui.internal.log.message;

import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class MessageFactory {
    private static final int MAX_RECYCLED = 10;
    private static final String TAG = "MessageFactory";
    private static HashMap mCacheMap = new HashMap();

    class MessageCache {
        private Message[] iCache;
        private Constructor iConstructor;
        private int iPointer = 0;

        private MessageCache(Constructor constructor, Message[] messageArr) {
            this.iConstructor = constructor;
            this.iCache = messageArr;
        }

        private Message create() {
            try {
                return (Message) this.iConstructor.newInstance(new Object[0]);
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Fail to construct new instance of class: ");
                sb.append(this.iConstructor.getDeclaringClass().getName());
                Log.e(MessageFactory.TAG, sb.toString(), e);
                return null;
            }
        }

        public static MessageCache createInstance(Class cls, int i) {
            try {
                return new MessageCache(cls.getConstructor(new Class[0]), (Message[]) Array.newInstance(cls, i));
            } catch (NoSuchMethodException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("Class ");
                sb.append(cls.getName());
                sb.append(" must have a public empty constructor");
                throw new IllegalArgumentException(sb.toString());
            }
        }

        public synchronized Message obtain() {
            if (this.iPointer > 0) {
                this.iPointer--;
                Message message = this.iCache[this.iPointer];
                message.prepareForReuse();
                return message;
            }
            return create();
        }

        public synchronized void recycle(Message message) {
            if (this.iPointer < this.iCache.length) {
                this.iCache[this.iPointer] = message;
                this.iPointer++;
            }
        }
    }

    public static Message obtain(Class cls) {
        MessageCache messageCache = (MessageCache) mCacheMap.get(cls);
        if (messageCache == null) {
            messageCache = MessageCache.createInstance(cls, 10);
            mCacheMap.put(cls, messageCache);
        }
        return messageCache.obtain();
    }

    static void recycle(Message message) {
        MessageCache messageCache = (MessageCache) mCacheMap.get(message.getClass());
        if (messageCache != null) {
            messageCache.recycle(message);
        }
    }
}
