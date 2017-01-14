package com.cheese.geeksone.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheese.geeksone.core.Container;
import com.cheese.geeksone.core.ContentType;
import com.cheese.geeksone.core.Geeksone;
import com.cheese.geeksone.core.OnCancelledListener;
import com.cheese.geeksone.core.OnGlobalListener;
import com.cheese.geeksone.core.OnResultListener;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnResultListener, OnCancelledListener, OnGlobalListener
{
    Geeksone mGS, mGS2, mGS3;
    TextView tvTextView;
    Button btnGET;
    Activity mActivity;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGET = (Button) findViewById(R.id.Button);
        tvTextView = (TextView) findViewById(R.id.tvTextView);
        mActivity = this;
        mGS = new Geeksone()
            .setTimeout(3000);

        btnGET.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick (View v)
            {
                try
                {
                    mGS.GET(new Container("https://gateway.yangsifu.com/api/router?action=list")
                        .setOnGlobalListener(MainActivity.this)
                        .setOnResult(MainActivity.this)
                        .setActivity(MainActivity.this));
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override public void OnError (Exception cause, boolean isConnection, Container container, Geeksone gs)
    {

    }

    @Override public void OnResult (Boolean result, Container container, Geeksone async, Exception ex)
    {
        if(result)
            tvTextView.setText(async.getJSON());
    }

    @Override public void OnGlobalListener (Container container, Geeksone async)
    {
        Log.e("Debug", async.getResponse());
    }
}
