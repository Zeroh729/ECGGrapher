package android.zeroh729.com.ecggrapher.ui.main.views.viewholders;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.model.BluetoothDeviceItem;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.row_device)
public class DeviceViewHolder extends LinearLayout {
    @ViewById
    TextView device_name;

    @ViewById
    TextView device_address;

    public DeviceViewHolder(Context context) {
        super(context);
    }

    public void bind(BluetoothDeviceItem device) {
        device_name.setText(device.getDeviceName());
        device_address.setText(device.getDeviceAddress());
    }

    public void bind(String name, String address) {
        device_name.setText(name);
        device_address.setText(address);
    }
}
