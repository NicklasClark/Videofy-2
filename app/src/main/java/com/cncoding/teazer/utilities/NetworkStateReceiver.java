package com.cncoding.teazer.utilities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.HashSet;
import java.util.Set;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 *
 * Created by Prem $ on 10/6/2017.
 */

public class NetworkStateReceiver  extends BroadcastReceiver {

//    public static final String CONNECTIVITY_ACTION_LOLLIPOP = "com.cncoding.teazer.CONNECTIVITY_ACTION_LOLLIPOP";
    protected Set<NetworkStateListener> listeners;
    protected Boolean connected;

    public NetworkStateReceiver() {
        listeners = new HashSet<>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (manager != null) {
            networkInfo = manager.getActiveNetworkInfo();
        }

        if(networkInfo != null && networkInfo.isConnected()) {
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
        }
//        if (manager != null) {
//            manager.registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
//                @Override
//                public void onAvailable(Network network) {
//                    for(NetworkStateListener listener : listeners)
//                        listener.onNetworkAvailable();
//                }
//
//                @Override
//                public void onLost(Network network) {
//                    for(NetworkStateListener listener : listeners)
//                        listener.onNetworkUnavailable();
//                }
//            });
//        }
        notifyStateToAll();
    }

    private void notifyStateToAll() {
        for(NetworkStateListener listener : listeners)
            notifyState(listener);
    }

    private void notifyState(NetworkStateListener listener) {
        if(connected == null || listener == null)
            return;

        if(connected)
            listener.onNetworkAvailable();
        else
            listener.onNetworkUnavailable();
    }

    public void addListener(NetworkStateListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateListener l) {
        listeners.remove(l);
    }

    public interface NetworkStateListener {
        void onNetworkAvailable();
        void onNetworkUnavailable();
    }

    public static boolean isConnected(Activity activity) {
        ConnectivityManager conman = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (conman != null) {
            networkInfo = conman.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}