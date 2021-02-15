package androidx.lifecycle;

public class MutableLiveData extends LiveData {
    public MutableLiveData() {
    }

    public MutableLiveData(Object obj) {
        super(obj);
    }

    public void postValue(Object obj) {
        super.postValue(obj);
    }

    public void setValue(Object obj) {
        super.setValue(obj);
    }
}
