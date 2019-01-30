package com.safedoctor.safedoctor.Utils;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stevkkys on 9/17/2017.
 */

public class StringUtils
{
    public static String join(String...list )
    {
        if ( list == null )
        {
            return null;
        }

       String seperator = list[0];
        String data = "";
        for(int i=1;i<list.length;i++)
        {
            if(null != list[i])
            {
                if (data.isEmpty()) {
                    data = list[i];
                } else {
                    data += seperator + list[i];
                }
            }
        }

        return data;
    }



}
