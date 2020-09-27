package com.cesar.alseademo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityNetWorkUtils {
    Context context;

    public ConnectivityNetWorkUtils (Context context) {
        this.context = context;
    }

    /**
     * Verifica conexión activa
     * @return
     */
    public boolean isConnected() {
        boolean res = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        res = networkInfo != null && networkInfo.isConnected();
        return res;
    }
}
