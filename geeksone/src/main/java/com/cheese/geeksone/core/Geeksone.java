package com.cheese.geeksone.core;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;

import com.cheese.geeksone.R;
import com.cheese.geeksone.Utils;
import com.cheese.geeksone.lib.HttpRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Geeksone
{
    Activity mActivity;
    Object mRequest;
    Container mContainer;
    HttpRequest mHttpRequest;
    boolean mAutoPilot = false;

    Mode mRequestMode;
    OnResultListener mResultListener;
    OnCancelledListener mCancelledListener;

    String mURL, mResponse;
    int mTimeout = 8000;

    AlertDialog mDialog;
    ProgressDialog mProgressDialog;

    public Geeksone ()
    {

    }

    public Geeksone GET (String url)
    {
        mURL = url;
        mRequestMode = Mode.GET;
        mContainer = new Container(url).setMode(Mode.GET);
        setOnResultListener(mContainer.getOnResult());
        setOnCancelledListener(mContainer.getOnCancelled());
        start();
        return this;
    }

    public Geeksone POST (String url, Object obj)
    {
        mURL = url;
        mRequestMode = Mode.POST;
        mRequest = obj;
        mContainer = new Container(url).setRequestBody(obj).setMode(Mode.POST);
        setOnResultListener(mContainer.getOnResult());
        setOnCancelledListener(mContainer.getOnCancelled());
        start();
        return this;
    }

    public Geeksone GET (Container container)
    {
        mContainer = container;
        mURL = container.getURL();
        mRequestMode = Mode.GET;
        mContainer.setMode(mRequestMode);
        setOnResultListener(mContainer.getOnResult());
        setOnCancelledListener(mContainer.getOnCancelled());
        start();
        return this;
    }

    public Geeksone POST (Container container)
    {
        mContainer = container;
        mURL = container.getURL();
        mRequest = container.getRequestBody();
        mRequestMode = Mode.POST;
        mContainer.setMode(mRequestMode);
        setOnResultListener(mContainer.getOnResult());
        setOnCancelledListener(mContainer.getOnCancelled());
        start();
        return this;
    }

    public Geeksone setTimeout (int timeoutInMs)
    {
        mTimeout = timeoutInMs;
        return this;
    }

    public Geeksone setAlertDialog(AlertDialog dialog)
    {
        this.mDialog = dialog;
        return this;
    }

    public Geeksone setProgressDialog(ProgressDialog progressDialog)
    {
        this.mProgressDialog = progressDialog;
        return this;
    }

    public Geeksone RETRY (Container c)
    {
        if (c.getMode() == Mode.GET)
            GET(c);

        if (c.getMode() == Mode.POST)
            POST(c);

        return this;
    }

    private void setOnResultListener (OnResultListener listener)
    {
        mResultListener = listener;
    }

    private void setOnCancelledListener (OnCancelledListener listener)
    {
        mCancelledListener = listener;
    }

    private void start ()
    {
        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground (Void... params)
            {
                if(!isCancelled())
                {
                    try
                    {
                        mHttpRequest = null;

                        if(mRequestMode == Mode.GET)
                        {
                            mHttpRequest = HttpRequest
                                .get(mURL)
                                .readTimeout(mTimeout)
                                .connectTimeout(mTimeout);
                        }
                        else if(mRequestMode == Mode.POST)
                        {
                            mHttpRequest = HttpRequest
                                .post(mURL)
                                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                                .send(getRequest(mRequest));
                        }
                        else if(mRequestMode == Mode.PUT)
                        {
                            mHttpRequest = HttpRequest
                                .put(mURL)
                                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                                .send(getRequest(mRequest))
                                .readTimeout(mTimeout)
                                .connectTimeout(mTimeout);
                        }
                        else if (mRequestMode == Mode.DELETE)
                        {
                            mHttpRequest = HttpRequest
                                .delete(mURL)
                                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                                .send(getRequest(mRequest));
                        }
                        else
                            PokeOnError(new Exception("Unsupported REST Mode"));

                        if(mHttpRequest != null)
                        {
                            if(mContainer.getHeader() != null)
                                mHttpRequest.headers(mContainer.getHeader());

                            if(mContainer.hasBasic())
                                mHttpRequest.basic(mContainer.BasicUsername(), mContainer.BasicPassword());

                            mHttpRequest.readTimeout(mTimeout)
                                .connectTimeout(mTimeout);
                        }

                        mResponse = mHttpRequest.body();
                        return true;
                    }
                    catch (Exception e)
                    {
                        Error(e);
                        e.printStackTrace();
                        return false;
                    }
                }
                else
                    return false;
            }

            @Override protected void onPreExecute ()
            {
                super.onPreExecute();
                mAutoPilot = mContainer.getActivity() != null;

                if(mAutoPilot)
                {
                    mActivity = mContainer.getActivity();
                    mDialog = new AlertDialog.Builder(mActivity)
                        .setCancelable(true)
                        .setNegativeButton("CANCEL", null)
                        .create();

                    mProgressDialog = new ProgressDialog(mActivity);
                    mProgressDialog.setMessage(mActivity.getString(R.string.gk_pb_message));
                    mProgressDialog.setTitle(mActivity.getString(R.string.gk_pb_title));
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                }

                if(mAutoPilot && mActivity != null)
                {
                    mActivity.runOnUiThread(new Runnable()
                    {
                        @Override public void run ()
                        {
                            if(mProgressDialog.isShowing())
                                mProgressDialog.dismiss();

                            mProgressDialog.show();
                        }
                    });
                }

                if (getRequest(mRequest) == null)
                    this.cancel(true);
            }

            @Override protected void onPostExecute (Boolean aBoolean)
            {
                super.onPostExecute(aBoolean);

                if(mAutoPilot && mActivity != null)
                {
                    mActivity.runOnUiThread(new Runnable()
                    {
                        @Override public void run ()
                        {
                            if(mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }
                    });
                }

                if (aBoolean)
                {
                    if (getResponse() == null)
                    {
                        if(mAutoPilot)
                            ShowDialog(R.string.gk_err_no_response);
                        else
                            PokeOnError(new Exception(mActivity.getString(R.string.gk_err_no_response)));
                    }
                    else if (getJSON() == null)
                    {
                        mResultListener.OnResult(false, mContainer, Geeksone.this, new Exception("Response is null or not a valid JSON"));
                    }
                    else
                    {
                        mResultListener.OnResult(true, mContainer, Geeksone.this, null);
                    }
                }
                else
                {
                    if (mResultListener == null)
                    {
                        PokeOnError(new Exception("OnResultListener cannot be null"));
                    }
                    else if (getRequest(mRequest) == null)
                    {
                        PokeOnError(new NullPointerException("Unable to handle request body"));
                    }
                }
            }
        }.execute();
    }

    private void Error(Exception e)
    {
        if(Utils.HasConnectivity(mActivity))
        {
            if (e.getCause() instanceof ConnectException || e.getCause() instanceof TimeoutException || e.getCause() instanceof SocketTimeoutException)
            {
                if(mAutoPilot)
                    ShowDialog(R.string.gk_err_connection_timeout);
                else
                    PokeOnConnectionTimeOut(e);
            }
            else if(e.getCause() instanceof UnknownHostException || e.getCause() instanceof ConnectException || e.getCause() instanceof NoRouteToHostException)
            {
                if(mAutoPilot)
                    ShowDialog(R.string.gk_err_host_unreachable);
                else
                    PokeOnError(e);
            }
            else if(e.getCause() instanceof MalformedURLException)
            {
                if(mAutoPilot)
                    ShowDialog(R.string.gk_err_malformed_url);
                else
                    PokeOnError(e);
            }
            else
            {
                if(mAutoPilot)
                    ShowDialog(R.string.gk_err_unknown);
                else
                    PokeOnError(e);
            }
        }
        else
        {
            if(mAutoPilot)
                ShowDialog(R.string.gk_err_no_internet);
            else
                PokeOnError(e);
        }
    }

    private void ShowDialog (final int stringId)
    {
        new Handler(Looper.getMainLooper())
            .post(new Runnable()
            {
                @Override public void run ()
                {
                    mDialog.setMessage(mActivity.getString(stringId));
                    mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "RETRY", new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick (DialogInterface dialog, int which)
                        {
                            RETRY(mContainer);
                        }
                    });
                    mDialog.show();
                }
            });
    }

    private void ShowDialog (final String msg)
    {
        new Handler(Looper.getMainLooper())
            .post(new Runnable()
            {
                @Override public void run ()
                {
                    mDialog.setMessage(msg);
                    mDialog.setButton(DialogInterface.BUTTON_POSITIVE, "RETRY", new DialogInterface.OnClickListener()
                    {
                        @Override public void onClick (DialogInterface dialog, int which)
                        {
                            RETRY(mContainer);
                        }
                    });
                    mDialog.show();
                }
            });
    }

    private void PokeOnError (final Exception ex)
    {
        new Handler(Looper.getMainLooper())
            .post(new Runnable()
            {
                @Override public void run ()
                {
                    if (mCancelledListener != null)
                        mCancelledListener.OnError(ex, false, mContainer, Geeksone.this);
                }
            });
    }

    private void PokeOnConnectionTimeOut (final Exception ex)
    {
        new Handler(Looper.getMainLooper())
            .post(new Runnable()
            {
                @Override public void run ()
                {
                    if (mCancelledListener != null)
                        mCancelledListener.OnError(ex, true, mContainer, Geeksone.this);
                }
            });
    }

    public String getRequest (Object obj)
    {
        if (mRequest instanceof JSONObject)
            return ((JSONObject) obj).toString();
        else if (mRequest instanceof String)
            return (String) obj;
        else
            return new GsonBuilder().create().toJson(obj);
    }

    public String getResponse ()
    {
        return mResponse;
    }

    public String getJSON ()
    {
        if (mResponse == null)
        {
            return null;
        }
        else
        {
            if (Utils.IsValidJSON(mResponse))
                return mResponse;
            else
                return null;
        }
    }

    public <T> T getClazz (Class<T> clazz)
    {
        if (getJSON() != null)
        {
            try
            {
                return new GsonBuilder().create().fromJson(getJSON(), clazz);
            }
            catch (Exception ex)
            {
                PokeOnError(ex);
                return null;
            }
        }
        else
        {
            PokeOnError(new Exception("Response is null"));
            return null;
        }
    }

    public <T> T getClazz (Type t)
    {
        try
        {
            return new GsonBuilder().create().fromJson(getJSON(), t);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public String getHeader(String field)
    {
        if(mHttpRequest != null)
            return mHttpRequest.header(field);
        else
            return null;
    }

    public Container getContainer ()
    {
        return mContainer;
    }
}
