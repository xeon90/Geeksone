package com.cheese.geeksone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils
{
    public static boolean HasConnectivity (Context c)
    {
        Log.e("Geeksone", "Context: " + String.valueOf(c == null));

        if(c == null)
            return false;

        try
        {
            ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            Log.e("Geeksone", "Network state: " + String.valueOf(info != null && info.isAvailable() && info.isConnectedOrConnecting()));
            return info != null && info.isAvailable() && info.isConnectedOrConnecting();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
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
