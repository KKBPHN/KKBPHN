package com.google.android.play.core.remote;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class RemoteServiceException extends RuntimeException {
    public RemoteServiceException() {
        super("Failed to bind to the service.");
    }

    public RemoteServiceException(String str) {
        super(str);
    }

    public RemoteServiceException(String str, Throwable th) {
        super(str, th);
    }
}
