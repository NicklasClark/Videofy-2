package com.cncoding.teazer.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

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
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
            connected = false;
        }

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

//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    private void registerConnectivityActionLollipop(final Context context) {
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//            return;
//
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkRequest.Builder builder = new NetworkRequest.Builder();
//
//        connectivityManager.registerNetworkCallback(builder.build(), new ConnectivityManager.NetworkCallback() {
//            @Override
//            public void onAvailable(Network network) {
//                Intent intent = new Intent(CONNECTIVITY_ACTION_LOLLIPOP);
//                intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
//                connected = true;
//                context.sendBroadcast(intent);
//            }
//
//            @Override
//            public void onLost(Network network) {
//                Intent intent = new Intent(CONNECTIVITY_ACTION_LOLLIPOP);
//                intent.putExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
//                connected = false;
//                context.sendBroadcast(intent);
//            }
//        });
//    }

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

    public static boolean isConnected(FragmentActivity fragmentActivity) {
        ConnectivityManager conman = (ConnectivityManager) fragmentActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}