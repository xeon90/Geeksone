package com.cheese.geeksone.sample;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheese.geeksone.core.Container;
import com.cheese.geeksone.core.Geeksone;
import com.cheese.geeksone.core.OnCancelledListener;
import com.cheese.geeksone.core.OnResultListener;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements OnResultListener, OnCancelledListener
{
    //TODO: Add POST form
    //TODO: Progress Dialog Theme Color
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
        mActivity = this
        ;
        mGS = new Geeksone()
            .setTimeout(3000);

        btnGET.setOnClickListener(new View.OnClickListener()
        {
            @Override public void onClick (View v)
            {
//                mGS.GET(new Container("http://echo.jsontest.com/name/tester/age/30")
//                    .setOnResult(MainActivity.this)
//                    .setOnCancelled(MainActivity.this));
                try
                {
                    Log.e("Debug", (mActivity == null) + "");
                    JSONObject json = new JSONObject();
                    json.put("msisdn", "393948922");
                    json.put("deviceId", "393993jd29dj92jd");

                    new Geeksone()
                        .POST(new Container("http://appproxy.tunetalk.net/selfcareapp/api/reqPin")
                            .setActivity(mActivity)
                            .setOnResult(new OnResultListener()
                            {
                                @Override public void OnResult (Boolean result, Container container, Geeksone async, Exception ex)
                                {
                                    Log.e("Debug", async.getResponse());
                                }
                            })
                            .setRequestBody(json));
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
}
