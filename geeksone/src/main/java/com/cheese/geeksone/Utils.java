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
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnectedOrConnecting();
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
