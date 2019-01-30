package com.safedoctor.safedoctor.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by stevkky on 07/06/2018.
 */

public interface IChatServiceState
{
     void OnStateChange(int state, BluetoothDevice device);

     void OnConnectionFailed(int failuretype, BluetoothDevice device);

}
