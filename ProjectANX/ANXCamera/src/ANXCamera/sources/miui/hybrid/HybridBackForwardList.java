package miui.hybrid;

public abstract class HybridBackForwardList {
    public int getCurrentIndex() {
        return -1;
    }

    public HybridHistoryItem getCurrentItem() {
        return null;
    }

    public HybridHistoryItem getItemAtIndex(int i) {
        return null;
    }

    public int getSize() {
        return 0;
    }
}
