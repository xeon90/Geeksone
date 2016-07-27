package com.cheese.geeksone.core;

public interface OnCancelledListener
{
    void OnError (Exception cause, boolean isConnection, Container container, Geeksone gs);
}
