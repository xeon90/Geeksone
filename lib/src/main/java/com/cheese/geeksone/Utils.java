package com.cheese.geeksone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Utils
{
    public static boolean HasConnectivity ()
    {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process ipProcess = runtime.exec("/system/bin/ping -c 2 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException io)
        {
            io.printStackTrace();
            return false;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean HasConnectivity (String destination)
    {
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process ipProcess = runtime.exec("/system/bin/ping -c 2 " + destination);
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException io)
        {
            io.printStackTrace();
            return false;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean Ping (String destination)
    {
        return HasConnectivity(destination);
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
