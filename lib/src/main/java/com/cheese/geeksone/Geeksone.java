package com.cheese.geeksone;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.cheese.geeksone.lib.HttpRequest;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

public class Geeksone
{
    Object mRequest;
    Container mContainer;
    HttpRequest mHttpRequest;

    Mode mRequestMode;
    OnResultListener mResultListener;
    OnCancelledListener mCancelledListener;

    String mURL, mResponse;
    int mTimeout = 5000;
    boolean mTrustAllCert = false, mTrustAllHost = false;

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

    private void setOnResultListener (OnResultListener listener)
    {
        mResultListener = listener;
    }

    private void setOnCancelledListener (OnCancelledListener listener)
    {
        mCancelledListener = listener;
    }

    public Geeksone TrustAllHost(boolean b)
    {
        mTrustAllHost = b;
        return this;
    }

    public Geeksone TrustAllCert(boolean b)
    {
        mTrustAllCert = b;
        return this;
    }

    public HttpRequest getHttpRequest()
    {
        return mHttpRequest;
    }

    public Geeksone setHttpRequest(HttpRequest req)
    {
        mHttpRequest = req;
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

    private void built()
    {
        mHttpRequest = null;

        if(mRequestMode == Mode.GET)
        {
            mHttpRequest = HttpRequest
                .get(mURL)
                .readTimeout(mTimeout)
                .connectTimeout(mTimeout);
        }

        if(mRequestMode == Mode.POST)
        {
            mHttpRequest = HttpRequest
                .post(mURL)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(getRequest(mRequest));
        }

        if(mRequestMode == Mode.PUT)
        {
            mHttpRequest = HttpRequest
                .put(mURL)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(getRequest(mRequest))
                .readTimeout(mTimeout)
                .connectTimeout(mTimeout);
        }

        if (mRequestMode == Mode.DELETE)
        {
            mHttpRequest = HttpRequest
                .delete(mURL)
                .contentType(HttpRequest.CONTENT_TYPE_JSON)
                .send(getRequest(mRequest));
        }

        if(mHttpRequest != null)
        {
            if(mContainer.getHeader() != null)
                mHttpRequest.headers(mContainer.getHeader());

            if(mContainer.hasBasic())
                mHttpRequest.basic(mContainer.BasicUsername(), mContainer.BasicPassword());

            mHttpRequest.readTimeout(mTimeout)
                .connectTimeout(mTimeout);
        }
        else
            PokeOnError(new Exception("Unsupported REST Mode"));
    }

    private void start ()
    {
        new AsyncTask<Void, Void, Boolean>()
        {
            @Override
            protected Boolean doInBackground (Void... params)
            {
                if (mResultListener == null)
                {
                    PokeOnError(new Exception("OnResultListener cannot be null"));
                    return false;
                }
                else
                {
                    if (Utils.HasConnectivity())
                    {
                        try
                        {
                            mResponse = mHttpRequest.body();
                            return true;
                        }
                        catch (Exception e)
                        {
                            if (e.getCause() instanceof ConnectException || e.getCause() instanceof TimeoutException || e.getCause() instanceof SocketTimeoutException)
                                PokeOnConnectionTimeOut(e);
                            else
                                PokeOnError(e);

                            e.printStackTrace();
                            return false;
                        }
                    }
                    else
                    {
                        Exception cep = new Exception("No connectivity found");
                        PokeOnError(cep);
                        return false;
                    }
                }
            }

            @Override protected void onPreExecute ()
            {
                super.onPreExecute();

                try
                {
                    built();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    PokeOnError(ex);
                }

                if (mRequest == null && mRequestMode == Mode.POST)
                    PokeOnError(new NullPointerException("POST must given Request Body"));

                if (getRequest(mRequest) == null)
                    PokeOnError(new NullPointerException("Unable to handle type of Request Body"));
            }

            @Override protected void onPostExecute (Boolean aBoolean)
            {
                super.onPostExecute(aBoolean);
                if (aBoolean)
                {
                    if (getResponse() == null)
                    {
                        PokeOnError(new Exception("Response is null"));
                        mResultListener.OnResult(false, mContainer, Geeksone.this);
                    }
                    else if (getJSON() == null)
                    {
                        PokeOnError(new Exception("Response is null or not a Valid JSON"));
                        mResultListener.OnResult(false, mContainer, Geeksone.this);
                    }
                    else
                    {
                        mResultListener.OnResult(true, mContainer, Geeksone.this);
                    }
                }
            }
        }.execute();
    }

    private void PokeOnError (final Exception ex)
    {
        new Handler(Looper.getMainLooper())
            .post(new Runnable()
            {
                @Override public void run ()
                {
                    if (mCancelledListener != null)
                        mCancelledListener.OnCancelled(ex, false, mContainer, Geeksone.this);

                    if (mResultListener != null)
                        mResultListener.OnResult(false, mContainer, Geeksone.this);
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
                        mCancelledListener.OnCancelled(ex, true, mContainer, Geeksone.this);

                    if (mResultListener != null)
                        mResultListener.OnResult(false, mContainer, Geeksone.this);
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

    public Container getContainer ()
    {
        return mContainer;
    }
}
