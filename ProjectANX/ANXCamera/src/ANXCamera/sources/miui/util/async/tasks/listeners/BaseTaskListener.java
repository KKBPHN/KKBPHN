package miui.util.async.tasks.listeners;

import miui.util.async.Task;
import miui.util.async.Task.Listener;
import miui.util.async.TaskManager;

public abstract class BaseTaskListener implements Listener {
    public void onCanceled(TaskManager taskManager, Task task) {
    }

    public void onException(TaskManager taskManager, Task task, Exception exc) {
    }

    public void onFinalize(TaskManager taskManager, Task task) {
    }

    public void onPrepare(TaskManager taskManager, Task task) {
    }

    public void onProgress(TaskManager taskManager, Task task, int i, int i2) {
    }

    public Object onResult(TaskManager taskManager, Task task, Object obj) {
        return obj;
    }
}
