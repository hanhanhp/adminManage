package com.link.common.kit;

/**
 * Created by linkzz on 2017-07-20.
 */
public class StrKit extends com.jfinal.kit.StrKit {
    public static String subString(String str,int num){
        if (str.length() > num){
            return str.substring(0,num);
        }else {
            return str;
        }
    }
}
