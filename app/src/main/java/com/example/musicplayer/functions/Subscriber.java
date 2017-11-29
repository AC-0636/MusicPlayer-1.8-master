package com.example.musicplayer.functions;

/**
 * TODO
 */
public interface Subscriber<T> {
    void onComplete(T t);
    void onError(Exception e);
}
