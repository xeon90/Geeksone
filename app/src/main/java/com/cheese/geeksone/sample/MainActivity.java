package com.cheese.geeksone.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cheese.geeksone.Container;
import com.cheese.geeksone.Geeksone;
import com.cheese.geeksone.OnCancelledListener;
import com.cheese.geeksone.OnResultListener;

public class MainActivity extends AppCompatActivity implements OnResultListener, OnCancelledListener
{
    Geeksone Gs = new Geeksone()
                    .setTimeout(5000)
                    .TrustAllCert(true)
                    .TrustAllHost(true);

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gs.GET(new Container("http://json.com/get")
            .setOnResult(this)
            .setOnCancelled(this));
    }

    @Override public void OnCancelled (Exception cause, boolean isConnection, Container container, Geeksone gs)
    {
        //Do something
        //Retry:
        gs.RETRY(container);
    }

    @Override public void OnResult (Boolean result, Container container, Geeksone async)
    {
        if(result)
        {
            Response resp = async.getClazz(Response.class);
            //Do something
        }
    }

    class Response
    {

    }
}
