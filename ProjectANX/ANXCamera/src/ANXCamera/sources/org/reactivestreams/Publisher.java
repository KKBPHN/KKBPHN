package org.reactivestreams;

public interface Publisher {
    void subscribe(Subscriber subscriber);
}
