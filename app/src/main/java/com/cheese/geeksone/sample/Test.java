package com.cheese.geeksone.sample;

import com.cheese.geeksone.lib.HttpRequest;

public class Test
{
    public static void main(String[] args)
    {
        int day = 15;
        int remaining = 0;

        while((day + remaining) % 7 != 0)
        {
            ++remaining;
        }


        int val = 2;
        int max = 0;
        while (val > 0)
        {
            val = val - 7;
            ++max;
        }

        System.out.println(max);
    }
}
