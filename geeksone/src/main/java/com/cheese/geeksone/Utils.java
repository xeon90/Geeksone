package com.cheese.geeksone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils
{
    public static boolean HasConnectivity (Context c)
    {
        boolean result;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        result = info != null && info.isAvailable() && info.isConnectedOrConnecting();

        if(!result)
        {
            try
            {
                Runtime runtime = Runtime.getRuntime();
                Process ipProcess = runtime.exec("/system/bin/ping -c 2 8.8.8.8");
                int exitValue = ipProcess.waitFor();
                result = (exitValue == 0);
            }
            catch (Exception io)
            {
                io.printStackTrace();
                result = false;
            }
        }

        return result;
    }

    public static boolean IsValidJSON (String test)
    {
        try
        {
            new JSONObject(test);
        }
        catch (JSONException ex)
        {
            try
            {
                new JSONArray(test);
            }
            catch (JSONException ex1)
            {
                return false;
            }
        }
        return true;
    }
}
