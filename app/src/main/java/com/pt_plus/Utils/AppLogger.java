package com.pt_plus.Utils;

import android.util.Log;




public class AppLogger {



    public static void log(String logMsg,String ... key){
        if(true) {
            String newKey = "PT PLUS";
            if(key != null && key.length > 0){
                newKey = newKey+">>"+key[0];
            }
            Log.e(newKey, logMsg);
        }
        logMsg = null;
        key = null;
    }
}
