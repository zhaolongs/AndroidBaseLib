package com.studyyoun.androidbaselibrary.model;

import java.io.Serializable;

public class RequestModel implements Serializable {
    /**
     *
     "url": url,
     "method": method,
     "data": params,
     "header": requestHeader,

     */

    public  String url;
    public  String method;
    public  String data;
    public  String header;
}
