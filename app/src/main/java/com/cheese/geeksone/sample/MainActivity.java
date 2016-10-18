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
import com.cheese.geeksone.core.OnResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnResultListener, OnCancelledListener
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

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });

        new Geeksone(ContentType.NULL)
            .POST(new Container("http://app.nextngin.com/api/rest/6/activitylist")
                .setRequestBody("apiKey=com.berjaya.ansa9b42ec37-a3a8-40c0-931c-cd05f9057d96")
                .setOnResult(new OnResultListener()
                {
                    @Override public void OnResult (Boolean result, Container container, Geeksone async, Exception ex)
                    {
                        try
                        {
                            Log.e("Debug", async.getResponse());
                        }
                        catch (Exception exx)
                        {
                            ex.printStackTrace();
                        }
                    }
                }));
    }

    @Override public void OnError (Exception cause, boolean isConnection, Container container, Geeksone gs)
    {

    }

    @Override public void OnResult (Boolean result, Container container, Geeksone async, Exception ex)
    {
        if(result)
            tvTextView.setText(async.getJSON());
    }
}
