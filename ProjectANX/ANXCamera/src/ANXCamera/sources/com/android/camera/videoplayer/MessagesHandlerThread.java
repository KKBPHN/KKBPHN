package com.android.camera.videoplayer;

import com.android.camera.videoplayer.player_messages.Message;
import com.android.camera.videoplayer.utils.Logger;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class MessagesHandlerThread {
    private static final boolean SHOW_LOGS = false;
    /* access modifiers changed from: private */
    public static final String TAG = "MessagesHandlerThread";
    /* access modifiers changed from: private */
    public Message mLastMessage;
    /* access modifiers changed from: private */
    public final Queue mPlayerMessagesQueue = new ConcurrentLinkedQueue();
    /* access modifiers changed from: private */
    public final PlayerQueueLock mQueueLock = new PlayerQueueLock();
    private final Executor mQueueProcessingThread = Executors.newSingleThreadExecutor();
    /* access modifiers changed from: private */
    public AtomicBoolean mTerminated = new AtomicBoolean(false);

    public MessagesHandlerThread() {
        this.mQueueProcessingThread.execute(new Runnable() {
            public void run() {
                Logger.d(MessagesHandlerThread.TAG, "start worker thread");
                do {
                    MessagesHandlerThread.this.mQueueLock.lock(MessagesHandlerThread.TAG);
                    while (MessagesHandlerThread.this.mPlayerMessagesQueue.isEmpty()) {
                        try {
                            MessagesHandlerThread.this.mQueueLock.wait(MessagesHandlerThread.TAG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            throw new RuntimeException("InterruptedException");
                        }
                    }
                    MessagesHandlerThread messagesHandlerThread = MessagesHandlerThread.this;
                    messagesHandlerThread.mLastMessage = (Message) messagesHandlerThread.mPlayerMessagesQueue.poll();
                    MessagesHandlerThread.this.mLastMessage.polledFromQueue();
                    MessagesHandlerThread.this.mQueueLock.unlock(MessagesHandlerThread.TAG);
                    MessagesHandlerThread.this.mLastMessage.runMessage();
                    MessagesHandlerThread.this.mQueueLock.lock(MessagesHandlerThread.TAG);
                    MessagesHandlerThread.this.mLastMessage.messageFinished();
                    MessagesHandlerThread.this.mQueueLock.unlock(MessagesHandlerThread.TAG);
                } while (!MessagesHandlerThread.this.mTerminated.get());
                Logger.d(MessagesHandlerThread.TAG, "end worker thread");
            }
        });
    }

    public void addMessage(Message message) {
        this.mQueueLock.lock(TAG);
        this.mPlayerMessagesQueue.add(message);
        this.mQueueLock.notify(TAG);
        this.mQueueLock.unlock(TAG);
    }

    public void addMessages(List list) {
        this.mQueueLock.lock(TAG);
        this.mPlayerMessagesQueue.addAll(list);
        this.mQueueLock.notify(TAG);
        this.mQueueLock.unlock(TAG);
    }

    public void clearAllPendingMessages(String str) {
        if (this.mQueueLock.isLocked(str)) {
            this.mPlayerMessagesQueue.clear();
            return;
        }
        throw new RuntimeException("cannot perform action, you are not holding a lock");
    }

    public void pauseQueueProcessing(String str) {
        this.mQueueLock.lock(str);
    }

    public void resumeQueueProcessing(String str) {
        this.mQueueLock.unlock(str);
    }

    public void terminate() {
        Logger.d(TAG, ">> terminate lock");
        this.mQueueLock.lock(TAG);
        this.mTerminated.set(true);
        this.mQueueLock.notify(TAG);
        this.mQueueLock.unlock(TAG);
        Logger.d(TAG, "<< terminate unlock");
    }
}
