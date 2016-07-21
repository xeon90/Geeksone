package com.cheese.geeksone;

public interface OnCancelledListener
{
    void OnCancelled (Exception cause, boolean isConnection, Container container, Geeksone gs);
}
