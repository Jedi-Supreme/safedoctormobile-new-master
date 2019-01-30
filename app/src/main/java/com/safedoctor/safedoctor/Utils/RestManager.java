package com.safedoctor.safedoctor.Utils;

import java.net.HttpURLConnection;

import retrofit2.Response;

/**
 * Created by Stevkkys on 9/6/2017.
 */

public class RestManager<T> {

    public boolean isValidResponse(Response<T> response)
    {
        if(response.code() == HttpURLConnection.HTTP_OK || response.code() == HttpURLConnection.HTTP_CREATED )
        {
            return true;
        }
        else
        {
            return  false;
        }
    }
}
