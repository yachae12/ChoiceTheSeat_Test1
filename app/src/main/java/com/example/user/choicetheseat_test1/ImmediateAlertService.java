package com.example.user.choicetheseat_test1;

import java.util.UUID;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

public class ImmediateAlertService extends BluetoothGattServerCallback{
    private static final String TAG = "IAS";
    private byte[] mAlertLevel = new byte[] {
            (byte) 0x00
    };

    private BluetoothGattServer mGattServer;

    public void setupServices(BluetoothGattServer gattServer) {
        if (gattServer == null) {
            throw new IllegalArgumentException("gattServer is null");
        }
        mGattServer = gattServer;


    }

    public void onServiceAdded(int status, BluetoothGattService service) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.d(TAG, "onServiceAdded status=GATT_SUCCESS service="
                    + service.getUuid().toString());
        } else {
            Log.d(TAG, "onServiceAdded status!=GATT_SUCCESS");
        }
    }

    public void onConnectionStateChange(android.bluetooth.BluetoothDevice device, int status,
                                        int newState) {
    }


}
