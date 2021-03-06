package com.cheese.geeksone.core;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.util.Map;

public class Container
{
    private Activity mActivity = null;
    private int Actions;
    private String URL;
    private Mode Mode;
    private OnCancelledListener mOnCancelled;
    private OnResultListener mOnResult;
    private OnGlobalListener mOnGlobal;
    private Map<String ,String> mHeader = null;
    private Map<?, ?> mForm;
    private String mBasicUsername, mBasicPassword;
    private Boolean Basic = false;

    @Nullable
    private Object RequestBody;

    public Container (String url)
    {
        URL = url;
    }

    public String getURL ()
    {
        return URL;
    }

    public Container setURL (String URL)
    {
        this.URL = URL;
        return this;
    }

    public Container setActivity(Activity act)
    {
        mActivity = act;
        return this;
    }

    Activity getActivity()
    {
        return mActivity;
    }

    public Container setActions (int actions)
    {
        Actions = actions;
        return this;
    }

    public int getActions ()
    {
        return Actions;
    }

    @Nullable
    public Object getRequestBody ()
    {
        return RequestBody;
    }

    public Container setRequestBody (@Nullable Object requestBody)
    {
        RequestBody = requestBody;
        return this;
    }

    public Container setMode (Mode m)
    {
        Mode = m;
        return this;
    }

    public Mode getMode ()
    {
        return Mode;
    }

    public OnCancelledListener getOnCancelled ()
    {
        return mOnCancelled;
    }

    public Container setOnCancelled (OnCancelledListener mOnCancelled)
    {
        this.mOnCancelled = mOnCancelled;
        return this;
    }

    public OnResultListener getOnResult ()
    {
        return mOnResult;
    }

    public Container setOnResult (OnResultListener mOnResult)
    {
        this.mOnResult = mOnResult;
        return this;
    }

    public Map<String, String> getHeader ()
    {
        return mHeader;
    }

    public Container setHeaders (Map<String, String> mHeader)
    {
        this.mHeader = mHeader;
        return this;
    }

    public Container setBasicAuth(String username, String password)
    {
        mBasicPassword = password;
        mBasicUsername = username;
        Basic = true;
        return this;
    }

    boolean hasBasic()
    {
        return Basic;
    }

    String BasicUsername()
    {
        return mBasicUsername;
    }

    String BasicPassword()
    {
        return mBasicPassword;
    }

    public Map<?, ?> getFormData ()
    {
        return mForm;
    }

    public Container setFormData (Map<?, ?> mForm)
    {
        this.mForm = mForm;
        return this;
    }

    public OnGlobalListener getOnGlobalListner ()
    {
        return mOnGlobal;
    }

    public Container setOnGlobalListener (OnGlobalListener mOnGlobal)
    {
        this.mOnGlobal = mOnGlobal;
        return this;
    }
}
