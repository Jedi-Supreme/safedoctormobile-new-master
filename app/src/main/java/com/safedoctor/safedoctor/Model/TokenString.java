package com.safedoctor.safedoctor.Model;

import com.safedoctor.safedoctor.Utils.App;
import com.safedoctor.safedoctor.Utils.SessionManagement;

/**
 * Created by kwabena on 8/17/17.
 */

public class TokenString {
    private static SessionManagement sessionManagement=new SessionManagement(App.context);
    public static String tokenString=sessionManagement.getLoginSession().get(sessionManagement.KEY_TOKEN_TYPE)+" "+sessionManagement.getLoginSession().get(sessionManagement.KEY_TOKEN);

}
