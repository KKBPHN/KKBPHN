package com.google.android.play.core.tasks;

import java.util.concurrent.Executor;

public abstract class Task {
    public abstract Task addOnCompleteListener(OnCompleteListener onCompleteListener);

    public abstract Task addOnCompleteListener(Executor executor, OnCompleteListener onCompleteListener);

    public abstract Task addOnFailureListener(OnFailureListener onFailureListener);

    public abstract Task addOnFailureListener(Executor executor, OnFailureListener onFailureListener);

    public abstract Task addOnSuccessListener(OnSuccessListener onSuccessListener);

    public abstract Task addOnSuccessListener(Executor executor, OnSuccessListener onSuccessListener);

    public abstract Exception getException();

    public abstract Object getResult();

    public abstract Object getResult(Class cls);

    public abstract boolean isComplete();

    public abstract boolean isSuccessful();
}
