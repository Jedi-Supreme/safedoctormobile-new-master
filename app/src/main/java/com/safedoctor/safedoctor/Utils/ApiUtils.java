package com.safedoctor.safedoctor.Utils;


import com.safedoctor.safedoctor.Api.SafeDoctorClient;
import com.safedoctor.safedoctor.Api.SafeDoctorService;

public class ApiUtils {
    private ApiUtils() {}
    public static final String BASE_URL = "http://safedoktor.com:1350/";
    //public static final String BASE_URL = "http://173.208.234.34:1350/";

    //public static final String BASE_URL = "http://192.168.1.101:1350/";




  //  public static final String BASE_URL = "http://192.168.1.6:1350/";
  
   //public static final String BASE_URL = "http://infotechhims.com:1350/";
    public static SafeDoctorService getAPIService() {
        return SafeDoctorClient.getClient(BASE_URL).create(SafeDoctorService.class);
    }
}

