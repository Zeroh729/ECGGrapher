package android.zeroh729.com.ecggrapher.data.model;


import android.bluetooth.BluetoothDevice;

public class BluetoothDeviceItem {
    String deviceName;
    String deviceAddress;

    public BluetoothDeviceItem(String deviceName, String deviceAddress) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
    }

    public BluetoothDeviceItem(BluetoothDevice device) {
        this.deviceName = device.getName();
        this.deviceAddress = device.getAddress();
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }
}
